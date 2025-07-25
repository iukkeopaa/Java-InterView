## 为什么可重复读下无法防止幻读

在数据库事务隔离级别中，**可重复读（Repeatable Read）** 虽然能解决**不可重复读**问题，但无法完全防止**幻读**，核心原因在于其底层实现机制的局限性与幻读的本质特征不匹配。

### 先明确概念：

- **不可重复读**：同一事务内，多次读取同一行数据时，因其他事务修改并提交了该行数据，导致前后读取结果不一致。
- **幻读**：同一事务内，多次读取**同一范围**的数据时，因其他事务**插入 / 删除**了符合该范围的新数据，导致前后读取的结果集行数不同（“幻觉” 般多出或消失了记录）。

### 为什么可重复读防不住幻读？

可重复读的核心目标是保证 “事务内多次读取同一数据的结果一致”，其典型实现依赖 **MVCC（多版本并发控制）**：
事务启动时会生成一个**一致性快照**，后续的普通查询（快照读）都会基于这个快照返回数据，不受其他事务修改的影响。这解决了 “不可重复读”（其他事务修改已有数据不会被当前事务看到）。



但幻读的本质是**新插入的数据**，而 MVCC 的快照机制无法覆盖这种情况，具体原因如下：



1. **快照读无法感知新插入的数据，但 “当前读” 会暴露幻读**
   可重复读中，普通查询（如`select * from t where id > 10`）是 “快照读”，基于事务启动时的快照返回结果，即使其他事务插入了新数据（如`id=11`），快照读也看不到，暂时不会出现幻读。

   但如果事务需要执行**当前读**（如加锁查询`select * from t where id > 10 for update`、更新`update t set ... where id > 10`），此时会读取最新的实时数据（而非快照）。若其他事务已插入符合条件的新数据（如`id=11`）并提交，当前读会读到这些新数据，导致同一事务内 “快照读” 与 “当前读” 的结果集不一致 —— 这就是幻读。

2. **MVCC 无法阻止 “新数据插入”**
   幻读的根源是 “其他事务插入了符合查询范围的新数据”。可重复读的 MVCC 机制仅能保证 “已有数据的版本一致性”，但无法限制其他事务插入新数据（新数据的版本号高于当前事务的快照版本，会被当前读感知）。

   例如：

    - 事务 A 启动，查询`id > 10`的记录（快照读，返回 0 条）；
    - 事务 B 插入`id=11`并提交；
    - 事务 A 执行`update t set name='x' where id > 10`（当前读，会匹配到`id=11`并更新）；
    - 事务 A 再次快照读`id > 10`，仍返回 0 条（快照未变），但此时若用当前读查询，会看到`id=11`已被更新 —— 结果集行数 “凭空增加”，产生幻读。

3. **可重复读的锁机制不覆盖 “范围插入”**
   部分数据库（如 InnoDB）在可重复读级别会对已有数据加行锁，但不会对 “不存在的范围” 加锁。因此，其他事务可以插入符合范围条件的新数据，而当前事务在执行当前读时会 “撞见” 这些新数据，导致幻读。

### 总结：

可重复读通过 MVCC 的快照机制保证了 “同一事务内快照读的一致性”，解决了不可重复读，但无法阻止其他事务插入新数据。当事务执行**当前读**（需获取最新数据）时，这些新数据会被感知，从而产生幻读。



要完全防止幻读，需依赖更高的隔离级别（如**序列化 Serializable**），通过加表锁或范围锁限制其他事务对该范围的插入 / 修改。