### drop��delete �� truncate ������

- DROP ������ɾ����?��ɾ�����ű�������ṹ���Ҳ��ܻع���
- DELETE ?��?��ɾ�������Դ� WHERE ���������Իع���
- TRUNCATE ?����ձ��е��������ݣ����ᱣ����ṹ�����ܻع���


### UNION �� UNION ALL ������

UNION ��?��ȥ���ϲ��������е��ظ�?��UNION ALL ����ȥ�أ��Ὣ���н�����ϲ�������


### SQL ��ѯ����ִ��˳��

![img.png](img.png)

### LIMIT Ϊʲô�����ִ?

### 1. **�߼�������ϵ**

LIMIT ��������**�������շ��ص�����**����һ����������������ݵ�ɸѡ�����顢���������ǰ�ô������С���������ڽ׶�ִ�� LIMIT�����ܻᵼ�£�



- **����ľۺϽ��**�����磬`GROUP BY` ��Ҫ�������з����������в�����ȷ���飬����ǰ LIMIT ���ܶ�ʧ�ؼ����ݡ�
- **�����������**��`ORDER BY` ���ȫ��������������ȷ�� ��ǰ N �С�������ǰ�ضϿ��ܵ��������߼�ʧЧ��

### 2. **ִ��Ч�ʿ���**

MySQL �Ĳ�ѯ�Ż����ᾡ����ִ��·����**�����������**������ͨ���������� WHERE ���������� LIMIT �����޷������ڽ׶η������ã�



- **ʾ��**������ `WHERE price > 100 LIMIT 10`�����ݿ���Ҫ��ͨ��������ȫ��ɨ���ҵ����з����������У��ٴ��н�ȡǰ 10 �С������д��ڴ����߼���Ʒ��LIMIT ֻ����ɸѡ����Ч��

### 3. **�������Ӿ�ĳ�ͻ**

��� LIMIT �������Ӿ�֮ǰִ�У����ܵ����߼���ͻ��



- **�� DISTINCT ��ͻ**������ǰ LIMIT�����ܷ����ظ�ֵ�����磬ǰ 10 ���д����ظ����ݣ���ʵ��ȥ�غ��� 10 �У���
- **�� HAVING ��ͻ**��HAVING ���ڷ����Ľ��ɸѡ������ǰ LIMIT��������©�ؼ����顣

### 4. **��ҳ�߼���ʵ��**

LIMIT ���� OFFSET ���ʵ�ַ�ҳ������ `LIMIT 10 OFFSET 20`��ȡ�� 21~30 �У�����������£����ݿ���룺



- ���������ɸѡ�����顢���������
- ������ OFFSET �к�ȡ LIMIT �С�
  ����ǰ LIMIT���޷���ȷ��λ��ҳλ�á�

### 5. **�Ż��������⴦��**

��Ȼ LIMIT ���߼������ִ�У����Ż�������ͨ��**�����Ż�**��**��ǰ��ֹɨ��**������Ч�ʣ�



- **�����Ż�**���� ORDER BY �� LIMIT �����ʹ�����������ݿ����ֱ��ͨ��������ȡǰ N �У�����ȫ��ɨ�衣
- **��ǰ��ֹ**������ `WHERE` �����ܿ��ٹ��˴������ݵĳ��������ݿ������ɨ�赽�㹻��������ǰ��ֹ�����ٺ�����������

### ʾ��˵��

������ SQL��



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



ִ������Ϊ��



1. **FROM/WHERE**��ɸѡ `price > 100` ����Ʒ��
2. **GROUP BY**���� `category_id` ���顣
3. **HAVING**�����˳�������������� 5 �����
4. **ORDER BY**����������������
5. **LIMIT**��ȡ������ǰ 3 �С�



�� LIMIT ��ǰִ�У�������δ��ɷ��������ʱ�ͽض����ݣ����½������

### ORDER BY Ϊʲô�� SELECT ֮��ִ?

### 1. **�߼�������ϵ**

- **`SELECT` ��������**��`SELECT` �Ӿ为��ȷ������������У��������ʽ�������;ۺϺ������� `SUM()`��`COUNT()`����ֻ���� `SELECT` ִ�к󣬽�����Ľṹ�����ݲű���ȫȷ����

- `ORDER BY` ���������

  ��

  ```
  ORDER BY
  ```



��Ҫ����



  ```
  SELECT
  ```



������н����������磺

sql











  ```sql
  SELECT column1 + column2 AS sum_result
  FROM table
  ORDER BY sum_result; -- ������ͨ�� SELECT ���� sum_result
  ```

���



  ```
  ORDER BY
  ```



��



  ```
  SELECT
  ```



ǰִ�У����ݿ��޷�֪��



  ```
  sum_result
  ```



��ʲô��

### 2. **������������**

- `SELECT` �еı����� `ORDER BY` �ɼ�

  ��SQL ��׼����



  ```
  ORDER BY
  ```



ʹ��



  ```
  SELECT
  ```



�ж���ı���������������



  ```
  WHERE
  ```



��



  ```
  GROUP BY
  ```



��ʹ�ã���Ϊ��Щ�Ӿ���



  ```
  SELECT
  ```



ǰִ�У������磺

sql











  ```sql
  SELECT CONCAT(first_name, ' ', last_name) AS full_name
  FROM users
  ORDER BY full_name; -- �Ϸ������� SELECT �ı���
  ```

��



  ```
  ORDER BY
  ```



��



  ```
  SELECT
  ```



ǰִ�У�����



  ```
  full_name
  ```



��δ�������ᵼ�´���

### 3. **�ۺϺ����Ĵ���**

- �ۺϺ����� `SELECT` �м���

  ��

  ```
  GROUP BY
  ```



֮��

  ```
  SELECT
  ```



��Ӧ�þۺϺ�������



  ```
  SUM()
  ```

��

  ```
  AVG()
  ```

������



  ```
  ORDER BY
  ```



����������Щ�����������磺

sql











  ```sql
  SELECT category, COUNT(*) AS product_count
  FROM products
  GROUP BY category
  ORDER BY product_count DESC; -- ���ۺϽ������
  ```

  ```
  COUNT(*)
  ```



�ļ��㷢����



  ```
  SELECT
  ```



�׶Σ�

  ```
  ORDER BY
  ```



���������ִ�в��ܻ�ȡ��ȷ�ľۺ�ֵ��

### 4. **�Ż�����ִ�в���**

- �߼�˳�� vs ����˳��

  ����Ȼ



  ```
  ORDER BY
  ```



���߼��Ϻ�ִ�У����Ż�������ͨ����������ʱ���Ż��������磺

sql











  ```sql
  SELECT id, name
  FROM users
  ORDER BY created_at;
  ```

���



  ```
  created_at
  ```



���������Ż�������ֱ�Ӱ�����˳��ɨ�����ݣ�����������򣬵������������Ż������ı��߼�ִ��˳��

### 5. **�� `DISTINCT` �� `LIMIT` ��Эͬ**

- `DISTINCT` �� `SELECT` ��Ӧ��

  ������ѯʹ��



  ```
  DISTINCT
  ```

��ȥ�ز���������



  ```
  SELECT
  ```



�׶Σ�

  ```
  ORDER BY
  ```



���ȥ�غ�Ľ���������磺

sql











  ```sql
  SELECT DISTINCT category
  FROM products
  ORDER BY category;
  ```

��ͨ��



  ```
  SELECT DISTINCT
  ```



��ȡΨһ��



  ```
  category
  ```

��������

- **`LIMIT` �� `ORDER BY` ��Ӧ��**��`ORDER BY` ȷ������˳���`LIMIT` ������ȷ��ȡǰ N �С��� `ORDER BY` ��ǰִ�У����ܵ��·�ҳ�߼�����

### ʾ��˵��

�������²�ѯ��



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


ִ������Ϊ��



1. **FROM/WHERE**��ɸѡ����ɶ�����
2. **GROUP BY**�����û����顣
3. **HAVING**�����˶�������5 ���û���
4. **SELECT**������ÿ���û��Ķ�������`order_count`����
5. **ORDER BY**���� `order_count` ��������
6. **LIMIT**��ȡǰ 10 ���û���



�� `ORDER BY` �� `SELECT` ǰִ�У����ݿ��޷���ȡ `order_count` ��ֵ����������ʧ�ܡ�


### MySQL �� 3-10 ����¼��ô��


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


### ˵˵ SQL ����ʽ��������ת��

�� SQL �У���ʽ��������ת����ָ���ݿ�ϵͳ**�Զ�**��һ����������ת��Ϊ��һ���������͵Ĺ��̣������û���ʽʹ�ú������� `CAST()` �� `CONVERT()`��������ת��ͨ�������ڱ��ʽ���㡢�Ƚϲ�����������ʱ�������ܵ������������������⡣

### **������ʽת������**

#### 1. **�Ƚϲ����е�ת��**

���Ƚϲ�ͬ���͵�ֵʱ�����ݿ�᳢�Խ�����ת��Ϊͬһ���ͣ�



sql











```sql
-- �ַ��������ֱȽ�
SELECT * FROM users WHERE age = '25';  -- �ַ��� '25' ��תΪ���� 25

-- �������ַ����Ƚ�
SELECT * FROM orders WHERE order_date = '2023-01-01';  -- �ַ���תΪ����
```

#### 2. **���������е�ת��**

��������Ĳ������ᱻת��Ϊ�������ͣ�



sql











```sql
SELECT '100' + 5;  -- �ַ��� '100' תΪ���� 100�����Ϊ 105

SELECT 10 / 3;     -- ����������������ܱ�תΪ��������ȡ�������ݿ⣩
```

#### 3. **����������ת��**

�������Զ�ת���������ͣ�



sql











```sql
SELECT CONCAT('Hello', 123);  -- ���� 123 תΪ�ַ��� '123'�����Ϊ 'Hello123'

SELECT LENGTH(12345);         -- ����תΪ�ַ��������㳤��Ϊ 5
```

#### 4. **�������ʽ�е�ת��**

`CASE`��`IF` �ȱ��ʽҪ��������һ�£�



sql











```sql
SELECT 
    CASE WHEN condition THEN 1 ELSE 'A' END;  -- ���� 1 ����תΪ�ַ��� '1'
```

#### 5. **JOIN �����е�ת��**

���������漰��ͬ����ʱ��



sql











```sql
SELECT * 
FROM users u 
JOIN orders o ON u.id = o.user_id;  -- �� u.id �� INT��o.user_id �� VARCHAR������ܷ���ת��
```

### **��ʽת���ķ���**

#### 1. **������**

sql











```sql
SELECT * FROM products WHERE price = '10.5abc';  -- �ַ������ض�Ϊ 10.5������ƥ��ɹ�
```

#### 2. **����ʧЧ**

�������б���ʽת������ѯ�����޷�����������



sql











```sql
-- �� user_id �� INT ���ͣ����²�ѯ�ᵼ��ȫ��ɨ��
SELECT * FROM orders WHERE user_id = '123';  -- �����޷������ں��� `CAST(user_id AS VARCHAR)`
```

#### 3. **�����½�**

����ת�����ܴ���ȫ��ɨ��������㣺



sql











```sql
SELECT * FROM logs WHERE log_time > '2023-01-01';  -- �ַ���תΪ���ڿ������м���
```

#### 4. **���ݽض�**

ת��Ϊ��С��������ʱ���ܶ�ʧ���ȣ�



sql











```sql
SELECT CAST(12345.67 AS INT);  -- ���Ϊ 12345��С�����ֱ��ض�
```

### **��ͬ���ݿ��ת���������**

��ͬ���ݿ�ϵͳ����ʽת��������ܲ�ͬ��



- **MySQL**�������ڽ��ַ���תΪ���ֽ��бȽϣ��� `'25' = 25` Ϊ�棩��
- **SQL Server**���ϸ�Ҫ������ƥ�䣬�ַ��������ֱȽϿ��ܱ���
- **PostgreSQL**��ͨ����Ҫ��ʽת������ʽת�����١�

### **��α�����ʽת����**

1. **��ʽ����ת��**
   ʹ�� `CAST()` �� `CONVERT()` ������ȷת�����ͣ�

   sql











   ```sql
   SELECT * FROM users WHERE age = CAST('25' AS INT);
   ```

2. **��������һ��**
   ȷ���Ƚϲ������������������ͬ��

   sql











   ```sql
   -- �� user_id �� INT����ѯ����Ҳ�� INT
   SELECT * FROM orders WHERE user_id = 123;  -- ���� '123'
   ```

3. **ʹ�ò�������ѯ**
   ��Ӧ�ô�����ʹ��Ԥ������䣨Prepared Statement���������ֶ�ƴ�� SQL��

   python



����









   ```python
   # Python + MySQL ʾ��
   cursor.execute("SELECT * FROM users WHERE age = %s", (25,))
   ```





4. **�������������**
   ȷ�����������ѯ��������������һ�£���������ʧЧ��

### **�ܽ�**

��ʽ����ת���� SQL �г���������������ԣ������ܵ������������������⡣���飺



- **����ʹ����ʽת��**����ߴ���ɶ��ԺͰ�ȫ�ԡ�
- **�������������Ͻ�����ʽת��**������Ӱ���ѯ���ܡ�
- **�˽����ݿ��ת������**����ͬϵͳ����Ϊ���ܴ��ڲ��졣
### InnoDB �� MyISAM ��Ҫ��ʲô����

InnoDB �� MyISAM ����?������������?�ֺ������ơ�InnoDB ?������?�������ʺ�?����ҵ��ϵͳ��?
MyISAM ��?������?���Ǳ�������ѯ�쵫д?���ܲ�ʺ϶���д�ٵĳ�����

�Ӵ洢�ṹ����˵��MyISAM ?���ָ�ʽ��?�����洢��.frm ?���洢��Ķ��壻.MYD �洢���ݣ�.MYI ��
��������? InnoDB ?���ָ�ʽ��?�����洢��.frm ?���洢��Ķ��壻.ibd �洢���ݺ�������


### InnoDB �� Buffer Pool

![img_1.png](img_1.png)

## MySql��ѯ�����ִ�С����������ô����ν��������


**�������һ��**

�޸����ݿ��ַ�����ƥ�����(��MySQL 8.0.1�汾��ʼ��֧��_cs��case sensitive collation����������ڴ˰汾֮ǰ��MySQL��֧��_bin��binary collation����������������ִ�Сд��)

*_bin: ��ʾ����binary case sensitive collation��Ҳ����˵�����ִ�Сд��
*_cs: case sensitive collation�����ִ�Сд
*_ci: case insensitive collation�������ִ�Сд�ַ�ƥ�����

**���������:**

ʹ�ùؼ���BINARY����

mysql��ѯĬ���ǲ����ִ�Сд�� ��:

```
select * from ���� where �ֶ��� ='aaa';
```



��

```
select * from ���� where �ֶ��� ='Aaa';
```



��ѯ�����ͬ

�����ʽ(ʹ�ùؼ���BINARY ����)

```
select * from ���� where BINARY �ֶ��� ='aaa';
```



ԭ��:

����CHAR��VARCHAR��TEXT���ͣ�BINARY���Կ���Ϊ�з�������ַ����� У�Թ���BINARY������ָ�����ַ����Ķ�Ԫ У�Թ���ļ�д������ͱȽϻ�����ֵ�ַ�ֵ�����Ҳ����Ȼ�����˴�Сд��

**����취��:**

ʹ��varbinary��������

���ݿ���varchar�ַ��������ǲ����ִ�Сд��ѯ��,��varchar��Ϊvarbinary����,�ٴβ�ѯ�ͻ�ʹ��ѯ���ֶ����ֲ�ѯ������Сд

## mysql������һ�����У������кܶ��ظ����ݣ������һ��sql���ظ�������һ�����������

### **���� 1��ʹ��`DELETE` + `JOIN`����������Ψһ��ʶ�е������**

�����ṹΪ `users(id, name, email)`������ `email` �д����ظ�ֵ���豣�� `id` ��С���У�



sql











```sql
DELETE u1
FROM users u1
JOIN users u2 
  ON u1.email = u2.email 
  AND u1.id > u2.id;
```



**����**��



- ͨ�� `JOIN` �������ҵ����� `email` ��ͬ�� `id` ����ļ�¼��������С `id` ���ظ��У���
- ɾ����Щ�ظ��У�����ÿ������ `id` ��С���С�

### **���� 2��ʹ��`DELETE` + `WHERE`�Ӳ�ѯ����������Ψһ��ʶ�е������**

������û��Ψһ��ʶ�У��� `id`������ͨ������ֶ��ж��ظ���



sql











```sql
DELETE FROM users
WHERE (name, email) IN (
    SELECT name, email
    FROM users
    GROUP BY name, email
    HAVING COUNT(*) > 1
)
AND ROWID NOT IN (  -- ROWID �� MySQL ��ʽ���б�ʶ����������������
    SELECT MIN(ROWID)
    FROM users
    GROUP BY name, email
);
```



**ע��**��



- �������������� `ROWID` �滻Ϊʵ�������У��� `id`����
- �������������� MySQL �汾������Ҫ�������ʱ������

### **���� 3��������ʱ�������ڸ��ӳ������豣�����ݱ��ݣ�**

sql











```sql
-- ������ʱ�������벻�ظ�������
CREATE TABLE temp_users AS
SELECT DISTINCT * 
FROM users;

-- ɾ��ԭ����������ʱ��
DROP TABLE users;
ALTER TABLE temp_users RENAME TO users;
```



**�ŵ�**����ֱ�ӣ����⸴�ӵ� `DELETE` �߼���
**ȱ��**������ʱ�洢�ռ䣬����Ӱ�����������ԣ����������������

### **���� 4��ʹ�ô��ں�����MySQL 8.0+��**

sql











```sql
WITH ranked AS (
    SELECT *,
           ROW_NUMBER() OVER (PARTITION BY email ORDER BY id) AS rn
    FROM users
)
DELETE FROM users
WHERE id IN (SELECT id FROM ranked WHERE rn > 1);
```



**����**��



- ͨ�� `ROW_NUMBER()` Ϊÿ�� `email` �����ڵļ�¼�����кţ��� `id` ���򣩡�
- ɾ���кŴ��� 1 �ļ�¼�����ظ��У���

### **ע������**

1. **��������**��ִ��ɾ��ǰ��ر��ݱ����ݣ������������

2. Ψһ����

   ��ɾ���ظ����ݺ󣬿����Ψһ������ֹ�ٴγ����ظ���

   sql











   ```sql
   ALTER TABLE users ADD UNIQUE (email);
   ```





![img](data:image/png;base64,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)

3. **���ܿ���**�����ɾ���������ܺ�ʱ�ϳ��������ڵͷ���ִ�С�

### **ʾ����ṹ������**

sql











```sql
CREATE TABLE users (
    id INT PRIMARY KEY,
    name VARCHAR(50),
    email VARCHAR(50)
);

INSERT INTO users VALUES
(1, 'Alice', 'alice@example.com'),
(2, 'Bob', 'bob@example.com'),
(3, 'Charlie', 'alice@example.com');  -- �ظ� email

-- ɾ���ظ��������Ӧ���� id=1 �� id=2 �ļ�¼
```








����ʵ�ʱ�ṹ������ѡ����ʵķ������Ƽ�����ʹ�÷��� 1���򵥸�Ч���򷽷� 4������ǿ�󣩡�


## Mysql�е�likeģ����ѯһ������������

#### 1. **ǰ׺ƥ�䣨`'xxx%'`��������������**

��� `LIKE` ��ƥ��ģʽ��**�Թ̶��ַ���ͷ��ͨ�����ĩβ**���� `col LIKE 'abc%'`������ʱ **B-tree �������ܱ�ʹ��**��
ԭ���ǣ�B-tree �����ǰ��ֶ�ֵ��ǰ׺�������еģ�`'abc%'` �������������������ԣ����ٶ�λ���� `'abc'` ��ͷ�ļ�¼��Χ������ȫ��ɨ�衣

**ʾ��**��
�����б� `user`���ֶ� `name` �ϴ�������ͨ B-tree ������

sql











```sql
CREATE INDEX idx_name ON user(name);
```

��ѯ��

sql











```sql
SELECT * FROM user WHERE name LIKE 'zhang%'; -- ����ʹ�� idx_name ����
```

MySQL �Ż������жϣ�`'zhang%'` ����ͨ���������ٶ�λ�������� `'zhang'` ��ͷ�ļ�¼����˿���ѡ����������

#### 2. **��׺ƥ�䣨`'%xxx'`�����м�ƥ�䣨`'%xxx%'`����ͨ����������**

��� `LIKE` ��ƥ��ģʽ��**ͨ����ڿ�ͷ**���� `col LIKE '%abc'`����**ͨ������м�**���� `col LIKE 'a%c'`������ʱ **B-tree �����޷�����Ч���ã�ͨ������ȫ��ɨ��**��

ԭ���ǣ�B-tree �������������ǻ����ֶ�ֵ������ǰ׺���� `'%abc'` �� `'%abc%'` ��Ҫƥ�� �������ַ� + �̶��ַ����������޷�ͨ�������Զ�λ�����巶Χ��ֻ������ɨ���жϡ�

**ʾ��**��

sql











```sql
SELECT * FROM user WHERE name LIKE '%zhang'; -- ͨ����ڿ�ͷ��ͨ����������
SELECT * FROM user WHERE name LIKE '%zhang%'; -- ͨ������м䣬ͨ����������
```

#### 3. **���ⳡ���������������ܱ�����**

��ʹ�� `'%xxx%'` ������ģʽ�������ѯ���ֶ�**ȫ��������ĳ��������**���� ����������������MySQL ���ܻ�ѡ��ɨ�������������ȫ������Ϊ���������ͨ����ȫ��С��ɨ����졣

**ʾ��**��
�� `user` ������ `idx_name_age (name, age)`����ѯ��

sql











```sql
SELECT name, age FROM user WHERE name LIKE '%zhang%'; -- ����ɨ�� idx_name_age ����
```

��Ȼ `'%zhang%'` �޷�ͨ�����������Զ�λ�������ڲ�ѯ�� `name` �� `age` ���������У��Ż�������ѡ��ɨ����������ȫ��ɨ���Ч����

#### 4. **�Ż��������վ���**

��ʹ����ǰ׺ƥ��������MySQL �Ż���Ҳ����**����ʹ������**��ת��ѡ��ȫ��ɨ�衣��ͨ�������ڣ�

- ����������С��ȫ��ɨ����������Ҹ��죩��
- ƥ��ļ�¼ռ�ȼ��ߣ��� `name LIKE 'a%'` ƥ���˱��� 80% �ļ�¼��������λ�ĳɱ����ܸ���ȫ��ɨ�裩��

### �ܽ�

`LIKE` ģ����ѯ�Ƿ��������ĺ����жϱ�׼�ǣ�**ƥ��ģʽ�ܷ����������������Կ��ٶ�λ��Χ**��

- `'ǰ׺%'`��������������B-tree ����֧��ǰ׺ƥ�䣩��
- `'%��׺'` �� `'%�м�%'`��ͨ�������������޷��������������ԣ���
- ���ⳡ�����縲������������ɨ����������ȫ��

## ʲô�ǻر���ν���ر�

### һ��ʲô�ǻر�

InnoDB ��������Ϊ�������ͣ�

- **�۴�������Clustered Index��**��������Ϊ����������Ҷ�ӽڵ�ֱ�Ӵ洢**������������**�������м�¼�������ֶΣ���һ�ű�ֻ��һ���۴�������ͨ��Ĭ����������������δ�������������ʽ���ɣ���
- **����������Secondary Index���Ǿ۴�������**���Է������ֶ�Ϊ������������ͨ���������������ȣ�����Ҷ�ӽڵ�**ֻ�洢������ֵ + ����ֵ**�����洢���������ݣ���

��ʹ��**��������**��ѯ����ʱ������ѯ���ֶ�**��ֻ�Ƕ�����������������ֶ�**������Ҫ��ȡ����������δ�洢�������ֶΣ�������Ҫ��������ѯ��

1. ��ͨ�����������ҵ�ƥ���¼��**����ֵ**��
2. ��������ֵȥ**�۴�����**�в�ѯ�����������ݣ���ȡ����������ֶΣ���

��� ��ͨ�����������ҵ���������ȥ�۴�������ѯ�������ݡ� �Ĺ��̣��ͳ�Ϊ**�ر�**��

### �����ر������

�ر�ı�����**��һ��������ѯ**���Ӷ����������۴��������������Ӵ��� IO ���������������ϴ���ѯƵ���ĳ����£��ر���������Ͳ�ѯ���ܣ�

- ���磬һ��ǧ�����ݵı���һ����ѯ��Ҫ�ر����ܻ���ⴥ���ɰ���ǧ�ξ۴������Ĳ��ң�IO �ɱ����������

### ������ν���ر�

����ر�ĺ���˼·�ǣ�**����Ӷ����������۴������Ķ��β�ѯ**�����ò�ѯ����������ֶζ���ֱ�ӴӶ��������л�ȡ������ر���õķ�����**��������**��

#### 1. ����������Covering Index��

**��������**��ָ����ѯ�����**�����ֶ�**������ `WHERE` �����еĹ����ֶκ� `SELECT` ��ķ����ֶΣ���������ĳ�����������С���ʱ��ͨ���ö�����������ֱ�ӻ�ȡ������Ҫ�����ݣ�����ر�

**ʾ��˵��**��
������һ���û��� `user`���ṹ���£�

sql











```sql
CREATE TABLE user (
  id INT PRIMARY KEY, -- �۴�����������������
  name VARCHAR(50),
  age INT,
  email VARCHAR(100),
  INDEX idx_name (name) -- ����������ֻ���� name + id��
);
```

- **���� 1����Ҫ�ر�**
  ����ѯ��

  sql











  ```sql
  SELECT id, name, age FROM user WHERE name = '����';
  ```

������
�������� `idx_name` ֻ���� `name` �� `id`������ѯ��Ҫ `age` �ֶΣ����� `idx_name` �У�����ˣ���ѯ����ͨ�� `idx_name` �ҵ� `name='����'` ��Ӧ������ `id`������ `id` ȥ�۴������в�ѯ `age`������**�ر�**��

- **���� 2��ʹ�ø�������������ر�**
  ����������������������ѯ����������ֶΣ�

  sql











  ```sql
  CREATE INDEX idx_name_age (name, age); -- �������������� name + age + id����Ϊ��������Ĭ��Я��������
  ```

��ִ��ͬ���Ĳ�ѯ��

sql











  ```sql
  SELECT id, name, age FROM user WHERE name = '����';
  ```

������
�������� `idx_name_age` ���� `name`��`age` ������ `id`����������Ĭ�ϴ洢����������ѯ����� `id`��`name`��`age` ����ֱ�ӴӸ������л�ȡ��**����ر�**��ֱ�ӷ��ؽ����

#### 2. ��������˼·

- **����ʹ�þ۴�������ѯ**������ѯ����ֱ�ӻ����������۴���������������ر���Ϊ�۴���������洢�������ݣ������� `SELECT * FROM user WHERE id = 100`��ֱ��ͨ���۴�������ȡ���ݣ��޻ر�
- **���Ʒ����ֶ�**������ʹ�� `SELECT *`����ѯ�����ֶΣ���ֻ��ѯ��Ҫ���ֶΡ�����ѯ���ֶν��٣�������ͨ��������������ر�

### �ܽ�

- **�ر�**��ʹ�ö���������ѯʱ���������ֶβ��ڶ��������У���ͨ���������۴�������ѯ�������ݵĹ��̣������� IO �ɱ���
- **�������**��������ʹ��**��������**���ò�ѯ�������ֶζ������ڶ��������У���������β�ѯ�۴�������
- ע�⣺��������Խ��Խ�ã��������������������д�루INSERT/UPDATE/DELETE��ʱ������ά���ɱ��������ʵ�ʲ�ѯ������ơ�

## �۴�����=����������

�� InnoDB �����У�ÿ�ű�����һ��������������۴���������Ҳ����֮Ϊ�ۼ����������������洢�����ݵġ�һ������£��۴�������ͬ��������������������һ��ǰ���������Ǿ������ű���Ҫ��������ֻ�������������������������������������������ܵ��ھ۴�������

���Կ����������Ӧ������һ�������۴�����������ȫ����������������Ϊһ�ű�ӽṹ������������û�������������������û������������������ô�۴������Ͳ��������������ˡ�


## Mysql����ID�������ô��

- ��������ID����

  ���������� ID �ﵽ���޺���������һ������ʱ������ ID ����䣨��������ֵ����ֻ�Ǵ�ʱ���������ʱ����Ϊ����Լ����ԭ��ID �ǲ������ظ��ģ����Ծͻᱨ����ʾ������ͻ��
- row_id����

�����û������������InnoDB ���Զ�����һ��ȫ�����ص� row_id���䳤��Ϊ 6 ���ֽڣ��� row_id �ﵽ���޺�����ִ�����̺����� ID ��ͬ�������ٴι��㣬Ȼ�����µ��������������ͬ�� row_id����������ݻḲ��֮ǰ������



## �����ǧ�򼶱��Mysql������θ�Ч���һ���µ���
### һ��ʹ�� `ALGORITHM=INPLACE` �� `LOCK=NONE`��MySQL 5.6+��

**����ԭ��**��
MySQL 5.6+ ������ Online DDL ���ܣ�ͨ�� `INPLACE` �㷨�����ؽ����������޸�Ԫ���ݡ�ʹ�� `LOCK=NONE` �����ڲ����ڼ������д��

**ʾ���﷨**��

sql











```sql
ALTER TABLE table_name
ADD COLUMN new_column INT DEFAULT 0,
ALGORITHM=INPLACE,
LOCK=NONE;
```

**��������**��

- ������ `NULL` �������� `DEFAULT` ֵ��������ȫ���������ݣ���
- ���ڷ� `NULL` ����Ĭ��ֵ���У�MySQL ����ʽ������� `0` ����ַ�����

**ע������**��

- ������� `FULLTEXT` ���������ܽ���Ϊ `COPY` �㷨�����ؽ�����
- ִ��ǰ��ȷ�Ͽ��ô��̿ռ���㣨ԼΪ���С�� 20%����

### ����ʹ�� `pt-online-schema-change`��Percona Toolkit��

**����ԭ��**��
ͨ������Ӱ�ӱ�Shadow Table������ԭ���ϴ���������ͬ�����ݣ�����л��������������̶�Ӧ��͸���������л�ʱ��������

**��������**��

1. **��װ����**��

   bash











   ```bash
   yum install percona-toolkit  # CentOS/RHEL
   apt-get install percona-toolkit  # Ubuntu/Debian
   ```

2. **ִ������**��

   bash











   ```bash
   pt-online-schema-change \
   --alter "ADD COLUMN new_column INT DEFAULT 0" \
   D=db_name,t=table_name \
   --user=username \
   --password=password \
   --execute
   ```

**�ŵ�**��

- ��ȫ���߲�������ҵ��Ӱ�켫С��
- �Զ�����������������֧�ִ�������

**ȱ��**��

- ��Ҫ����Ĵ��̿ռ䣨ԼΪԭ���С����
- ͬ���ڼ���������Ӹ����ӳ١�

### ���������ε������ݣ������ڸ��ӳ�����

**����ԭ��**��
�����������Χ��֣��������������±�����滻ԭ��

**��������**��

1. **������ʱ��**��

   sql











   ```sql
   CREATE TABLE new_table LIKE old_table;
   ALTER TABLE new_table ADD COLUMN new_column INT DEFAULT 0;
   ```

2. **�����θ�������**��

   sql











   ```sql
   -- ʾ����ÿ�θ���10����
   INSERT INTO new_table 
   SELECT * FROM old_table 
   WHERE id BETWEEN start_id AND end_id;
   ```

3. **�л�����**��

   sql











   ```sql
   RENAME TABLE old_table TO old_table_backup, new_table TO old_table;
   ```

**�Ż�����**��

- ʹ���������ÿ�����ݣ�ȷ��ԭ���ԡ�
- �����ڼ������ݿ⸺�أ�����Ӱ��ҵ��

### �ġ�Ԥ�����У���ƽ׶��Ż���

**���ʵ��**��
�ڱ���ƽ׶�Ԥ����չ�У��� `extra_data JSON`�����������Ƶ���޸ı�ṹ��

sql











```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY,
  name VARCHAR(100),
  extra_data JSON  -- Ԥ���ֶΣ��洢��̬��չ����
);
```

### �塢�Ա���ѡ����

| ����                      | ���ó���               | �ŵ�               | ȱ��                       |
| ------------------------- | ---------------------- | ------------------ | -------------------------- |
| `ALGORITHM=INPLACE`       | ������ӣ���Ĭ��ֵ�� | ���٣��������ռ� | ���ܶ�������               |
| `pt-online-schema-change` | ���ӱ��������ȫ����   | ��ҵ���ж�         | ��Ҫ˫���ռ䣬���Ӹ����ӳ� |
| �����ε���                | �������������辫ϸ���� | ������ͣ�ָ�   | �������ӣ���ʱ��           |
| Ԥ������                  | ��ƽ׶ι滮           | ��������޸�       | ��ҪԤ��ҵ��仯           |

### ����ע������

1. **��������**�����۲������ַ�����ִ��ǰ��ر���ȫ�����ݡ�
2. **ѡ��ͷ��ڲ���**����ʹ�� Online DDL���Կ��ܶ����ܲ�����΢Ӱ�졣
3. **�����Դ**�������ڼ��ش��̿ռ䡢CPU���ڴ�͸����ӳ١�
4. **��֤����**���������������Ƿ��������ر������Լ����

# ================================
### һ���������Ӹ��Ƽܹ����ӿ����б�������л���ɫ

**����ԭ��**��
�������Ӹ��Ƶ� ����д���롱 ���ԣ����ڴӿ⣨��ҵ�����⣩ִ�б�ṹ�����������䣬���ӿ�����������һ���ұ����ɺ��л����ӽ�ɫ�����ӿ�����Ϊ���⣩������ֱ��������������µ���������ܲ�����

#### �������裺

1. **ȷ������ͬ��״̬**
   ȷ���ӿ���׷������� binlog���������ݲ�һ�£�

   sql











   ```sql
   -- �ڴӿ�ִ�У��鿴Seconds_Behind_Master�Ƿ�Ϊ0
   SHOW SLAVE STATUS\G
   ```

2. **�ڴӿ�ֹͣͬ����ִ�б�ṹ���**
   ����ͣ�ӿ�ͬ�����������������ݸ��ű������������У�

   sql











   ```sql
   STOP SLAVE;  -- ֹͣ�ӿ�ͬ��
   ALTER TABLE table_name ADD COLUMN new_column [����] [Ĭ��ֵ];  -- �ӿ��������
   ```

3. **����������ݣ����������������� JOIN��**
   �����е�ֵ�������������`user_info`�����`order`���ȡ�û��ȼ�������`JOIN`�������£�

   sql











   ```sql
   -- ʾ�����ӹ�����user_infoͬ��level��order���new_column
   UPDATE order o
   JOIN user_info u ON o.user_id = u.id
   SET o.new_column = u.level
   WHERE o.new_column IS NULL;  -- �����ظ�����
   ```

���ɷ�����ִ�У��簴`order.id`��Χ��֣�ÿ�θ��� 10 ���У���������ʱ�䣩

4. **�ӿ�����ͬ����׷ƽ��������**
   �����ɺ������ӿ�ͬ�����ôӿ�׷�������ڱ���ڼ�����������ݣ�

   sql











   ```sql
   START SLAVE;
   -- �ٴ�ȷ��Seconds_Behind_MasterΪ0
   SHOW SLAVE STATUS\G
   ```

5. **�л����ӽ�ɫ**
   ͨ��`VIP`�������л�����ҵ�������е�����ɱ���Ĵӿ⣨�����⣩��ԭ���⽵��Ϊ�ӿ⡣

    - ����`MGR`��MySQL Group Replication������ֱ��ͨ��`switchover`ƽ���л���
    - ��ͳ���ӿ�ͨ��`pt-table-sync`ȷ�����һ���ԣ����޸�Ӧ�����ӵ�ַ��

#### ���ó�����

- �������Ӽܹ����Ҵӿ�ɳе���ʱ������Ǻ���ҵ����⣩��
- ������Ҫ�������������ݣ���`JOIN`��䣩��ֱ����������`UPDATE JOIN`�ᵼ�´���������ܱ�����

#### �ŵ㣺

- ����ȫ���ޱ��������ҵ���д����Ӱ�죻
- �ӿ�����`JOIN`���¿��ڵͷ���ִ�У����տɿء�

#### ȱ�㣺

- �����Ӽܹ�֧�֣��л����̿������뼶�жϣ������л����ߣ���
- �������ڱ���ڼ�д��������ݣ��ӿ�׷ͬ�����ܺ�ʱ�ϳ���

### �������½��� + JOIN Ǩ�� + �����л��� ��Ϸ���

**����ԭ��**��
��ֱ��`ALTER`�����չ���ʱ�����ڴӿ��½�Ŀ��������У���ͨ��`JOIN`ԭ��͹���������Ǩ�����ݣ���ͨ�������л��滻ԭ��

#### �������裺

1. **�ӿ��½�Ŀ��������У�**

   sql











   ```sql
   -- �ӿ�ִ�У�����ԭ��ṹ���������
   CREATE TABLE new_table LIKE old_table;
   ALTER TABLE new_table ADD COLUMN new_column [����];
   ```

2. **ͨ�� JOIN ����Ǩ�����ݵ��±�**
   ��`INSERT ... SELECT ... JOIN`��ԭ��͹�����Ǩ�����ݣ�������ִ�У�

   sql











   ```sql
   -- ʾ����ÿ��Ǩ��id��[start, end]��Χ�����ݣ�����user_info���ȡnew_columnֵ
   INSERT INTO new_table (col1, col2, ..., new_column)
   SELECT o.col1, o.col2, ..., u.level  -- �ӹ�����ȡnew_column��ֵ
   FROM old_table o
   JOIN user_info u ON o.user_id = u.id
   WHERE o.id BETWEEN 1 AND 100000;  -- �����Σ�ÿ��10����
   ```

3. **ͬ����������**
   Ǩ����ʷ���ݺ�ͨ���������� binlog �������ߣ��� Canal��ͬ���ӿ���Ǩ���ڼ�������������ݵ�`new_table`��

4. **��֤����һ����**
   �Ա�`old_table`��`new_table`���������ؼ��ֶι�ϣֵ��ȷ������һ�£�

   sql











   ```sql
   -- У������
   SELECT COUNT(*) FROM old_table;
   SELECT COUNT(*) FROM new_table;
   
   -- У���������
   SELECT MD5(CONCAT(col1, col2, new_column)) FROM new_table WHERE id = 12345;
   ```

5. **�л������������ӿ�Ϊ����**
   �ӿ�����ȷ����������������л����ӽ�ɫ��

   sql











   ```sql
   -- �ӿ�ִ�У�ԭ��������������������
   RENAME TABLE old_table TO old_table_bak, new_table TO old_table;
   ```

֮�󽫸ôӿ�����Ϊ���⣬�н�ҵ��������

#### ���ó�����

- ������Ҫ���ӹ����߼������ JOIN����ֱ��`ALTER`+`UPDATE`Ч�ʼ��ͣ�
- ԭ�������Ƭ�������ֶΣ�ϣ������Ż���ṹ��������ֶ�˳��ɾ�������У���

#### �ŵ㣺

- ��ȫ������ԭ����ִ��`ALTER`�ʹ�����`UPDATE`����������Ӱ�죻
- ��˳���Ż���ṹ������������ѯ���ܡ�

#### ȱ�㣺

- ��������ϸ��ӣ����ϸ�У������һ���ԣ�
- ��������ͬ������⹤��֧�֣��ʺ��г�������ͬ����ϵ���Ŷӡ�

### �����ؼ�ע������

1. **����һ��������**��
   ���������л���������Ǩ�ƣ�����ͨ��У�飨��������ϣֵ�����������ȷ���±�������ԭ��һ�£�����ҵ���쳣��
2. **����������������**��
   ������ִ��`JOIN`��`INSERT/UPDATE`ʱ�����β���������������� 1 �� - 10 �򣨸��ݷ��������ܵ����������ⳤʱ��ռ������Դ��
3. **�ع�����**��
   ����ǰԤ���ع�ͨ�����籣��ԭ���ݡ������л�ǰ��¼ԭ������Ϣ����һ����������ɿ����лء�
4. **��������ӳ�**��
   �ӿ����ڼ���ʵʱ��������ӳ٣����ӳٹ��󣬿���ͣǨ�����������ôӿ�׷ƽ���⡣
