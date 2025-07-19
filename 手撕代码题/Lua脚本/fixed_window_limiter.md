```lua

-- �̶����������㷨
-- KEYS[1]: ������
-- ARGV[1]: ���ڴ�С���룩
-- ARGV[2]: ���������
-- ARGV[3]: ��ǰʱ��������룩

local count = redis.call('INCR', KEYS[1])
if count == 1 then
    redis.call('PEXPIRE', KEYS[1], ARGV[1] * 1000)  -- ���ú��뼶����
end

-- �ж��Ƿ񳬳�����
if count > tonumber(ARGV[2]) then
    -- ���ؾܾ���0������ǰ������ʣ�ര��ʱ��
    return {0, count, redis.call('PTTL', KEYS[1])}
else
    -- ��������1������ǰ������ʣ�ര��ʱ��
    return {1, count, redis.call('PTTL', KEYS[1])}
end
```


```lua
-- ����Ͱ�����㷨ʵ��
-- KEYS[1]: ����Ͱ�����洢��ǰ����������
-- KEYS[2]: ����Ͱ������ʱ���
-- ARGV[1]: ����Ͱ����
-- ARGV[2]: �����������ʣ�ÿ�����ɵ���������
-- ARGV[3]: ��ǰʱ��������룩
-- ARGV[4]: ������Ҫ����������Ĭ��Ϊ1��

local tokens_key = KEYS[1]
local timestamp_key = KEYS[2]
local capacity = tonumber(ARGV[1])
local rate = tonumber(ARGV[2])
local now = tonumber(ARGV[3])
local requested = tonumber(ARGV[4] or 1)

-- �����������ɼ�������룩
local interval = 1000 / rate

-- ��ȡ����Ͱ��ǰ״̬
local last_tokens = tonumber(redis.call('GET', tokens_key) or capacity)
local last_refreshed = tonumber(redis.call('GET', timestamp_key) or 0)

-- ������ϴ�ˢ�µ�������Ҫ���ɵ�������
local delta = math.max(0, now - last_refreshed)
local filled_tokens = math.min(capacity, last_tokens + (delta * rate / 1000))

-- �ж��Ƿ���������
local allowed = filled_tokens >= requested
local new_tokens = filled_tokens
if allowed then
    new_tokens = filled_tokens - requested
end

-- ��������Ͱ״̬
redis.call('SET', tokens_key, new_tokens, 'EX', math.ceil(capacity / rate))
redis.call('SET', timestamp_key, now, 'EX', math.ceil(capacity / rate))

-- ���ؽ�����Ƿ����� + ʣ�������� + �´ο�������ʱ��
return {
    allowed and 1 or 0,
    new_tokens,
    allowed and 0 or interval - (delta % interval)
}

```