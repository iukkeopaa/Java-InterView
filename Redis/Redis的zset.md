## 对于二维的数据 zset怎么进行排序  (既有积分又有时间)

### **方案 1：组合分数（Score）法**

**核心思路**：将多个维度的值组合为一个分数，确保分数的排序效果符合多维排序需求。

#### **场景示例**

假设用户数据需按以下规则排序：

1. **积分（points）**：降序排列（积分高的在前）。
2. **时间（timestamp）**：积分相同时，按时间升序（时间早的在前）。

#### **实现步骤**

1. **设计组合分数公式**：

   plaintext











   ```plaintext
   分数 = (MAX_POINTS - 当前积分) * 时间单位 + 时间戳
   ```

- `MAX_POINTS`：预估的最大积分值（如 10000）。
- `时间单位`：足够大的数，确保积分差异对总分的影响远大于时间差异（如 `1000000000`）。
- `时间戳`：通常用毫秒时间戳（如 `System.currentTimeMillis()`）。

2. **计算示例**：

    - 用户 A：积分 100，时间戳 1690000000000（2023-07-21）。
      分数 = `(10000 - 100) * 1000000000 + 1690000000000 = 9900169000000000`。
    - 用户 B：积分 100，时间戳 1690100000000（2023-07-22）。
      分数 = `(10000 - 100) * 1000000000 + 1690100000000 = 9900169010000000`。
    - 用户 C：积分 99，时间戳 1690000000000（2023-07-21）。
      分数 = `(10000 - 99) * 1000000000 + 1690000000000 = 9901169000000000`。
      → 排序结果：A（9900169000000000）→ B（9900169010000000）→ C（9901169000000000）。

3. **Redis 操作**：

   python



运行









   ```python
   import redis
   import time
   
   r = redis.Redis()
   
   # 添加用户（积分100，时间戳1690000000000）
   max_points = 10000
   time_unit = 1000000000
   points = 100
   timestamp = 1690000000000  # 实际使用时用 int(time.time() * 1000)
   
   score = (max_points - points) * time_unit + timestamp
   r.zadd("user_rank", {user_id: score})
   ```

4. **查询排序结果**：

   python



运行









   ```python
   # 获取排名前10的用户（按积分降序，积分相同按时间升序）
   top_users = r.zrange("user_rank", 0, 9, withscores=True)
   ```

### **方案 2：双 ZSet 法**

**核心思路**：维护两个 ZSet，分别按积分和时间排序，查询时合并结果。

#### **实现步骤**

1. **维护两个 ZSet**：

    - `points_zset`：分数为积分（降序需用负数）。
    - `time_zset`：分数为时间戳（升序）。

   python



运行









   ```python
   # 添加用户
   points = 100
   timestamp = int(time.time() * 1000)
   
   r.zadd("points_zset", {user_id: -points})  # 负号实现降序
   r.zadd("time_zset", {user_id: timestamp})
   ```

2. **查询逻辑**：

   python



运行









   ```python
   # 1. 获取积分前100的用户（缩小范围）
   top_points_users = r.zrange("points_zset", 0, 99)
   
   # 2. 获取这些用户的时间排序
   time_scores = r.zmget("time_zset", top_points_users)
   
   # 3. 本地合并排序（Python示例）
   user_time_pairs = list(zip(top_points_users, time_scores))
   user_time_pairs.sort(key=lambda x: x[1])  # 按时间升序
   
   # 4. 取最终结果
   final_result = [user for user, _ in user_time_pairs[:10]]
   ```

### **方案 3：Hash + Sorted Set 法**

**核心思路**：用 Hash 存储完整数据，ZSet 按积分排序，查询后再过滤时间。

#### **实现步骤**

1. **数据结构**：

    - `user_hash`：Hash 存储用户完整信息（积分、时间等）。
    - `points_zset`：ZSet 按积分排序（分数为积分，降序用负数）。

   python



运行









   ```python
   # 添加用户
   user_data = {
       "points": 100,
       "timestamp": int(time.time() * 1000)
   }
   
   r.hset("user_hash", user_id, json.dumps(user_data))
   r.zadd("points_zset", {user_id: -user_data["points"]})
   ```

2. **查询逻辑**：

   python



运行









   ```python
   # 1. 获取积分前100的用户
   top_users = r.zrange("points_zset", 0, 99)
   
   # 2. 获取完整数据并过滤时间
   user_datas = [json.loads(r.hget("user_hash", user)) for user in top_users]
   user_datas.sort(key=lambda x: x["timestamp"])  # 按时间升序
   
   # 3. 取最终结果
   final_result = [data["user_id"] for data in user_datas[:10]]
   ```






### **方案对比与选择建议**

| **方案**         | **优点**                          | **缺点**                       | **适用场景**               |
| ---------------- | --------------------------------- | ------------------------------ | -------------------------- |
| **组合分数法**   | 单 ZSet，查询高效，Redis 原生支持 | 需预估最大积分值，分数计算复杂 | 积分和时间范围可预估的场景 |
| **双 ZSet 法**   | 灵活，无需预估值                  | 需要本地合并排序，复杂度高     | 数据量大，需精确控制排序   |
| **Hash+ZSet 法** | 数据结构清晰，便于维护            | 查询需多次操作，性能略低       | 需要完整数据的场景         |

**建议**：优先使用**组合分数法**，简单高效，适合大多数场景；若积分范围无法预估或排序规则复杂，可考虑**双 ZSet 法**。