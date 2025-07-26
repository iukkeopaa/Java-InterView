### **一、定位慢查询**

#### 1. 开启慢查询日志

通过配置 MySQL 参数记录执行时间超过阈值的 SQL 语句：

sql











```sql
-- 临时开启慢查询日志
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 1;  -- 超过1秒的查询记录
SET GLOBAL log_queries_not_using_indexes = 'ON';  -- 记录未使用索引的查询

-- 查看慢查询日志路径
SHOW VARIABLES LIKE 'slow_query_log_file';
```

#### 2. 使用 `pt-query-digest` 分析日志

通过 Percona 工具集分析慢查询日志，生成统计报告：

bash











```bash
pt-query-digest /var/log/mysql/slow.log > slow_report.txt
```

### **二、分析慢查询**

#### 1. 使用 `EXPLAIN` 查看执行计划

通过 `EXPLAIN` 分析 SQL 的执行路径、索引使用情况：

sql











```sql
EXPLAIN 
SELECT * FROM orders 
WHERE customer_id = 123 AND order_date > '2023-01-01';
```

重点关注：

- **type**：访问类型（`ALL` 表示全表扫描，`index` 表示索引扫描，`range` 表示范围扫描）。
- **key**：实际使用的索引。
- **rows**：估算扫描的行数。
- **Extra**：额外信息（如 `Using filesort`、`Using temporary` 表示性能损耗）。

#### 2. 检查索引

确认查询条件和连接字段是否有合适的索引：

sql











```sql
-- 查看表的索引
SHOW INDEX FROM orders;

-- 检查索引使用情况
SHOW STATUS LIKE 'Handler_read%';
```

#### 3. 查看查询统计信息

通过 `SHOW PROFILE` 查看 SQL 执行各阶段的耗时：

sql











```sql
SET profiling = 1;  -- 开启查询性能分析
SELECT * FROM orders WHERE customer_id = 123;
SHOW PROFILES;  -- 查看查询ID
SHOW PROFILE FOR QUERY 1;  -- 查看指定查询的详细耗时
```

### **三、优化慢查询**

#### 1. 优化索引

- 添加复合索引

  ：针对频繁查询的字段组合创建索引。

  sql











  ```sql
  CREATE INDEX idx_customer_date ON orders (customer_id, order_date);
  ```

- 索引覆盖

  ：确保索引包含所有需要查询的字段，避免回表。

  sql











  ```sql
  -- 查询字段全部包含在索引中
  SELECT customer_id, order_date FROM orders WHERE customer_id = 123;
  ```

#### 2. 优化查询语句

- **避免全表扫描**：确保 `WHERE` 条件有索引支持。

- 减少子查询

  ：用



  ```
  JOIN
  ```



替代子查询。

sql











  ```sql
  -- 优化前
  SELECT * FROM orders WHERE customer_id IN (SELECT id FROM customers WHERE country = 'US');
  
  -- 优化后
  SELECT o.* FROM orders o JOIN customers c ON o.customer_id = c.id WHERE c.country = 'US';
  ```

- 分页优化

  ：大偏移量分页用



  ```
  JOIN
  ```



替代



  ```
  LIMIT OFFSET
  ```

。

sql











  ```sql
  -- 优化前
  SELECT * FROM orders ORDER BY id LIMIT 100000, 10;
  
  -- 优化后
  SELECT o.* FROM orders o JOIN (SELECT id FROM orders ORDER BY id LIMIT 100000, 10) t USING (id);
  ```

#### 3. 优化表结构

- **拆分大表**：将不常用字段拆分到独立表中。
- **垂直分表**：按字段访问频率拆分表。
- **水平分表**：按业务规则（如时间、ID 范围）拆分表。

#### 4. 调整 MySQL 参数

- **增加缓冲区**：调整 `innodb_buffer_pool_size` 以缓存更多数据和索引。
- **优化排序**：增大 `sort_buffer_size` 减少磁盘排序。
- **调整查询缓存**：启用 `query_cache_type`（适用于读多写少场景）。

### **四、验证优化效果**

#### 1. 再次执行 `EXPLAIN`

确认优化后的执行计划是否更优（如扫描行数减少、避免 `Using filesort`）。

#### 2. 对比执行时间

sql











```sql
-- 使用MySQL内置函数测量执行时间
SET @start = NOW();
SELECT * FROM orders WHERE customer_id = 123;
SELECT TIMESTAMPDIFF(MICROSECOND, @start, NOW()) AS execution_time;
```

#### 3. 监控服务器指标

使用 `SHOW STATUS`、`SHOW ENGINE INNODB STATUS` 等命令监控服务器状态，确保优化未引发新问题。

### **五、预防慢查询**

1. 定期分析表

   ：更新统计信息，帮助优化器生成更优执行计划。

   sql











   ```sql
   ANALYZE TABLE orders;
   ```

2. 定期重建索引

   ：修复索引碎片。

   sql











   ```sql
   OPTIMIZE TABLE orders;
   ```

3. **应用层优化**：缓存频繁查询的数据，减少数据库访问。

### **总结**

优化慢查询的核心思路：

1. **定位**：通过慢查询日志找到问题 SQL。
2. **分析**：用 `EXPLAIN` 和 `SHOW PROFILE` 分析执行路径和耗时。
3. **优化**：添加合适索引、改写查询、调整参数。
4. **验证**：对比优化前后的性能指标。

通过系统化的方法，可以逐步提升数据库的查询性能，避免因慢查询导致的系统瓶颈。