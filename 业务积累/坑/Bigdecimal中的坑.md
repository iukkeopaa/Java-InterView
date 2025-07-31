## tostring�������ܻᵼ�¿�ѧ���������������
```java
BigDecimal big = new BigDecimal("1000000000000000000");
System.out.println(big.toString()); // ���������1E+18
```

## ��double����BigDecimal���¾��ȶ�ʧ

```java
BigDecimal d1 = new BigDecimal(0.1);
System.out.println(d1); // �����0.1000000000000000055511151231257827021181583404541015625
```

�������

```java
BigDecimal d2 = new BigDecimal("0.1");
System.out.println(d2); // �����0.1����ȷ��
```


## equals()�����Ƚϵ��� ��ֵ + ���ȡ������ǵ�������ֵ

```java
BigDecimal a = new BigDecimal("2.0");
BigDecimal b = new BigDecimal("2.00");

System.out.println(a.equals(b)); // �����false�����Ȳ�ͬ��
```

�������

```java
System.out.println(a.compareTo(b) == 0); // �����true�����Ƚ���ֵ��
```

## ��������δָ������ģʽ�������׳�ArithmeticException

```java

BigDecimal a = new BigDecimal("1");
BigDecimal b = new BigDecimal("3");
a.divide(b); // �׳��쳣��java.lang.ArithmeticException: Non-terminating decimal expansion
```

�������

```java

// ����2λС������������
BigDecimal result = a.divide(b, 2, RoundingMode.HALF_UP);
System.out.println(result); // �����0.33
```