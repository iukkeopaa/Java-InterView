## �û��� (users)

| id   | name    |
   | ---- | ------- |
| 1    | Alice   |
| 2    | Bob     |
| 3    | Charlie |

## ������ (orders)

| id   | user_id | amount | created_at |
   | ---- | ------- | ------ | ---------- |
| 1    | 1       | 100    | 2024-01-01 |
| 2    | 1       | 300    | 2024-02-01 |
| 3    | 2       | 200    | 2024-01-15 |
| 4    | 3       | 50     | 2023-12-10 |
| 5    | 3       | 70     | 2024-03-10 |

## ��ѯ����

��ѯ�����û������һ�ʶ��������������������



## ��

```sql
SELECT 
    u.name AS user_name,
    o.id AS order_id,
    o.amount,
    o.created_at
FROM orders o
JOIN users u ON o.user_id = u.id
WHERE (o.user_id, o.created_at) IN (
    SELECT user_id, MAX(created_at)
    FROM orders
    GROUP BY user_id
)
ORDER BY o.amount DESC;
```