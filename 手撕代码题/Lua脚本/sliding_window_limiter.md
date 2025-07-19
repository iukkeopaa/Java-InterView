```lua
-- 滑动窗口限流算法（基于有序集合）
-- KEYS[1]: 限流键（有序集合）
-- ARGV[1]: 窗口大小（毫秒）
-- ARGV[2]: 最大请求数
-- ARGV[3]: 当前时间戳（毫秒）

local window = tonumber(ARGV[1])
local now = tonumber(ARGV[3])
local start = now - window

-- 移除窗口外的记录
redis.call('ZREMRANGEBYSCORE', KEYS[1], 0, start)

-- 统计窗口内请求数
local count = redis.call('ZCARD', KEYS[1])

-- 判断是否超出限制
if count >= tonumber(ARGV[2]) then
    -- 返回拒绝（0）、当前计数、下次可用时间
    return {0, count, window - (now - tonumber(redis.call('ZRANGE', KEYS[1], 0, 0, 'WITHSCORES')[2] or 0))}
else
    -- 记录本次请求
    redis.call('ZADD', KEYS[1], now, now)
    -- 设置过期时间，避免冷key占用内存
    redis.call('PEXPIRE', KEYS[1], window * 2)
    
    -- 返回允许（1）、当前计数、剩余窗口时间
    return {1, count + 1, window}
end
```