## threadlocal��ʲô����»����OOM

`ThreadLocal` �� Java ������ʵ���ֲ߳̾������Ĺ����࣬��Ϊÿ��ʹ�øñ������̶߳��ṩһ�������ı���������ÿ���̶߳����Զ����ظı��Լ��ĸ�����������Ӱ�������߳�����Ӧ�ĸ�����Ȼ������ĳЩ����£�`ThreadLocal` ��ʹ�ÿ��ܻᵼ���ڴ������OOM������������ϸ������

### һ���ڴ�й©�� OOM �ĸ���ԭ��

`ThreadLocal` ���� OOM �ĺ���������**�ڴ�й©**���������޷����������������գ�����ռ���ڴ档���峡�����£�

#### 1. **ǿ���������µ��ڴ�й©**

- **ԭ��**��
  `ThreadLocal` ��ʵ��������ÿ���߳��ڲ��� `ThreadLocalMap`���� map �ļ��� `ThreadLocal` ����������ã�ֵ���û����õĶ���ǿ���ã����� `ThreadLocal` ���������գ����������ԣ������߳�δ����ʱ��`ThreadLocalMap` �еļ����Ϊ `null`����ֵ��ǿ���ã��Դ��ڣ��޷������ա�

  java



����









  ```java
  Thread �� ThreadLocalMap �� Entry(value)  // value �޷�������
  ```

- **����**��
  ���̳߳ػ����У��̻߳ᱻ�������������ڳ������û����ʽ���� `ThreadLocal.remove()`����ʹ `ThreadLocal` ���������ѱ����գ���ֵҲ��һֱ�������̵߳� `ThreadLocalMap` �У����۹���ᵼ�� OOM��

#### 2. **�����洢**

- **ԭ��**��
  ��� `ThreadLocal` �洢���Ǵ������������顢���ͼ��ϣ�����ÿ���̶߳�����ά��һ�ݸ������ᵼ���ڴ�ռ�ü��������߳����������ʱ�����У����ܳű����ڴ档

#### 3. **�߳��������ڹ���**

- ԭ��

  ��

  ���̵߳���������ԶԶ����



  ```
  ThreadLocal
  ```



��ʹ������ʱ�����̳߳��еĺ����̣߳���

  ```
  ThreadLocalMap
  ```



�е�ֵ�޷������ա����磺

java



����









  ```java
  ExecutorService executor = Executors.newFixedThreadPool(10);
  for (int i = 0; i < 1000; i++) {
      executor.submit(() -> {
          ThreadLocal<byte[]> local = new ThreadLocal<>();
          local.set(new byte[1024 * 1024]);  // ÿ���߳�1MB
          // δ���� local.remove()
      });
  }
  // �̳߳��е��̲߳�����ֹ�������ڴ����ռ��
  ```

### �������� OOM ����

#### 1. **�̳߳� + δ����� ThreadLocal**

- **����**��
  �̳߳��е��̻߳ᱻ���ã��� `ThreadLocal` ��ʹ�ú�δ���� `remove()`����ֵ����ÿ���̸߳���ʱ�ۻ������յ��� OOM��

- **ʾ��**��

  java



����









  ```java
  ExecutorService executor = Executors.newFixedThreadPool(5);
  for (int i = 0; i < 1000; i++) {
      executor.execute(() -> {
          ThreadLocal<List<Object>> local = new ThreadLocal<>();
          local.set(new ArrayList<>(1000));  // ÿ�����񴴽������
          // ���������δ���� local.remove()
      });
  }
  ```

#### 2. **�߲��������µĴ����߳�**

- ����

  ��

  �ڸ߲��������У���ÿ���̶߳������Լ���



  ```
  ThreadLocal
  ```



�������Ҵ洢����󣬻ᵼ���ڴ�ռ�ó��������������磺

java



����









  ```java
  public class OOMExample {
      private static final ThreadLocal<byte[]> threadLocal = ThreadLocal.withInitial(() -> new byte[1024 * 1024]); // 1MB
  
      public static void main(String[] args) throws InterruptedException {
          for (int i = 0; i < 1000; i++) {  // ����1000���߳�
              new Thread(() -> {
                  try {
                      Thread.sleep(1000);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                  // ÿ���߳�ռ��1MB�����ڴ�����1GB
              }).start();
          }
      }
  }
  ```

#### 3. **ʹ�� ThreadLocal �洢 Session �ȳ��������ڶ���**

- **����**��
  �� Web Ӧ���У����� `ThreadLocal` �洢�û��Ự��Session������δ���������ʱ�����ᵼ��ÿ���̳߳������лỰ��������ڴ�й©��

### ������α��� ThreadLocal ���µ� OOM

#### 1. **��ʱ����ʹ�� try-finally ��**

- ��ÿ��ʹ����



  ```
  ThreadLocal
  ```



����ص���



  ```
  remove()
  ```



����������ݡ�

java



����









  ```java
  ThreadLocal<BigObject> threadLocal = new ThreadLocal<>();
  try {
      threadLocal.set(new BigObject());
      // ʹ�� threadLocal
  } finally {
      threadLocal.remove();  // ȷ�������Ƿ��쳣������
  }
  ```

#### 2. **ʹ�������ð�װֵ����**

- ���޷�����



  ```
  ThreadLocal
  ```



���������ڣ��ɽ��洢�Ķ�����



  ```
  WeakReference
  ```



��װ��ʹ���ܱ��������ա�

java



����









  ```java
  ThreadLocal<WeakReference<BigObject>> threadLocal = new ThreadLocal<>();
  threadLocal.set(new WeakReference<>(new BigObject()));
  ```






#### 3. **�����߳���������**

- �������̳߳��г��ڳ��� `ThreadLocal`����ʹ�� `ThreadLocal` ʱ����ѡ����������̡߳�

#### 4. **��������**

- ͨ�����ߣ��� VisualVM��MAT������ڴ�ʹ�ã���ʱ�����ڴ�й©��
- �������� JVM �Ѵ�С��`-Xmx` ���������������ڴ治�㵼�� OOM��

### �ġ��ܽ�

`ThreadLocal` ������ֱ�ӵ��� OOM�������ʹ�ò�������δ�����洢������߳��������ڹ��������������ڴ�й©�����յ����ڴ�������ؼ����ڣ�**ȷ��ÿ���߳��ڲ�����Ҫ `ThreadLocal` ����ʱ����ʽ���� `remove()` ����**����������ƴ洢����Ĵ�С���������ڡ�


## Ϊʲôʹ�� static ���� ThreadLocal 

### 1. **����ʵ������ʡ����**

`ThreadLocal` �ĺ���������Ϊ**ÿ��ʹ�øñ������߳�**�ṩ�����ĸ������� `ThreadLocal` ������ͨ����ȫ�ֹ���ģ�����Ϊÿ���̴߳����µ� `ThreadLocal` ʵ����ʹ�� `static` ���ο���ȷ����



- **ȫ��Ψһ��**������Ӧ����ֻ��һ�� `ThreadLocal` ʵ���������̹߳����ʵ����
- **��ʡ�ڴ�**�������ظ����� `ThreadLocal` ���󣬼��ٿ�����



**ʾ��**��



java



����









```java
public class ConnectionManager {
    // ��̬ ThreadLocal�������̹߳���ͬһ�� ThreadLocal ʵ��
    private static final ThreadLocal<Connection> connectionHolder = 
        ThreadLocal.withInitial(() -> DriverManager.getConnection(DB_URL));
    
    public static Connection getConnection() {
        return connectionHolder.get(); // ÿ���̻߳�ȡ�Լ��� Connection ����
    }
}
```

### 2. **�����������ڰ�**

`static` ���������࣬�������ʵ��������ζ�ţ�



- **����ʵ������**������ֱ��ͨ���������� `ThreadLocal`�������Ϲ���������ģʽ���� `ThreadLocalRandom.current()`����
- **�������ڸ���**��`ThreadLocal` ����������������������һ�£�ͨ������Ӧ�������������ڡ����ʺ���Ҫ���ڴ��ڵ���������Ϣ�����û��Ự�����ݿ����ӣ���

### 3. **�����ڴ�й©����**

�� `ThreadLocal` ������Ϊʵ���������� `static`�������ܵ��£�



- **���ʵ������**��ÿ����ʵ���������һ�� `ThreadLocal` ����������ࡣ
- **Ǳ���ڴ�й©**������ʵ��������ʱ�����߳��Դ�`ThreadLocal` ��������޷����������գ����̵߳� `ThreadLocalMap` �Գ��������ã���



�� `static` ���ε� `ThreadLocal` ����󶨣�ֻҪ��δ��ж�أ�������ʼ����Ч����������ʵ�������յ��µ� `ThreadLocal` ʧЧ���⡣

### 4. **����ʹ�ó���**

`ThreadLocal` ���������³�������Щ����ͨ����Ҫȫ�ֹ���������ģ�



- �̰߳�ȫ�Ĺ�����

  ����



  ```
  SimpleDateFormat
  ```

�����ݿ����ӳء�

java



����









  ```java
  public class DateUtil {
      private static final ThreadLocal<SimpleDateFormat> formatter = 
          ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
  }
  ```

- �û��Ự����

  ���洢��ǰ�̵߳��û���Ϣ��

  java



����









  ```java
  public class UserContext {
      private static final ThreadLocal<User> currentUser = new ThreadLocal<>();
  }
  ```

- **����������**���ڷֲ�ʽϵͳ�д洢��ǰ�̵߳����� ID��

### 5. **���̳߳���ϵı�Ҫ��**

���̳߳ػ����У��̻߳ᱻ���ã��� `ThreadLocal` δ�� `static` ���Σ�



- ÿ�δ����µ���ʵ��ʱ�����������µ� `ThreadLocal` ���󣬵����̵߳� `ThreadLocalMap` �л��۶����Ч�� `ThreadLocal` ��Ŀ��
- ��δ��ʱ���� `remove()`���ɵ� `ThreadLocal` ֵ����Ӱ������������̸߳��ã���



�� `static` ���ε� `ThreadLocal` ȫ��Ψһ��ȷ���̸߳���ʱ�����߼���һ���ԡ�

### �ܽ�

**ʹ�� `static` ���� `ThreadLocal` �ĺ���Ŀ���ǣ�**



1. **���� `ThreadLocal` ʵ��**�������ظ���������ʡ�ڴ档
2. **�� `ThreadLocal` �����������ڰ�**��ȷ��ȫ��Ψһ�Ժͳ�����Ч�ԡ�
3. **�����ڴ�й©����**�����������̳߳ػ����С�



**ע������**��



- `static` ������ `ThreadLocal` ������������洢��ֵ��ÿ���̵߳ĸ���ֵ���Ƕ����ġ�
- ʹ�ú���ص��� `remove()` �������ݣ������̸߳��õ��µ�������Ⱦ��



**����**������ʾ������



java



����









```java
public class BadExample {
    private ThreadLocal<Integer> counter = new ThreadLocal<>(); // �Ǿ�̬��
    
    public void increment() {
        counter.set(counter.get() + 1); // ÿ��ʵ�������Լ��� ThreadLocal�����»���
    }
}
```