```lua
-- �������������㷨���������򼯺ϣ�
-- KEYS[1]: �����������򼯺ϣ�
-- ARGV[1]: ���ڴ�С�����룩
-- ARGV[2]: ���������
-- ARGV[3]: ��ǰʱ��������룩

local window = tonumber(ARGV[1])
local now = tonumber(ARGV[3])
local start = now - window

-- �Ƴ�������ļ�¼
redis.call('ZREMRANGEBYSCORE', KEYS[1], 0, start)

-- ͳ�ƴ�����������
local count = redis.call('ZCARD', KEYS[1])

-- �ж��Ƿ񳬳�����
if count >= tonumber(ARGV[2]) then
    -- ���ؾܾ���0������ǰ�������´ο���ʱ��
    return {0, count, window - (now - tonumber(redis.call('ZRANGE', KEYS[1], 0, 0, 'WITHSCORES')[2] or 0))}
else
    -- ��¼��������
    redis.call('ZADD', KEYS[1], now, now)
    -- ���ù���ʱ�䣬������keyռ���ڴ�
    redis.call('PEXPIRE', KEYS[1], window * 2)
    
    -- ��������1������ǰ������ʣ�ര��ʱ��
    return {1, count + 1, window}
end
```