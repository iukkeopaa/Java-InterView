## 分布式寻址算法

- hash 算法（大量缓存重建）
- 一致性 hash 算法（自动缓存迁移）+ 虚拟节点（自动负载均衡）
- Redis cluster 的 hash slot 算法

## Redis cluster 的 hash slot 算法

### **1. 哈希槽基本概念**

- **总槽数**：固定为 **16384 个**（编号 0~16383）。

- 分配规则

  ：每个 Redis 节点负责一部分哈希槽。例如，一个三节点的集群可能分配：

    - 节点 A：0~5460
    - 节点 B：5461~10922
    - 节点 C：10923~16383

- **数据定位**：每个键通过哈希算法映射到一个槽，再根据槽的分配找到对应节点。

### **2. 哈希槽计算过程**

#### **步骤 1：计算键的哈希值**

使用 **CRC16 算法** 计算键的哈希值，得到一个 16 位整数（0~65535）。

#### **步骤 2：映射到哈希槽**

将哈希值对 **16384** 取模，得到对应的槽编号：



plaintext











```plaintext
slot = CRC16(key) % 16384
```

#### **示例**

假设键 `"user:1"` 的 CRC16 哈希值为 `57832`，则：



plaintext











```plaintext
slot = 57832 % 16384 = 8680
```



因此，键 `"user:1"` 属于槽 `8680`。如果槽 `8680` 由节点 B 负责，则该键的数据会被存储在节点 B 上。

### **3. 哈希标签（Hash Tag）**

Redis 支持通过 **哈希标签** 强制多个键分配到同一个槽，用于实现事务或批量操作的原子性。

#### **规则**

如果键中包含 `{}` 括号，则只对括号内的字符串计算哈希值。例如：



- 键 `"{user}:1"` 和 `"{user}:2"` 的哈希值只取决于 `user`，因此它们会被分配到同一个槽。
- 键 `"user:{1}"` 和 `"user:{2}"` 的哈希值不同，会分配到不同槽。

### **4. 集群节点通信**

- **槽分配信息**：每个节点维护一份哈希槽到节点的映射表。

- 请求路由

  ：当客户端请求一个键时，节点会：

    1. 计算键对应的槽。
    2. 如果该槽由当前节点负责，直接处理请求。
    3. 如果由其他节点负责，返回 `MOVED` 错误，指引客户端重定向到正确节点。

### **5. 动态扩容与缩容**

- 重新分片

  ：当集群添加或移除节点时，需要重新分配哈希槽。例如：

    1. 从节点 B 和 C 各迁移 1000 个槽到新节点 D。
    2. 迁移过程中，部分槽可能处于 “迁移中” 状态，节点会返回 `ASK` 错误指导客户端获取数据。

- **数据迁移**：Redis 使用 **增量式迁移**，保证服务不停机。


### **6. 为什么是 16384 个槽？**

- **消息开销**：节点间通过 Gossip 协议交换槽信息，16384 个槽需要约 2KB 空间（每个槽 1 位）。
- **平衡性**：实验证明，16384 个槽在大多数集群规模下能提供良好的平衡性。
- **性能**：CRC16 计算快，取模运算效率高。

### **总结**

哈希槽算法是 Redis Cluster 的核心，通过固定数量的槽和 CRC16 哈希函数，实现了数据的均匀分布和动态扩展。理解该算法有助于优化集群部署、处理数据倾斜问题，以及设计高效的键命名策略


## Redis 的并发竞争问题

### **一、问题产生的核心原因**

Redis 本身是单线程执行命令的（核心命令处理是单线程），单个命令的执行是原子的。但实际业务中，操作往往是**多步复合操作**（如 “读取值 → 业务计算 → 写回新值”），这个过程并非原子操作。多个客户端的复合操作可能交叉执行，导致数据覆盖或逻辑错误。



**示例**：
两个客户端同时操作计数器 `count`（初始值为 10），意图将其加 1：



1. 客户端 A 读取 `count=10`；
2. 客户端 B 读取 `count=10`；
3. 客户端 A 计算后写回 `11`；
4. 客户端 B 计算后写回 `11`；
   最终结果为 `11`，而非预期的 `12`，这就是典型的并发竞争。

### **二、解决 Redis 并发竞争的方案**

根据业务场景的复杂度和性能要求，可选择以下方案：

#### **1. 利用 Redis 原子命令（推荐简单场景）**

Redis 提供了大量**单命令原子操作**，可直接替代 “读 - 改 - 写” 的复合操作，从根本上避免竞争。



- **常用原子命令**：
    - 计数器：`INCR`/`DECR`（自增 / 自减）、`INCRBY`/`DECRBY`（指定步长增减）；
    - 哈希表：`HINCRBY`（哈希字段增减）、`HSETNX`（字段不存在时才设置）；
    - 集合：`SADD`（添加元素，已存在则忽略）、`SPOP`（弹出元素）；
    - 字符串：`SETNX`（键不存在时才设置）、`GETSET`（获取旧值并设置新值）。
- **示例**：
  上述计数器问题，直接用 `INCR count` 即可，无需客户端 “读 - 改 - 写”，天然避免竞争。

#### **2. 使用 Redis 事务（MULTI + EXEC + WATCH）**

Redis 事务可将多个命令打包为一个原子执行单元（中间不会插入其他客户端的命令），配合 `WATCH` 命令实现 “乐观锁”，解决并发竞争。



- **原理**：

    - `WATCH key`：监控一个或多个键，若在事务执行前这些键被其他客户端修改，事务会被打断（`EXEC` 返回 `nil`）；
    - `MULTI`：开始事务；
    - 执行一系列命令（如 `GET`/`SET`）；
    - `EXEC`：提交事务，若 `WATCH` 的键未被修改，则原子执行所有命令；否则事务失败。

- **示例（伪代码）**：

  python











  ```python
  def increment_count():
      while True:
          # 监控 count 键
          redis.watch("count")
          # 读取当前值
          current = int(redis.get("count") or 0)
          # 开始事务
          pipe = redis.pipeline()
          pipe.multi()
          # 执行修改（+1）
          pipe.set("count", current + 1)
          # 提交事务：若 count 未被其他客户端修改，则成功；否则重试
          result = pipe.execute()
          if result:  # 事务成功
              break
  ```

- **优缺点**：

    - 优点：轻量，无需额外组件；
    - 缺点：依赖 `WATCH` 机制，若并发过高，事务可能频繁失败（需重试），适合低并发场景。

#### **3. 分布式锁（适合复杂业务逻辑）**

通过分布式锁，强制同一时间只有一个客户端能执行 “读 - 改 - 写” 操作，实现 “悲观锁” 效果。



- **实现方式**：
  基于 Redis 的 `SET key value NX PX timeout` 命令（原子性设置键，仅当键不存在时成功，并设置过期时间）。

- **核心步骤**：

    1. **加锁**：`SET lock_key unique_value NX PX 5000`（`NX` 确保只有一个客户端获得锁，`PX 5000` 避免死锁，`unique_value` 用于安全释放锁）；

    2. **执行业务**：获取锁后，执行 “读 - 改 - 写” 等复合操作；

    3. 释放锁

       ：通过 Lua 脚本原子删除锁（避免误删其他客户端的锁）：

       lua











     ```lua
     if redis.call("get", KEYS[1]) == ARGV[1] then
         return redis.call("del", KEYS[1])
     else
         return 0
     end
     ```

- **示例（伪代码）**：

  python











  ```python
  def update_data():
      lock_key = "lock:data"
      unique_value = uuid.uuid4().hex  # 唯一标识，用于释放锁
      # 尝试加锁，超时时间 5 秒
      locked = redis.set(lock_key, unique_value, nx=True, px=5000)
      if not locked:
          # 未获取到锁，重试或降级
          return retry_or_fallback()
      
      try:
          # 执行业务逻辑（读-改-写）
          data = redis.get("data")
          new_data = process(data)
          redis.set("data", new_data)
      finally:
          # 释放锁（Lua 脚本确保原子性）
          lua = """
              if redis.call('get', KEYS[1]) == ARGV[1] then
                  return redis.call('del', KEYS[1])
              end
              return 0
          """
          redis.eval(lua, 1, lock_key, unique_value)
  ```

- **优缺点**：

    - 优点：适合复杂业务逻辑，兼容性强；
    - 缺点：实现较复杂（需处理锁超时、重入、误删等问题），高并发下可能有性能损耗。

#### **4. 使用 Lua 脚本**

Redis 会将整个 Lua 脚本作为一个原子操作执行（中间不会被其他命令打断），可将 “读 - 改 - 写” 的复合逻辑写入脚本，直接在 Redis 端执行，避免并发竞争。



- **原理**：
  Lua 脚本在 Redis 中是单线程执行的，脚本内的所有命令会原子执行，无需担心中间被其他客户端干扰。

- **示例（计数器）**：
  用 Lua 脚本实现 “读取当前值 +1 后写回”：

  lua











  ```lua
  -- 脚本逻辑：获取 count，加 1 后写回，返回新值
  local current = redis.call("get", KEYS[1])
  current = current and tonumber(current) or 0
  local new_val = current + 1
  redis.call("set", KEYS[1], new_val)
  return new_val
  ```

客户端调用该脚本：

python











  ```python
  redis.eval(script, 1, "count")  # 1 表示 KEYS 数量，"count" 是键名
  ```

- **优缺点**：

    - 优点：灵活性高（支持复杂逻辑）、原子性强、性能好（减少网络往返）；
    - 缺点：脚本过长可能影响 Redis 性能（单线程阻塞），需控制脚本复杂度。

### **三、方案选择建议**

1. **简单场景（如计数器、开关）**：优先用 **Redis 原子命令**（最简单高效）；
2. **中等复杂度（多命令原子执行）**：用 **Redis 事务 + WATCH**（轻量，适合低并发）；
3. **复杂业务逻辑（跨多步操作）**：用 **Lua 脚本**（灵活且原子性强）或 **分布式锁**（适合跨服务场景）；
4. **高并发场景**：避免分布式锁（性能瓶颈），优先用原子命令或 Lua 脚本。

### **总结**

Redis 并发竞争的本质是 “读 - 改 - 写” 复合操作的非原子性。解决核心是通过**原子化操作**（单命令、事务、Lua 脚本）或**锁机制**（分布式锁），确保同一时间只有一个逻辑能修改目标键，从而保证数据一致性。