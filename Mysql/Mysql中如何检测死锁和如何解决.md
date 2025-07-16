## Mysql中如何检测死锁和如何解决


在 MySQL 中，死锁是指两个或多个事务在执行过程中因争夺锁资源而造成的循环等待现象，导致这些事务都无法继续执行。以下是关于 MySQL 死锁的检测方法、常见原因及解决方案的详细介绍：

### **一、检测死锁的方法**

#### **1. 查看错误日志（最直接）**

当发生死锁时，MySQL 会在错误日志（如`error.log`）中记录详细信息，包括死锁发生的时间、涉及的事务 SQL、锁等待关系等。



**示例日志片段**：



plaintext











```plaintext
2025-07-16 10:30:45 140584220004096 [ERROR] InnoDB: Deadlock found ...
2025-07-16 10:30:45 140584220004096 [ERROR] InnoDB: Transaction 1:
2025-07-16 10:30:45 140584220004096 [ERROR] InnoDB:   UPDATE users SET balance = balance - 100 WHERE id = 1;
2025-07-16 10:30:45 140584220004096 [ERROR] InnoDB: Transaction 2:
2025-07-16 10:30:45 140584220004096 [ERROR] InnoDB:   UPDATE users SET balance = balance + 100 WHERE id = 2;
```





![img](data:image/png;base64,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)

#### **2. 查询`information_schema.INNODB_TRX`和`INNODB_LOCKS`表**

在死锁发生后，可通过以下 SQL 查询当前活跃事务和锁信息：



sql











```sql
-- 查询当前活跃事务
SELECT * FROM information_schema.INNODB_TRX;

-- 查询当前锁等待情况
SELECT * FROM information_schema.INNODB_LOCKS;

-- 查询锁等待的详细堆栈（需启用innodb_print_all_deadlocks参数）
SELECT * FROM information_schema.INNODB_LOCK_WAITS;
```

#### **3. 使用`SHOW ENGINE INNODB STATUS`（实时监控）**

执行此命令可获取 InnoDB 存储引擎的详细状态，包括最近一次死锁的完整堆栈信息（包含事务执行顺序、锁类型、等待关系等）。



**关键输出示例**：



plaintext











```plaintext
------------------------
LATEST DETECTED DEADLOCK
------------------------
2025-07-16 10:30:45
*** (1) TRANSACTION:
TRANSACTION 12345, ACTIVE 5 sec updating
mysql tables in use 1, locked 1
LOCK WAIT 2 lock struct(s), heap size 1136, 1 row lock(s)
...
*** (1) WAITING FOR THIS LOCK TO BE GRANTED:
RECORD LOCKS space id 123 page no 456 n bits 72 index PRIMARY of table `test`.`users` ...

*** (2) TRANSACTION:
TRANSACTION 67890, ACTIVE 3 sec updating
mysql tables in use 1, locked 1
2 lock struct(s), heap size 1136, 1 row lock(s)
...
*** (2) WAITING FOR THIS LOCK TO BE GRANTED:
RECORD LOCKS space id 123 page no 789 n bits 80 index PRIMARY of table `test`.`users` ...

*** WE ROLL BACK TRANSACTION (2)
```

### **二、死锁的常见原因**

#### **1. 事务执行顺序不一致**

两个事务以相反的顺序获取锁，例如：



- **事务 1**：`UPDATE A; UPDATE B;`
- **事务 2**：`UPDATE B; UPDATE A;`

#### **2. 锁粒度不匹配**

- 行锁与表锁混用（如一个事务使用行锁，另一个使用`LOCK TABLES`）。
- 索引失效导致行锁升级为表锁。

#### **3. 长事务持有锁时间过长**

事务执行时间过长，导致锁等待时间增加，死锁概率上升。

#### **4. 高并发下的锁竞争**

在高并发场景下，多个事务频繁争夺同一资源的锁。

### **三、解决死锁的策略**

#### **1. 优化事务设计（最根本）**

- **保持事务短小**：减少事务执行时间，降低锁持有时间。
- **统一锁获取顺序**：确保所有事务以相同顺序访问资源（如按 ID 升序更新记录）。
- **减少事务中的锁数量**：避免在一个事务中同时操作多个表或大量记录。

#### **2. 调整隔离级别**

- 降低隔离级别

  ：将

  ```
  REPEATABLE READ
  ```

  （默认）调整为

  ```
  READ COMMITTED
  ```

  ，减少间隙锁（gap lock）的使用，降低死锁概率。

  sql











  ```sql
  SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;
  ```

#### **3. 索引优化**

- **确保查询使用索引**：避免全表扫描导致行锁升级为表锁。
- **添加合适的索引**：例如，在`WHERE`条件字段上创建索引。

#### **4. 重试机制**

在应用层捕获死锁异常（如 MySQL 错误码`1213`），并实现重试逻辑：



java











```java
// Java示例：捕获死锁异常并重试
int maxRetries = 3;
for (int i = 0; i < maxRetries; i++) {
    try {
        // 执行事务操作
        executeTransaction();
        break;
    } catch (SQLException e) {
        if (isDeadlockException(e)) { // 判断是否为死锁异常
            Thread.sleep(100 * (i + 1)); // 指数退避
            continue;
        }
        throw e;
    }
}
```

#### **5. 调整 MySQL 参数**

- 缩短锁等待超时时间

  ：通过

  ```
  innodb_lock_wait_timeout
  ```

  参数（默认 50 秒）减少事务等待锁的时间。

  sql











  ```sql
  SET GLOBAL innodb_lock_wait_timeout = 10; -- 超时时间设为10秒
  ```

- 启用死锁检测日志

  ：通过

  ```
  innodb_print_all_deadlocks
  ```

  参数记录所有死锁信息（调试时使用）。

  sql











  ```sql
  SET GLOBAL innodb_print_all_deadlocks = 1;
  ```

#### **6. 使用意向锁和间隙锁**

理解并合理使用意向锁（Intention Lock）和间隙锁（Gap Lock），避免锁范围过大：



- 在`READ COMMITTED`隔离级别下，间隙锁仅在`INSERT`时使用，减少死锁可能性。

### **四、预防死锁的最佳实践**

1. **监控死锁频率**：定期分析错误日志，统计死锁发生的频率和场景。
2. **压测验证**：在高并发场景下进行压力测试，提前发现潜在的死锁风险。
3. **使用`SHOW ENGINE INNODB STATUS`分析**：发生死锁后，立即查看该命令输出，定位问题。
4. **优先使用`REPEATABLE READ`+ 索引**：在保证数据一致性的前提下，通过索引减少锁冲突。

### **五、总结**

死锁是数据库并发控制中不可避免的问题，但通过合理的设计和优化，可以显著降低其发生概率。关键在于**事务设计优化**、**索引优化**和**锁策略调整**，同时结合监控和重试机制，确保系统在发生死锁时能快速恢复。