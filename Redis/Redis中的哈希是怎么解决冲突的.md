### **����ַ������ԭ��**

�������ͨ����ϣ��������õ���ͬ������λ��ʱ��Redis ����Щ��ֵ�Դ洢��ͬһ������λ�õ�**����**�С�ÿ����ϣ��ڵ㣨`dictEntry`������һ��ָ����һ���ڵ��ָ�룬�γ�����ṹ��

### **Redis ��ϣ��ľ���ʵ��**

Redis �Ĺ�ϣ��ṹ������ `dict.h` �У���Ҫ������

1. ��ϣ��`dictht`��

   ��

   c



����









   ```c
   typedef struct dictht {
       dictEntry **table;      // ��ϣ�����飨ÿ��Ԫ��������ͷָ�룩
       unsigned long size;     // ��ϣ���С
       unsigned long sizemask; // ��ϣ���루size-1�������ڼ�������
       unsigned long used;     // ��ʹ�ýڵ���
   } dictht;
   ```





![img](data:image/png;base64,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)

2. ��ϣ��ڵ㣨`dictEntry`��

   ��

   c



����









   ```c
   typedef struct dictEntry {
       void *key;             // ��
       union {
           void *val;
           uint64_t u64;
           int64_t s64;
           double d;
       } v;                   // ֵ
       struct dictEntry *next; // ָ����һ���ڵ��ָ�루�����ͻ��
   } dictEntry;
   ```

### **�����ͻ������**

1. �����ϣֵ

   ��

   plaintext











   ```plaintext
   hash = dictHashKey(ht, key);  // ������Ĺ�ϣֵ
   index = hash & ht->sizemask;  // ͨ�����������������
   ```

2. ����ڵ�

   ��

    - ������λ��Ϊ�գ�ֱ�Ӳ����½ڵ㡣
    - ������λ���Ѵ��ڽڵ㣨��ͻ�����������½ڵ��������**��ͷ**��O (1) ��������

### **�Ż���ʩ**

1. **����ʽ rehash**��
    - ����ϣ�������ӣ�`used/size`�����ߣ�Ĭ�� > 1������ͣ�Ĭ�� < 0.1��ʱ��Redis ��������ݻ����ݣ���ͨ������ʽ rehash ���ɱ�����Ǩ�����±�����һ���� rehash ���������ܿ�����
2. **����ת�����**��Redis 4.0+����
    - �������ȳ�����ֵ��Ĭ�� 8���ҹ�ϣ���С���� 64 ʱ�������ת��Ϊ**�����**������ѯʱ�临�Ӷȴ� O (N) �Ż��� O (logN)��

### **��ȱ��**

- **�ŵ�**��
    - ʵ�ּ򵥣���ͻ����Ч�ʸߣ�������� O (1)����
    - ֧�ֶ�̬���ݣ���Ӧ��ͬ��������
    - ����ת�������һ���Ż��˼�������µ����ܡ�
- **ȱ��**��
    - �������ʱ��ѯЧ���½���δת�����ʱ����
    - �����ڼ���Ҫά���¾�������ϣ��ռ�ö����ڴ档

### **�ܽ�**

Redis ͨ������ַ�������� + ������������ϣ��ͻ����Ͻ���ʽ rehash ���ƣ��ڱ�֤���ܵ�ͬʱ�����˹�ϣ��������������⣬��һ�ָ�Ч��ƽ���ʵ�ַ�����