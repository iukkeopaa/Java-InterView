### **һ����λ����ѯ**

#### 1. ��������ѯ��־

ͨ������ MySQL ������¼ִ��ʱ�䳬����ֵ�� SQL ��䣺

sql











```sql
-- ��ʱ��������ѯ��־
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 1;  -- ����1��Ĳ�ѯ��¼
SET GLOBAL log_queries_not_using_indexes = 'ON';  -- ��¼δʹ�������Ĳ�ѯ

-- �鿴����ѯ��־·��
SHOW VARIABLES LIKE 'slow_query_log_file';
```

#### 2. ʹ�� `pt-query-digest` ������־

ͨ�� Percona ���߼���������ѯ��־������ͳ�Ʊ��棺

bash











```bash
pt-query-digest /var/log/mysql/slow.log > slow_report.txt
```

### **������������ѯ**

#### 1. ʹ�� `EXPLAIN` �鿴ִ�мƻ�

ͨ�� `EXPLAIN` ���� SQL ��ִ��·��������ʹ�������

sql











```sql
EXPLAIN 
SELECT * FROM orders 
WHERE customer_id = 123 AND order_date > '2023-01-01';
```

�ص��ע��

- **type**���������ͣ�`ALL` ��ʾȫ��ɨ�裬`index` ��ʾ����ɨ�裬`range` ��ʾ��Χɨ�裩��
- **key**��ʵ��ʹ�õ�������
- **rows**������ɨ���������
- **Extra**��������Ϣ���� `Using filesort`��`Using temporary` ��ʾ������ģ���

#### 2. �������

ȷ�ϲ�ѯ�����������ֶ��Ƿ��к��ʵ�������

sql











```sql
-- �鿴�������
SHOW INDEX FROM orders;

-- �������ʹ�����
SHOW STATUS LIKE 'Handler_read%';
```

#### 3. �鿴��ѯͳ����Ϣ

ͨ�� `SHOW PROFILE` �鿴 SQL ִ�и��׶εĺ�ʱ��

sql











```sql
SET profiling = 1;  -- ������ѯ���ܷ���
SELECT * FROM orders WHERE customer_id = 123;
SHOW PROFILES;  -- �鿴��ѯID
SHOW PROFILE FOR QUERY 1;  -- �鿴ָ����ѯ����ϸ��ʱ
```

### **�����Ż�����ѯ**

#### 1. �Ż�����

- ��Ӹ�������

  �����Ƶ����ѯ���ֶ���ϴ���������

  sql











  ```sql
  CREATE INDEX idx_customer_date ON orders (customer_id, order_date);
  ```

- ��������

  ��ȷ����������������Ҫ��ѯ���ֶΣ�����ر�

  sql











  ```sql
  -- ��ѯ�ֶ�ȫ��������������
  SELECT customer_id, order_date FROM orders WHERE customer_id = 123;
  ```

#### 2. �Ż���ѯ���

- **����ȫ��ɨ��**��ȷ�� `WHERE` ����������֧�֡�

- �����Ӳ�ѯ

  ����



  ```
  JOIN
  ```



����Ӳ�ѯ��

sql











  ```sql
  -- �Ż�ǰ
  SELECT * FROM orders WHERE customer_id IN (SELECT id FROM customers WHERE country = 'US');
  
  -- �Ż���
  SELECT o.* FROM orders o JOIN customers c ON o.customer_id = c.id WHERE c.country = 'US';
  ```

- ��ҳ�Ż�

  ����ƫ������ҳ��



  ```
  JOIN
  ```



���



  ```
  LIMIT OFFSET
  ```

��

sql











  ```sql
  -- �Ż�ǰ
  SELECT * FROM orders ORDER BY id LIMIT 100000, 10;
  
  -- �Ż���
  SELECT o.* FROM orders o JOIN (SELECT id FROM orders ORDER BY id LIMIT 100000, 10) t USING (id);
  ```

#### 3. �Ż���ṹ

- **��ִ��**�����������ֶβ�ֵ��������С�
- **��ֱ�ֱ�**�����ֶη���Ƶ�ʲ�ֱ�
- **ˮƽ�ֱ�**����ҵ�������ʱ�䡢ID ��Χ����ֱ�

#### 4. ���� MySQL ����

- **���ӻ�����**������ `innodb_buffer_pool_size` �Ի���������ݺ�������
- **�Ż�����**������ `sort_buffer_size` ���ٴ�������
- **������ѯ����**������ `query_cache_type`�������ڶ���д�ٳ�������

### **�ġ���֤�Ż�Ч��**

#### 1. �ٴ�ִ�� `EXPLAIN`

ȷ���Ż����ִ�мƻ��Ƿ���ţ���ɨ���������١����� `Using filesort`����

#### 2. �Ա�ִ��ʱ��

sql











```sql
-- ʹ��MySQL���ú�������ִ��ʱ��
SET @start = NOW();
SELECT * FROM orders WHERE customer_id = 123;
SELECT TIMESTAMPDIFF(MICROSECOND, @start, NOW()) AS execution_time;
```

#### 3. ��ط�����ָ��

ʹ�� `SHOW STATUS`��`SHOW ENGINE INNODB STATUS` �������ط�����״̬��ȷ���Ż�δ���������⡣

### **�塢Ԥ������ѯ**

1. ���ڷ�����

   ������ͳ����Ϣ�������Ż������ɸ���ִ�мƻ���

   sql











   ```sql
   ANALYZE TABLE orders;
   ```

2. �����ؽ�����

   ���޸�������Ƭ��

   sql











   ```sql
   OPTIMIZE TABLE orders;
   ```

3. **Ӧ�ò��Ż�**������Ƶ����ѯ�����ݣ��������ݿ���ʡ�

### **�ܽ�**

�Ż�����ѯ�ĺ���˼·��

1. **��λ**��ͨ������ѯ��־�ҵ����� SQL��
2. **����**���� `EXPLAIN` �� `SHOW PROFILE` ����ִ��·���ͺ�ʱ��
3. **�Ż�**����Ӻ�����������д��ѯ������������
4. **��֤**���Ա��Ż�ǰ�������ָ�ꡣ

ͨ��ϵͳ���ķ������������������ݿ�Ĳ�ѯ���ܣ�����������ѯ���µ�ϵͳƿ����