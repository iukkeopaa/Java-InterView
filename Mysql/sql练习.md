```sql
CREATE TABLE employees (
    id INT PRIMARY KEY,
    name VARCHAR(50),
    department VARCHAR(50),
    salary DECIMAL(10, 2),
    hire_date DATE
);

-- ����ʾ������
INSERT INTO employees (id, name, department, salary, hire_date)
VALUES
    (1, '����', '������', 8000, '2022-01-15'),
    (2, '����', '�г���', 6500, '2022-02-20'),
    (3, '����', '������', 9000, '2021-11-30'),
    (4, '����', '������Դ��', 7000, '2022-03-10'),
    (5, 'Ǯ��', '�г���', 7500, '2021-12-05');
```

### 1. ��ѯ����Ա����Ϣ









```sql
SELECT * FROM employees;
```

### 2. ��ѯ����������Ա���������͹���










```sql
SELECT name, salary FROM employees WHERE department = '������';
```

### 3. ��ѯ���ʸ��� 8000 ��Ա��









```sql
SELECT * FROM employees WHERE salary > 8000;
```

### 4. ��ѯÿ�����ŵ�ƽ������








```sql
SELECT department, AVG(salary) AS average_salary
FROM employees
GROUP BY department;
```

### 5. ��ѯ������ߵ�Ա��









```sql
SELECT * FROM employees
WHERE salary = (SELECT MAX(salary) FROM employees);
```

### 6. ��ѯ 2022 ����ְ��Ա��







```sql
SELECT * FROM employees
WHERE YEAR(hire_date) = 2022;
```

### 7. �����ʴӸߵ�������Ա��






```sql
SELECT * FROM employees
ORDER BY salary DESC;
```

### 8. ��ѯÿ�����ŵ�Ա������








```sql
SELECT department, COUNT(*) AS employee_count
FROM employees
GROUP BY department;
```

# =====================================
```sql
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    registration_date DATE NOT NULL,
    last_login TIMESTAMP,
    is_active BOOLEAN DEFAULT true
);

CREATE TABLE products (
                          product_id INT PRIMARY KEY AUTO_INCREMENT,
                          product_name VARCHAR(100) NOT NULL,
                          description TEXT,
                          price DECIMAL(10, 2) NOT NULL,
                          category_id INT NOT NULL,
                          brand_id INT NOT NULL,
                          stock_quantity INT NOT NULL,
                          is_featured BOOLEAN DEFAULT false,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE categories (
                            category_id INT PRIMARY KEY AUTO_INCREMENT,
                            category_name VARCHAR(50) NOT NULL UNIQUE,
                            parent_category_id INT,
                            FOREIGN KEY (parent_category_id) REFERENCES categories(category_id)
);

CREATE TABLE brands (
                        brand_id INT PRIMARY KEY AUTO_INCREMENT,
                        brand_name VARCHAR(50) NOT NULL UNIQUE,
                        country VARCHAR(50),
                        established_year INT
);

CREATE TABLE orders (
                        order_id INT PRIMARY KEY AUTO_INCREMENT,
                        user_id INT NOT NULL,
                        order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        status ENUM('pending', 'paid', 'shipped', 'delivered', 'cancelled') NOT NULL DEFAULT 'pending',
                        total_amount DECIMAL(10, 2) NOT NULL,
                        payment_method VARCHAR(50),
                        shipping_address TEXT NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE order_items (
                             item_id INT PRIMARY KEY AUTO_INCREMENT,
                             order_id INT NOT NULL,
                             product_id INT NOT NULL,
                             quantity INT NOT NULL,
                             unit_price DECIMAL(10, 2) NOT NULL,
                             FOREIGN KEY (order_id) REFERENCES orders(order_id),
                             FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE TABLE reviews (
                         review_id INT PRIMARY KEY AUTO_INCREMENT,
                         user_id INT NOT NULL,
                         product_id INT NOT NULL,
                         rating TINYINT NOT NULL CHECK (rating BETWEEN 1 AND 5),
                         review_text TEXT,
                         review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         is_approved BOOLEAN DEFAULT false,
                         FOREIGN KEY (user_id) REFERENCES users(user_id),
                         FOREIGN KEY (product_id) REFERENCES products(product_id)
);
```