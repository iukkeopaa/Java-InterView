## ��������

SQL���ṹ����ѯ���ԣ������ڹ���Ͳ������ݿ�ı�׼���ԡ���Ҫ��Ϊ���¼��ࣺ



- **DDL�����ݶ������ԣ�**���������޸ġ�ɾ�����ݿ���󣨱������ȣ�
- **DML�����ݲ������ԣ�**�����롢��ѯ�����¡�ɾ������
- **DCL�����ݿ������ԣ�**�������û�Ȩ�ޣ�GRANT��REVOKE��
- **TCL������������ԣ�**����������COMMIT��ROLLBACK��

## SQL ִ��˳��

1. FROM �Ӿ䣺ȷ�������������
2. WHERE �Ӿ䣺������
3. GROUP BY �Ӿ䣺����
4. HAVING �Ӿ䣺���˷���
5. SELECT �Ӿ䣺ѡ����
6. ORDER BY �Ӿ䣺����
7. LIMIT/OFFSET�����ƽ������

## Լ������

`NOT NULL` - ָʾĳ�в��ܴ洢 NULL ֵ��

`UNIQUE` - ��֤ĳ�е�ÿ�б�����Ψһ��ֵ��

`PRIMARY KEY` - NOT NULL �� UNIQUE �Ľ�ϡ�ȷ��ĳ�У��������ж���еĽ�ϣ���Ψһ��ʶ�������ڸ����׸����ٵ��ҵ����е�һ���ض��ļ�¼��

`FOREIGN KEY` - ��֤һ�����е�����ƥ����һ�����е�ֵ�Ĳ��������ԡ�

`CHECK` - ��֤���е�ֵ����ָ����������

`DEFAULT` - �涨û�и��и�ֵʱ��Ĭ��ֵ��

------


```sql
CREATE TABLE Users (
  Id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '����Id',
  Username VARCHAR(64) NOT NULL UNIQUE DEFAULT 'default' COMMENT '�û���',
  Password VARCHAR(64) NOT NULL DEFAULT 'default' COMMENT '����',
  Email VARCHAR(64) NOT NULL DEFAULT 'default' COMMENT '�����ַ',
  Enabled TINYINT(4) DEFAULT NULL COMMENT '�Ƿ���Ч',
  PRIMARY KEY (Id)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='�û���';
```
## ������

���ܻ��� SELECT ��䣬���� SELECT ���Ҳû���壻Ҳ���ܻ��� CREATE �� DROP ���

MySQL Ĭ������ʽ�ύ��ÿִ��һ�����Ͱ�������䵱��һ������Ȼ������ύ�������� START TRANSACTION ���ʱ����ر���ʽ�ύ���� COMMIT �� ROLLBACK ���ִ�к�������Զ��رգ����»ָ���ʽ�ύ��

ͨ�� set autocommit=0 ����ȡ���Զ��ύ��ֱ�� set autocommit=1 �Ż��ύ��autocommit ��������ÿ�����Ӷ�������Է������ġ�


```sql
-- ��ʼ����
START TRANSACTION;

-- ������� A
INSERT INTO `user`
VALUES (1, 'root1', 'root1', 'xxxx@163.com');

-- ���������� updateA
SAVEPOINT updateA;

-- ������� B
INSERT INTO `user`
VALUES (2, 'root2', 'root2', 'xxxx@163.com');

-- �ع��������� updateA
ROLLBACK TO updateA;

-- �ύ����ֻ�в��� A ��Ч
COMMIT;
```
## ������

```sql
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    age INT CHECK (age >= 18),
    email VARCHAR(100) UNIQUE
);
```

![img.png](img.png)

## ��������

```sql
INSERT INTO users (name, age, email)
VALUES ('Alice', 25, 'alice@example.com'),
       ('Bob', 30, 'bob@example.com');
```

![img_1.png](img_1.png)


## ��������

```sql
UPDATE users SET age = age + 1 WHERE name = 'Alice';
```

![img_2.png](img_2.png)


## ɾ������

```sql
DELETE FROM users WHERE age <= 26;
```

![img_3.png](img_3.png)


## ��ѯ

```sql
-- ����ǰ 5 ��
SELECT * FROM mytable LIMIT 5;
SELECT * FROM mytable LIMIT 0, 5;
-- ���ص� 3 ~ 5 ��
SELECT * FROM mytable LIMIT 2, 3;
```


## ������

���������ݿ���һ������Ĵ洢���̣��������ض������ݿ�������� INSERT��UPDATE �� DELETE������ʱ�Զ�ִ�С�������ͨ������ʵ������������Լ������¼�����־���Զ�ִ����ز�����

```sql
CREATE TRIGGER trigger_name
BEFORE/AFTER INSERT/UPDATE/DELETE ON table_name
FOR EACH ROW
BEGIN
    -- �������߼�
END;
```
```sql
-- ����һ�����������ڲ����¼�¼ʱ�Զ����� created_at �ֶ�Ϊ��ǰʱ��
CREATE TRIGGER set_created_at
BEFORE INSERT ON mytable
FOR EACH ROW
BEGIN
    SET NEW.created_at = NOW();
END;
```

