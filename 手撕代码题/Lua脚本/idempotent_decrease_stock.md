```lua

-- KEYS[1]: 库存键
-- KEYS[2]: 锁键
-- KEYS[3]: 幂等性记录键
-- ARGV[1]: 请求唯一标识（如UUID）
-- ARGV[2]: 锁过期时间（毫秒）
-- ARGV[3]: 扣减数量
-- ARGV[4]: 请求幂等令牌

-- 检查幂等性记录
local idempotentRecord = redis.call('GET', KEYS[3])
if idempotentRecord then
    return tonumber(idempotentRecord)  -- 直接返回之前的结果
end

-- 检查库存是否充足
local stock = tonumber(redis.call('GET', KEYS[1]))
if stock == nil then
    return -1  -- 库存不存在
end

if stock < tonumber(ARGV[3]) then
    return -2  -- 库存不足
end

-- 尝试获取锁
local lockAcquired = redis.call('SET', KEYS[2], ARGV[1], 'NX', 'PX', ARGV[2])

if not lockAcquired then
    return -3  -- 锁获取失败
end

-- 再次检查幂等性（防止锁竞争期间已处理）
idempotentRecord = redis.call('GET', KEYS[3])
if idempotentRecord then
    redis.call('DEL', KEYS[2])  -- 释放锁
    return tonumber(idempotentRecord)  -- 直接返回之前的结果
end

-- 扣减库存
local newStock = redis.call('DECRBY', KEYS[1], ARGV[3])

-- 记录幂等性结果（带过期时间，防止内存溢出）
redis.call('SET', KEYS[3], newStock, 'EX', 86400)  -- 结果缓存24小时

-- 释放锁
redis.call('DEL', KEYS[2])

return newStock  -- 返回扣减后的库存
```