```lua

-- KEYS[1]: ����
-- KEYS[2]: ����
-- KEYS[3]: �ݵ��Լ�¼��
-- ARGV[1]: ����Ψһ��ʶ����UUID��
-- ARGV[2]: ������ʱ�䣨���룩
-- ARGV[3]: �ۼ�����
-- ARGV[4]: �����ݵ�����

-- ����ݵ��Լ�¼
local idempotentRecord = redis.call('GET', KEYS[3])
if idempotentRecord then
    return tonumber(idempotentRecord)  -- ֱ�ӷ���֮ǰ�Ľ��
end

-- ������Ƿ����
local stock = tonumber(redis.call('GET', KEYS[1]))
if stock == nil then
    return -1  -- ��治����
end

if stock < tonumber(ARGV[3]) then
    return -2  -- ��治��
end

-- ���Ի�ȡ��
local lockAcquired = redis.call('SET', KEYS[2], ARGV[1], 'NX', 'PX', ARGV[2])

if not lockAcquired then
    return -3  -- ����ȡʧ��
end

-- �ٴμ���ݵ��ԣ���ֹ�������ڼ��Ѵ���
idempotentRecord = redis.call('GET', KEYS[3])
if idempotentRecord then
    redis.call('DEL', KEYS[2])  -- �ͷ���
    return tonumber(idempotentRecord)  -- ֱ�ӷ���֮ǰ�Ľ��
end

-- �ۼ����
local newStock = redis.call('DECRBY', KEYS[1], ARGV[3])

-- ��¼�ݵ��Խ����������ʱ�䣬��ֹ�ڴ������
redis.call('SET', KEYS[3], newStock, 'EX', 86400)  -- �������24Сʱ

-- �ͷ���
redis.call('DEL', KEYS[2])

return newStock  -- ���ؿۼ���Ŀ��
```