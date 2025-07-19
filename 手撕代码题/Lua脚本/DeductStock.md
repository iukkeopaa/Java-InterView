```lua
-- KEYS[1]: ����
-- KEYS[2]: ����
-- ARGV[1]: ����Ψһ��ʶ����UUID��
-- ARGV[2]: ������ʱ�䣨���룩
-- ARGV[3]: �ۼ�����

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

-- �ۼ����
local newStock = redis.call('DECRBY', KEYS[1], ARGV[3])

-- �ͷ���
redis.call('DEL', KEYS[2])

return newStock  -- ���ؿۼ���Ŀ��
```