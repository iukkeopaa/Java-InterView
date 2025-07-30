# ���ﻯ��

Ϊ���Ż��� SQL ��ѯ����ͨ����Ӽ����������֣�

���ȣ��һ����ѯ��䱾��ȷ��ʹ���˺��ʵ�����������ȫ��ɨ�衣���磬��WHERE��JOIN��ORDER BY�Ӿ����漰�����ϴ����������������Դ��������ѯ�ٶȡ�

��Σ��һ��Ż����ݿ���ơ����磬ʹ�ñ���������������߸���ʵ����Ҫ�ڹ淶���ͷ��淶��֮������ƽ�⣬�Լ��ٸ��ӵ�JOIN������

Ȼ���һ���� MySQL �����ò��������磬���� InnoDB ����صĴ�С���ø�������ݿ��Ի������ڴ��У����ٴ��� I/O ������ͬʱ��������Ҫ������ѯ����Ĵ�С��

���⣬�һ�ʹ��һЩ���ܷ������ߣ�����EXPLAIN����������ѯ��ִ�мƻ����ҳ�����ƿ����Performance Schema Ҳ��һ���ܺõĹ��ߣ����԰����ռ���ϸ���������ݡ�

��󣬳����ļ�غ͵����Ǳز����ٵġ�ʹ��һЩ��ع��ߣ����� Percona Monitoring and Management (PMM) �� Datadog����ʵʱ������ݿ�����ܣ������������Ż�����ѯ��ȷ�����ݿ�ʼ�ձ��ָ�Ч��

# ��ϸ���

### 1. �Ż���ѯ���

#### ʹ���ʵ�������

- **��������**��ȷ����ѯʹ�����ʵ�����������Ƶ��������WHERE��JOIN��ORDER BY��GROUP BY�Ӿ��е��д���������

```
CREATE INDEX idx_column_name ON table_name(column_name);
```

- **��������**�����ڶ��в�ѯ������ʹ�ø���������������������

```
CREATE INDEX idx_columns ON table_name(column1, column2);
```

#### ����ȫ��ɨ��

- **ʹ�ú��ʵĹ�������**��ȷ��WHERE�Ӿ��е������ܹ���Ч����������������ȫ��ɨ�衣

```
SELECT*FROM table_name WHERE indexed_column ='value';
```

- **���Ʒ��ص�����**��ʹ��LIMIT�Ӿ����Ʒ��ص��������������ݿ�ĸ�����

```
SELECT*FROM table_name WHEREcondition LIMIT 10;
```

#### �Ż�JOIN����

- **ʹ��С���������**����JOIN�����У�ȷ��С����ǰ������ں�

```
SELECT*FROM small_table ST JOIN large_table LT ON ST.id = LT.id;
```

- **����������**��ȷ�������������������Լӿ�JOIN������

#### ���ⲻ��Ҫ�ĸ��Ӳ�ѯ

- **�򻯲�ѯ**�������򻯲�ѯ������ʹ�ò���Ҫ���Ӳ�ѯ��Ƕ�ײ�ѯ��

```
-- ���⸴�ӵ�Ƕ�ײ�ѯSELECT*FROM table_name WHERE id IN (SELECT id FROM another_table WHEREcondition);
-- ʹ��JOIN���SELECT table_name.*FROM table_name JOIN another_table ON table_name.id = another_table.id WHERE another_table.condition;
```

### 2. �Ż����ݿ����

#### �淶���뷴�淶��

- **�淶��**��ȷ�����ݿ���Ʒ��ϵ�����ʽ�������������ࡣ
- **���淶��**����ĳЩ����£�Ϊ�����ܣ������ʶȷ��淶�������ٸ��ӵ�JOIN������

#### ������

- **�����**�����ڷǳ���ı�����ʹ�ñ�����������ݷֳɸ�С�Ĳ��֣���߲�ѯ���ܡ�

```
CREATETABLE orders (
    order_id INT,
    order_date DATE,
    ...
)PARTITIONBYRANGE (YEAR(order_date)) (
    PARTITION p0 VALUES LESS THAN (2020),
    PARTITION p1 VALUES LESS THAN (2021),
    PARTITION p2 VALUES LESS THAN (2022)
);
```

### 3. �Ż�����������

#### ���� MySQL ���ò���

- **��������ش�С**������ InnoDB �洢���棬����innodb_buffer_pool_size������ʹ�価���󣨵���Ҫ���������ڴ�� 70-80%����

```
[mysqld]innodb_buffer_pool_size = 4G
```

- **������ѯ����**������Ӧ�����󣬵�����ѯ�����С��

```
[mysqld]query_cache_size = 64M
```

#### ʹ�ú��ʵĴ洢����

- **ѡ���ʵ��Ĵ洢����**������Ӧ������ѡ����ʵĴ洢���棨�� InnoDB��MyISAM����

### 4. ʹ�����ܷ�������

#### ʹ��EXPLAIN������ѯ

- **����ִ�мƻ�**��ʹ��EXPLAIN������ѯ��ִ�мƻ���ʶ������ƿ����

```
EXPLAIN SELECT*FROM your_table WHERE your_condition;
```

#### ʹ������ģʽ��Performance Schema��

- **�ռ���������**��ʹ�� Performance Schema �ռ���ϸ���������ݣ���������ѯ��

```
SELECT*FROM performance_schema.events_statements_summary_by_digestORDERBY SUM_TIMER_WAIT DESC
LIMIT 10;
```

### 5. ��غ͵���

#### �������

- **ʹ�ü�ع���**��ʹ�� MySQL Enterprise Monitor��Percona Monitoring and Management (PMM)��New Relic��Datadog �ȹ��߳���������ݿ����ܡ�

#### ���ڵ���

- **��������ѯ**�����������Ż�����ѯ��ȷ�����ݿ����ܳ���������