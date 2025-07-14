### drop、delete 与 truncate 的区别

- DROP 是物理删除，?来删除整张表，包括表结构，且不能回滚。
- DELETE ?持?级删除，可以带 WHERE 条件，可以回滚。
- TRUNCATE ?于清空表中的所有数据，但会保留表结构，不能回滚。


### UNION 与 UNION ALL 的区别

UNION 会?动去除合并后结果集中的重复?。UNION ALL 不会去重，会将所有结果集合并起来。


### SQL 查询语句的执行顺序

![img.png](img.png)

### LIMIT 为什么在最后执?

### 1. **逻辑依赖关系**

LIMIT 的作用是**限制最终返回的行数**，这一操作必须在完成数据的筛选、分组、排序等所有前置处理后进行。如果在早期阶段执行 LIMIT，可能会导致：



- **错误的聚合结果**：例如，`GROUP BY` 需要处理所有符合条件的行才能正确分组，若提前 LIMIT 可能丢失关键数据。
- **错误的排序结果**：`ORDER BY` 需对全量数据排序后才能确定 “前 N 行”，若提前截断可能导致排序逻辑失效。

### 2. **执行效率考量**

MySQL 的查询优化器会尽量在执行路径中**尽早过滤数据**（例如通过索引加速 WHERE 条件），但 LIMIT 本身无法在早期阶段发挥作用：



- **示例**：对于 `WHERE price > 100 LIMIT 10`，数据库需要先通过索引或全表扫描找到所有符合条件的行，再从中截取前 10 行。若表中存在大量高价商品，LIMIT 只能在筛选后生效。

### 3. **与其他子句的冲突**

如果 LIMIT 在其他子句之前执行，可能导致逻辑冲突：



- **与 DISTINCT 冲突**：若提前 LIMIT，可能返回重复值（例如，前 10 行中存在重复数据，但实际去重后不足 10 行）。
- **与 HAVING 冲突**：HAVING 基于分组后的结果筛选，若提前 LIMIT，可能遗漏关键分组。

### 4. **分页逻辑的实现**

LIMIT 常与 OFFSET 结合实现分页，例如 `LIMIT 10 OFFSET 20`（取第 21~30 行）。这种情况下，数据库必须：



- 先完成所有筛选、分组、排序操作。
- 再跳过 OFFSET 行后取 LIMIT 行。
  若提前 LIMIT，无法正确定位分页位置。

### 5. **优化器的特殊处理**

虽然 LIMIT 在逻辑上最后执行，但优化器可能通过**索引优化**或**提前终止扫描**来提升效率：



- **索引优化**：若 ORDER BY 与 LIMIT 结合且使用索引，数据库可能直接通过索引获取前 N 行，避免全表扫描。
- **提前终止**：对于 `WHERE` 条件能快速过滤大量数据的场景，数据库可能在扫描到足够行数后提前终止，减少后续处理开销。

### 示例说明

假设有 SQL：



sql











```sql
SELECT category_id, COUNT(*) 
FROM products 
WHERE price > 100 
GROUP BY category_id 
HAVING COUNT(*) > 5 
ORDER BY COUNT(*) DESC 
LIMIT 3;
```



执行流程为：



1. **FROM/WHERE**：筛选 `price > 100` 的商品。
2. **GROUP BY**：按 `category_id` 分组。
3. **HAVING**：过滤出分组后数量超过 5 的类别。
4. **ORDER BY**：按数量降序排序。
5. **LIMIT**：取排序后的前 3 行。



若 LIMIT 提前执行，可能在未完成分组或排序时就截断数据，导致结果错误。

### ORDER BY 为什么在 SELECT 之后执?

### 1. **逻辑依赖关系**

- **`SELECT` 定义结果集**：`SELECT` 子句负责确定最终输出的列，包括表达式、别名和聚合函数（如 `SUM()`、`COUNT()`）。只有在 `SELECT` 执行后，结果集的结构和内容才被完全确定。

- `ORDER BY` 依赖结果列

  ：

  ```
  ORDER BY
  ```



需要根据



  ```
  SELECT
  ```



输出的列进行排序。例如：

sql











  ```sql
  SELECT column1 + column2 AS sum_result
  FROM table
  ORDER BY sum_result; -- 必须先通过 SELECT 定义 sum_result
  ```

如果



  ```
  ORDER BY
  ```



在



  ```
  SELECT
  ```



前执行，数据库无法知道



  ```
  sum_result
  ```



是什么。

### 2. **别名的作用域**

- `SELECT` 中的别名在 `ORDER BY` 可见

  ：SQL 标准允许



  ```
  ORDER BY
  ```



使用



  ```
  SELECT
  ```



中定义的别名，但不允许在



  ```
  WHERE
  ```



或



  ```
  GROUP BY
  ```



中使用（因为这些子句在



  ```
  SELECT
  ```



前执行）。例如：

sql











  ```sql
  SELECT CONCAT(first_name, ' ', last_name) AS full_name
  FROM users
  ORDER BY full_name; -- 合法，依赖 SELECT 的别名
  ```

若



  ```
  ORDER BY
  ```



在



  ```
  SELECT
  ```



前执行，别名



  ```
  full_name
  ```



尚未创建，会导致错误。

### 3. **聚合函数的处理**

- 聚合函数在 `SELECT` 中计算

  ：

  ```
  GROUP BY
  ```



之后，

  ```
  SELECT
  ```



会应用聚合函数（如



  ```
  SUM()
  ```

、

  ```
  AVG()
  ```

），而



  ```
  ORDER BY
  ```



可能依赖这些计算结果。例如：

sql











  ```sql
  SELECT category, COUNT(*) AS product_count
  FROM products
  GROUP BY category
  ORDER BY product_count DESC; -- 按聚合结果排序
  ```

  ```
  COUNT(*)
  ```



的计算发生在



  ```
  SELECT
  ```



阶段，

  ```
  ORDER BY
  ```



必须在其后执行才能获取正确的聚合值。

### 4. **优化器的执行策略**

- 逻辑顺序 vs 物理顺序

  ：虽然



  ```
  ORDER BY
  ```



在逻辑上后执行，但优化器可能通过索引或临时表优化排序。例如：

sql











  ```sql
  SELECT id, name
  FROM users
  ORDER BY created_at;
  ```

如果



  ```
  created_at
  ```



有索引，优化器可能直接按索引顺序扫描数据，避免额外排序，但这属于物理优化，不改变逻辑执行顺序。

### 5. **与 `DISTINCT` 和 `LIMIT` 的协同**

- `DISTINCT` 在 `SELECT` 后应用

  ：若查询使用



  ```
  DISTINCT
  ```

，去重操作发生在



  ```
  SELECT
  ```



阶段，

  ```
  ORDER BY
  ```



需对去重后的结果排序。例如：

sql











  ```sql
  SELECT DISTINCT category
  FROM products
  ORDER BY category;
  ```

先通过



  ```
  SELECT DISTINCT
  ```



获取唯一的



  ```
  category
  ```

，再排序。

- **`LIMIT` 在 `ORDER BY` 后应用**：`ORDER BY` 确定排序顺序后，`LIMIT` 才能正确截取前 N 行。若 `ORDER BY` 提前执行，可能导致分页逻辑错误。

### 示例说明

考虑以下查询：



sql











```sql
SELECT 
    user_id, 
    COUNT(order_id) AS order_count
FROM orders
WHERE status = 'completed'
GROUP BY user_id
HAVING order_count >= 5
ORDER BY order_count DESC
LIMIT 10;
```


执行流程为：



1. **FROM/WHERE**：筛选已完成订单。
2. **GROUP BY**：按用户分组。
3. **HAVING**：过滤订单数≥5 的用户。
4. **SELECT**：计算每个用户的订单数（`order_count`）。
5. **ORDER BY**：按 `order_count` 降序排序。
6. **LIMIT**：取前 10 名用户。



若 `ORDER BY` 在 `SELECT` 前执行，数据库无法获取 `order_count` 的值，导致排序失败。


### MySQL 第 3-10 条记录怎么查


```sql
SELECT * 
FROM your_table
ORDER BY some_column
LIMIT 8 OFFSET 2;
```


```mysql
SELECT * 
FROM your_table
ORDER BY some_column
LIMIT 2, 8;
```


### 说说 SQL 的隐式数据类型转换

在 SQL 中，隐式数据类型转换是指数据库系统**自动**将一种数据类型转换为另一种数据类型的过程，无需用户显式使用函数（如 `CAST()` 或 `CONVERT()`）。这种转换通常发生在表达式计算、比较操作或函数调用时，但可能导致意外结果或性能问题。

### **常见隐式转换场景**

#### 1. **比较操作中的转换**

当比较不同类型的值时，数据库会尝试将它们转换为同一类型：



sql











```sql
-- 字符串与数字比较
SELECT * FROM users WHERE age = '25';  -- 字符串 '25' 被转为数字 25

-- 日期与字符串比较
SELECT * FROM orders WHERE order_date = '2023-01-01';  -- 字符串转为日期
```

#### 2. **算术运算中的转换**

参与运算的操作数会被转换为兼容类型：



sql











```sql
SELECT '100' + 5;  -- 字符串 '100' 转为数字 100，结果为 105

SELECT 10 / 3;     -- 整数除法，结果可能被转为浮点数（取决于数据库）
```

#### 3. **函数参数的转换**

函数会自动转换参数类型：



sql











```sql
SELECT CONCAT('Hello', 123);  -- 数字 123 转为字符串 '123'，结果为 'Hello123'

SELECT LENGTH(12345);         -- 数字转为字符串，计算长度为 5
```

#### 4. **条件表达式中的转换**

`CASE`、`IF` 等表达式要求结果类型一致：



sql











```sql
SELECT 
    CASE WHEN condition THEN 1 ELSE 'A' END;  -- 数字 1 可能转为字符串 '1'
```

#### 5. **JOIN 条件中的转换**

连接条件涉及不同类型时：



sql











```sql
SELECT * 
FROM users u 
JOIN orders o ON u.id = o.user_id;  -- 若 u.id 是 INT，o.user_id 是 VARCHAR，则可能发生转换
```

### **隐式转换的风险**

#### 1. **意外结果**

sql











```sql
SELECT * FROM products WHERE price = '10.5abc';  -- 字符串被截断为 10.5，导致匹配成功
```

#### 2. **索引失效**

若索引列被隐式转换，查询可能无法利用索引：



sql











```sql
-- 若 user_id 是 INT 类型，以下查询会导致全表扫描
SELECT * FROM orders WHERE user_id = '123';  -- 索引无法作用于函数 `CAST(user_id AS VARCHAR)`
```

#### 3. **性能下降**

类型转换可能触发全表扫描或额外计算：



sql











```sql
SELECT * FROM logs WHERE log_time > '2023-01-01';  -- 字符串转为日期可能逐行计算
```

#### 4. **数据截断**

转换为较小数据类型时可能丢失精度：



sql











```sql
SELECT CAST(12345.67 AS INT);  -- 结果为 12345，小数部分被截断
```

### **不同数据库的转换规则差异**

不同数据库系统的隐式转换规则可能不同：



- **MySQL**：倾向于将字符串转为数字进行比较（如 `'25' = 25` 为真）。
- **SQL Server**：严格要求类型匹配，字符串与数字比较可能报错。
- **PostgreSQL**：通常需要显式转换，隐式转换较少。

### **如何避免隐式转换？**

1. **显式类型转换**
   使用 `CAST()` 或 `CONVERT()` 函数明确转换类型：

   sql











   ```sql
   SELECT * FROM users WHERE age = CAST('25' AS INT);
   ```

2. **保持类型一致**
   确保比较操作两侧的数据类型相同：

   sql











   ```sql
   -- 若 user_id 是 INT，查询参数也用 INT
   SELECT * FROM orders WHERE user_id = 123;  -- 而非 '123'
   ```

3. **使用参数化查询**
   在应用代码中使用预编译语句（Prepared Statement），避免手动拼接 SQL：

   python



运行









   ```python
   # Python + MySQL 示例
   cursor.execute("SELECT * FROM users WHERE age = %s", (25,))
   ```





4. **检查索引列类型**
   确保索引列与查询条件的数据类型一致，避免索引失效。

### **总结**

隐式类型转换是 SQL 中常见但需谨慎的特性，它可能导致意外结果或性能问题。建议：



- **优先使用显式转换**，提高代码可读性和安全性。
- **避免在索引列上进行隐式转换**，以免影响查询性能。
- **了解数据库的转换规则**，不同系统的行为可能存在差异。
### InnoDB 和 MyISAM 主要有什么区别

InnoDB 和 MyISAM 的最?区别在于事务?持和锁机制。InnoDB ?持事务、?级锁，适合?多数业务系统；?
MyISAM 不?持事务，?的是表锁，查询快但写?性能差，适合读多写少的场景。

从存储结构上来说，MyISAM ?三种格式的?件来存储，.frm ?件存储表的定义；.MYD 存储数据；.MYI 存
储索引；? InnoDB ?两种格式的?件来存储，.frm ?件存储表的定义；.ibd 存储数据和索引。


### InnoDB 的 Buffer Pool

![img_1.png](img_1.png)