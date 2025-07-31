## tostring方法可能会导致科学计数法的问题出现
```java
BigDecimal big = new BigDecimal("1000000000000000000");
System.out.println(big.toString()); // 可能输出：1E+18
```

## 用double构造BigDecimal导致精度丢失

```java
BigDecimal d1 = new BigDecimal(0.1);
System.out.println(d1); // 输出：0.1000000000000000055511151231257827021181583404541015625
```

解决方法

```java
BigDecimal d2 = new BigDecimal("0.1");
System.out.println(d2); // 输出：0.1（精确）
```


## equals()方法比较的是 “值 + 精度”，而非单纯的数值

```java
BigDecimal a = new BigDecimal("2.0");
BigDecimal b = new BigDecimal("2.00");

System.out.println(a.equals(b)); // 输出：false（精度不同）
```

解决方法

```java
System.out.println(a.compareTo(b) == 0); // 输出：true（仅比较数值）
```

## 除法运算未指定舍入模式，可能抛出ArithmeticException

```java

BigDecimal a = new BigDecimal("1");
BigDecimal b = new BigDecimal("3");
a.divide(b); // 抛出异常：java.lang.ArithmeticException: Non-terminating decimal expansion
```

解决方法

```java

// 保留2位小数，四舍五入
BigDecimal result = a.divide(b, 2, RoundingMode.HALF_UP);
System.out.println(result); // 输出：0.33
```