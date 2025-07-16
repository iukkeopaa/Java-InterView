## Mysql����μ����������ν��


�� MySQL �У�������ָ��������������ִ�й���������������Դ����ɵ�ѭ���ȴ����󣬵�����Щ�����޷�����ִ�С������ǹ��� MySQL �����ļ�ⷽ��������ԭ�򼰽����������ϸ���ܣ�

### **һ����������ķ���**

#### **1. �鿴������־����ֱ�ӣ�**

����������ʱ��MySQL ���ڴ�����־����`error.log`���м�¼��ϸ��Ϣ����������������ʱ�䡢�漰������ SQL�����ȴ���ϵ�ȡ�



**ʾ����־Ƭ��**��



plaintext











```plaintext
2025-07-16 10:30:45 140584220004096 [ERROR] InnoDB: Deadlock found ...
2025-07-16 10:30:45 140584220004096 [ERROR] InnoDB: Transaction 1:
2025-07-16 10:30:45 140584220004096 [ERROR] InnoDB:   UPDATE users SET balance = balance - 100 WHERE id = 1;
2025-07-16 10:30:45 140584220004096 [ERROR] InnoDB: Transaction 2:
2025-07-16 10:30:45 140584220004096 [ERROR] InnoDB:   UPDATE users SET balance = balance + 100 WHERE id = 2;
```





![img](data:image/png;base64,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)

#### **2. ��ѯ`information_schema.INNODB_TRX`��`INNODB_LOCKS`��**

�����������󣬿�ͨ������ SQL ��ѯ��ǰ��Ծ���������Ϣ��



sql











```sql
-- ��ѯ��ǰ��Ծ����
SELECT * FROM information_schema.INNODB_TRX;

-- ��ѯ��ǰ���ȴ����
SELECT * FROM information_schema.INNODB_LOCKS;

-- ��ѯ���ȴ�����ϸ��ջ��������innodb_print_all_deadlocks������
SELECT * FROM information_schema.INNODB_LOCK_WAITS;
```

#### **3. ʹ��`SHOW ENGINE INNODB STATUS`��ʵʱ��أ�**

ִ�д�����ɻ�ȡ InnoDB �洢�������ϸ״̬���������һ��������������ջ��Ϣ����������ִ��˳�������͡��ȴ���ϵ�ȣ���



**�ؼ����ʾ��**��



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

### **���������ĳ���ԭ��**

#### **1. ����ִ��˳��һ��**

�����������෴��˳���ȡ�������磺



- **���� 1**��`UPDATE A; UPDATE B;`
- **���� 2**��`UPDATE B; UPDATE A;`

#### **2. �����Ȳ�ƥ��**

- ������������ã���һ������ʹ����������һ��ʹ��`LOCK TABLES`����
- ����ʧЧ������������Ϊ������

#### **3. �����������ʱ�����**

����ִ��ʱ��������������ȴ�ʱ�����ӣ���������������

#### **4. �߲����µ�������**

�ڸ߲��������£��������Ƶ������ͬһ��Դ������

### **������������Ĳ���**

#### **1. �Ż�������ƣ��������**

- **���������С**����������ִ��ʱ�䣬����������ʱ�䡣
- **ͳһ����ȡ˳��**��ȷ��������������ͬ˳�������Դ���簴 ID ������¼�¼����
- **���������е�������**��������һ��������ͬʱ���������������¼��

#### **2. �������뼶��**

- ���͸��뼶��

  ����

  ```
  REPEATABLE READ
  ```

  ��Ĭ�ϣ�����Ϊ

  ```
  READ COMMITTED
  ```

  �����ټ�϶����gap lock����ʹ�ã������������ʡ�

  sql











  ```sql
  SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;
  ```

#### **3. �����Ż�**

- **ȷ����ѯʹ������**������ȫ��ɨ�赼����������Ϊ������
- **��Ӻ��ʵ�����**�����磬��`WHERE`�����ֶ��ϴ���������

#### **4. ���Ի���**

��Ӧ�ò㲶�������쳣���� MySQL ������`1213`������ʵ�������߼���



java











```java
// Javaʾ�������������쳣������
int maxRetries = 3;
for (int i = 0; i < maxRetries; i++) {
    try {
        // ִ���������
        executeTransaction();
        break;
    } catch (SQLException e) {
        if (isDeadlockException(e)) { // �ж��Ƿ�Ϊ�����쳣
            Thread.sleep(100 * (i + 1)); // ָ���˱�
            continue;
        }
        throw e;
    }
}
```

#### **5. ���� MySQL ����**

- �������ȴ���ʱʱ��

  ��ͨ��

  ```
  innodb_lock_wait_timeout
  ```

  ������Ĭ�� 50 �룩��������ȴ�����ʱ�䡣

  sql











  ```sql
  SET GLOBAL innodb_lock_wait_timeout = 10; -- ��ʱʱ����Ϊ10��
  ```

- �������������־

  ��ͨ��

  ```
  innodb_print_all_deadlocks
  ```

  ������¼����������Ϣ������ʱʹ�ã���

  sql











  ```sql
  SET GLOBAL innodb_print_all_deadlocks = 1;
  ```

#### **6. ʹ���������ͼ�϶��**

��Ⲣ����ʹ����������Intention Lock���ͼ�϶����Gap Lock������������Χ����



- ��`READ COMMITTED`���뼶���£���϶������`INSERT`ʱʹ�ã��������������ԡ�

### **�ġ�Ԥ�����������ʵ��**

1. **�������Ƶ��**�����ڷ���������־��ͳ������������Ƶ�ʺͳ�����
2. **ѹ����֤**���ڸ߲��������½���ѹ�����ԣ���ǰ����Ǳ�ڵ��������ա�
3. **ʹ��`SHOW ENGINE INNODB STATUS`����**�����������������鿴�������������λ���⡣
4. **����ʹ��`REPEATABLE READ`+ ����**���ڱ�֤����һ���Ե�ǰ���£�ͨ��������������ͻ��

### **�塢�ܽ�**

���������ݿⲢ�������в��ɱ�������⣬��ͨ���������ƺ��Ż����������������䷢�����ʡ��ؼ�����**��������Ż�**��**�����Ż�**��**�����Ե���**��ͬʱ��ϼ�غ����Ի��ƣ�ȷ��ϵͳ�ڷ�������ʱ�ܿ��ٻָ���