### 1. ��ȷ�ı�������

ĳЩ���ݿ������û�ֱ��ʹ����������ȡ����������� MySQL ���ṩ�������Ĺ��ܣ�

sql











```sql
-- ��MySQL��ʹ�ã��������Խ��ж�����
LOCK TABLES table_name READ;

-- ��MySQL��ʹ�ã��������Խ���д����
LOCK TABLES table_name WRITE;
```

��Ҫע����ǣ�`UNLOCK TABLES`���������ͷ�����

### 2. ȫ��ɨ�����

��ִ��ȫ��ɨ��ʱ�����ݿ���ܻ�Ϊ�˱�֤���ݵ�һ���Զ��ӱ��������磺

sql











```sql
-- ��û�к��ʵ�����ʱ�����ܻ����ȫ��ɨ�貢����
SELECT * FROM table_name WHERE some_column = 'value';
```

### 3. DDL�����ݶ������ԣ�����

ִ�� DDL ���ʱ�����ݿ�ͨ����ӱ�������Ϊ��Щ������ı��Ľṹ�����磺

sql











```sql
ALTER TABLE table_name ADD COLUMN new_column INT;
DROP TABLE table_name;
TRUNCATE TABLE table_name;
```

### 4. �ض�������뼶���µĲ���

��ĳЩ���뼶���У��ض��Ĳ�ѯ���ܻᴥ���������� MySQL �� MyISAM �洢����Ϊ����

sql











```sql
-- ��REPEATABLE READ���뼶���£����ܻ�ӱ���
BEGIN TRANSACTION;
SELECT * FROM table_name WHERE some_column = 'value' FOR UPDATE;
COMMIT;
```

### 5. �洢�������Ե��µı���

��ͬ�Ĵ洢�����������ʵ�ֻ���������ͬ����Щ�洢����Ĭ�Ͼ�ʹ�ñ��������磺

sql











```sql
-- MyISAM�洢����Ĭ��ʹ�ñ���
CREATE TABLE myisam_table (id INT) ENGINE=MyISAM;
```

### 6. �������ݲ���

ִ���������»�ɾ������ʱ�����ܻᴥ���������ر�����û�к�������������¡����磺

sql











```sql
-- û�к�������ʱ���ܴ�������
UPDATE table_name SET column1 = 'value';
DELETE FROM table_name WHERE some_column = 'value';
```

### 7. ���ݿ����ò���Ӱ��

���ݿ�����ò���Ҳ���������Ϊ����Ӱ�졣���磺

sql











```sql
-- ����innodb_table_locks��������Ӱ�������Ϊ
SET GLOBAL innodb_table_locks = 1;
```

### �ܽ�

����һ������������г��֣�

- �ֶ�ִ�б������
- ����ȫ��ɨ�������������
- ִ�� DDL ��䡣
- �ض��洢�����Ĭ����Ϊ��
- �ض�������뼶���µĲ�����

��ʵ��Ӧ���У�Ҫ��������ʹ�ñ�������Ϊ���ή�����ݿ�Ĳ������ܡ�����ͨ����������������Ż���ѯ����Լ�ѡ����ʵĴ洢���棨�� InnoDB ֧���м������ȷ�ʽ�������������ȡ�