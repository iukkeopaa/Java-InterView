�� Redis ����ۼ�����������ۼ������ֿۼ������������ȣ��У�**��Ѫģ�ͣ�Rich Domain Model��** ��һ�ֽ�����������߼���װ��һ������˼�룬�������� ���ۼ�������� �Ȱ�������״̬���統ǰ���ֵ���ۼ���ֵ�����ְ���������Щ���ݵ���Ϊ����ۼ��߼���У�����ԭ���Ա�֤�ȣ������ǽ����ݺ��߼����루ƶѪģ�ͣ���

### Ϊʲô Redis �ۼ���Ҫ��Ѫģ�ͣ�

Redis �ۼ�����ͨ������**�߲�����ԭ����Ҫ��ߡ�ҵ�������**�����⣨���磺��治��Ϊ�����ۼ���У��Ȩ�ޡ��ۼ����败�������߼��ȣ���������ƶѪģ�ͣ����ݴ��� Redis �У��߼���ɢ��ҵ���������ᵼ�£�

- �߼���ɢ���ظ�����ࣨ��ദ�ۼ�����ҪУ�����Ƿ���㣩��
- ԭ���Ա�֤�߼���ҵ���߼���ϣ��׳���
- ����ά������չ�������ۼ��������޸Ķദ���룩��

����Ѫģ��ͨ�� ������ + ��Ϊ�� �ķ�װ���ɽ����Щ���⡣

### Redis �ۼ��г�Ѫģ�͵ĺ���Ҫ��

��Ѫģ�͵ĺ��������һ�� ���ۼ�������󡱣���`RedisDeductor`�������ڲ�������

#### 1. ����״̬���� Redis �����ĺ������ݣ�

- ������`key`����Redis �д洢�ۼ�Ŀ��ļ�����`stock:1001`��ʾ��Ʒ 1001 �Ŀ�棩��
- ��ǰֵ��`currentValue`������ Redis �ж�ȡ�ĵ�ǰֵ�����ӳټ��أ���
- �ۼ���ֵ��`minThreshold`�����������Сֵ�����治��С�� 0����ֵΪ 0����
- Redis ���� / �ͻ��ˣ�`redisClient`�������� Redis �Ŀͻ���ʵ����

#### 2. ��Ϊ��������װ�ۼ�����߼���

���ۼ������е����в�����У�顢ԭ����ִ�С��쳣��������֪ͨ�ȣ���װΪ����ķ��������磺

- **У�鷽��**��`validateDeduct(int amount)`
  ���ۼ�����Ƿ�Ϸ����粻��Ϊ�������ۼ����Ƿ񳬹���ֵ������ۼ�����С�� 0����
- **ԭ�ӿۼ�����**��`deduct(int amount)`
  ���� Redis ��ԭ�Ӳ�������`DECRBY`��`INCRBY`���� Lua �ű�ִ�пۼ�����֤������ȫ��
  �������ۼ����ֵС����ֵ����ع�������ʧ�ܡ�
- **��ѯ��ǰֵ����**��`getCurrentValue()`
  �� Redis �ж�ȡ����ֵ����װ Redis ��`GET`��������
- **����֪ͨ����**��`notifyAfterDeduct()`
  �ۼ��ɹ��󴥷������߼������治��ʱ����Ԥ�����ۼ��ɹ����¼��־����

### ʾ�������ۼ��ĳ�Ѫģ��ʵ��

����Ʒ���ۼ�Ϊ����չʾ��Ѫģ�͵Ĵ���ṹ��α���룩��

java



����









```java
// ���ۼ�������󣨳�Ѫģ�ͣ�
public class StockDeducter {
    // ����״̬����Redis��صĺ������ݣ�
    private final String redisKey; // Redis������"stock:1001"��
    private final RedisTemplate redisTemplate; // Redis�ͻ���
    private final long minThreshold; // �����С��ֵ����0��������Ϊ����
    private Long currentStock; // ��ǰ��棨�ӳټ��أ�

    // ���췽������ʼ������
    public StockDeducter(String skuId, RedisTemplate redisTemplate) {
        this.redisKey = "stock:" + skuId;
        this.redisTemplate = redisTemplate;
        this.minThreshold = 0L;
    }

    // ��Ϊ1��У��ۼ��Ϸ���
    private boolean validateDeduct(long deductAmount) {
        if (deductAmount <= 0) {
            throw new IllegalArgumentException("�ۼ���������Ϊ����");
        }
        // Ԥ�鵱ǰ��棨��ԭ�ӣ���������У�飩
        long current = getCurrentValue();
        if (current - deductAmount < minThreshold) {
            throw new InsufficientStockException("��治�㣬��ǰ: " + current + ", ��ۼ�: " + deductAmount);
        }
        return true;
    }

    // ��Ϊ2��ִ��ԭ�ӿۼ��������߼���
    public boolean doDeduct(long deductAmount) {
        // 1. ��У��
        validateDeduct(deductAmount);

        // 2. ��Redis Lua�ű���֤ԭ���ԣ��ۼ�+У�飩
        String luaScript = "local current = tonumber(redis.call('get', KEYS[1]) or '0') " +
                          "if current - ARGV[1] < ARGV[2] then " +
                          "   return -1 " + // �ۼ�ʧ��
                          "end " +
                          "redis.call('decrby', KEYS[1], ARGV[1]) " +
                          "return redis.call('get', KEYS[1])"; // ���ؿۼ����ֵ

        // ִ��Lua�ű�
        Long result = (Long) redisTemplate.execute(
            new DefaultRedisScript<>(luaScript, Long.class),
            Collections.singletonList(redisKey),
            String.valueOf(deductAmount),
            String.valueOf(minThreshold)
        );

        // 3. ������
        if (result == -1) {
            return false; // �ۼ�ʧ�ܣ��������¿�治�㣩
        }
        this.currentStock = result; // ���µ�ǰֵ
        notifyAfterDeduct(); // ������������
        return true;
    }

    // ��Ϊ3����ѯ��ǰ���
    public long getCurrentValue() {
        if (currentStock == null) {
            String value = (String) redisTemplate.opsForValue().get(redisKey);
            currentStock = value == null ? 0L : Long.parseLong(value);
        }
        return currentStock;
    }

    // ��Ϊ4���ۼ���֪ͨ������־��Ԥ����
    private void notifyAfterDeduct() {
        if (currentStock <= 10) { // ������Ԥ��
            log.warn("��Ʒ{}��治�㣬��ǰ: {}", redisKey, currentStock);
            // ������Ϣ��MQ��������������
        }
    }
}
```

### ��Ѫģ�͵����ƣ��� Redis �ۼ��У�

1. **�߼��ھ�**���ۼ���ص�У�顢ԭ�Ӳ���������֪ͨ���߼���������������У������ɢ��ҵ������
2. **�����Ը�**��ͬһ��ۼ����������桢���֣��ɸ��øö��󣬼����ظ�������
3. **��ά��**���޸Ŀۼ���������ֵ������У���߼������ֻ���޸������������Ķ����е��ô���
4. **������ȫ**��ԭ�Ӳ�����Lua �ű�����ҵ���߼���װ��һ�𣬱���©д����� Redis ԭ�����

### �ܽ�

Redis �ۼ��еĳ�Ѫģ�ͣ�������ͨ�� ������ + ��Ϊ�� �ķ�װ�����ۼ������е� Redis ������ԭ���Ա�֤��ҵ��У����߼��ھ۵���������У��Ӷ���Ӧ�߲��������µĸ����ԣ���ߴ���Ŀ�ά���ԺͿɿ��ԡ�