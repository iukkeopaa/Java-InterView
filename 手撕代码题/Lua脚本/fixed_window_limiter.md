```lua

-- 固定窗口限流算法
-- KEYS[1]: 限流键
-- ARGV[1]: 窗口大小（秒）
-- ARGV[2]: 最大请求数
-- ARGV[3]: 当前时间戳（毫秒）

local count = redis.call('INCR', KEYS[1])
if count == 1 then
    redis.call('PEXPIRE', KEYS[1], ARGV[1] * 1000)  -- 设置毫秒级过期
end

-- 判断是否超出限制
if count > tonumber(ARGV[2]) then
    -- 返回拒绝（0）、当前计数、剩余窗口时间
    return {0, count, redis.call('PTTL', KEYS[1])}
else
    -- 返回允许（1）、当前计数、剩余窗口时间
    return {1, count, redis.call('PTTL', KEYS[1])}
end
```


```lua
-- 令牌桶限流算法实现
-- KEYS[1]: 令牌桶键（存储当前令牌数量）
-- KEYS[2]: 令牌桶最后填充时间键
-- ARGV[1]: 令牌桶容量
-- ARGV[2]: 令牌生成速率（每秒生成的令牌数）
-- ARGV[3]: 当前时间戳（毫秒）
-- ARGV[4]: 请求需要的令牌数（默认为1）

local tokens_key = KEYS[1]
local timestamp_key = KEYS[2]
local capacity = tonumber(ARGV[1])
local rate = tonumber(ARGV[2])
local now = tonumber(ARGV[3])
local requested = tonumber(ARGV[4] or 1)

-- 计算令牌生成间隔（毫秒）
local interval = 1000 / rate

-- 获取令牌桶当前状态
local last_tokens = tonumber(redis.call('GET', tokens_key) or capacity)
local last_refreshed = tonumber(redis.call('GET', timestamp_key) or 0)

-- 计算从上次刷新到现在需要生成的令牌数
local delta = math.max(0, now - last_refreshed)
local filled_tokens = math.min(capacity, last_tokens + (delta * rate / 1000))

-- 判断是否允许请求
local allowed = filled_tokens >= requested
local new_tokens = filled_tokens
if allowed then
    new_tokens = filled_tokens - requested
end

-- 更新令牌桶状态
redis.call('SET', tokens_key, new_tokens, 'EX', math.ceil(capacity / rate))
redis.call('SET', timestamp_key, now, 'EX', math.ceil(capacity / rate))

-- 返回结果：是否允许 + 剩余令牌数 + 下次可用令牌时间
return {
    allowed and 1 or 0,
    new_tokens,
    allowed and 0 or interval - (delta % interval)
}

```