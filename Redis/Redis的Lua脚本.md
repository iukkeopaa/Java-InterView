```lua
-- redis_lock_stock.lua
-- 分布式锁 + 扣减库存的原子操作

-- KEYS[1]: 库存键，如 "stock:product:1001"
-- KEYS[2]: 锁键，如 "lock:product:1001"
-- ARGV[1]: 请求的库存数量
-- ARGV[2]: 锁的过期时间（毫秒）
-- ARGV[3]: 唯一标识（如 UUID，用于释放锁时验证）

local stock_key = KEYS[1]
local lock_key = KEYS[2]
local requested = tonumber(ARGV[1])
local lock_ttl = tonumber(ARGV[2])
local identifier = ARGV[3]

-- 尝试获取锁（使用 SETNX 原子操作）
local acquired = redis.call('SET', lock_key, identifier, 'NX', 'PX', lock_ttl)

if not acquired then
    return -1  -- 获取锁失败，返回 -1
end

-- 获取当前库存
local current = tonumber(redis.call('GET', stock_key) or 0)

-- 检查库存是否充足
if current < requested or current <= 0 then
    -- 库存不足，释放锁并返回 0
    redis.call('DEL', lock_key)
    return 0
end

-- 扣减库存
local new_stock = redis.call('DECRBY', stock_key, requested)

-- 释放锁
redis.call('DEL', lock_key)

-- 返回扣减后的实际库存
return new_stock
```



```lua
local stock_key = KEYS[1]
local lock_key = KEYS[2]
local requested = tonumber(ARGV[1])
local lock_ttl = tonumber(ARGV[2])
local identifier = ARGV[3]

local acquired = redis.call('SET', lock_key, identifier, 'NX', 'PX', lock_ttl)
if not acquired then return -1 end

local current = tonumber(redis.call('GET', stock_key) or 0)
if current < requested or current <= 0 then
    redis.call('DEL', lock_key)
    return 0
end

local new_stock = redis.call('DECRBY', stock_key, requested)
redis.call('DEL', lock_key)
return new_stock
```


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
public class StockService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private RedisScript<Long> stockDeductionScript;

    @Autowired
    public void init() {
        // 初始化 Lua 脚本
        stockDeductionScript = new DefaultRedisScript<>();
        stockDeductionScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis-lock-stock.lua")));
        stockDeductionScript.setResultType(Long.class);
    }

    /**
     * 扣减库存（带分布式锁）
     * @param productId 商品ID
     * @param quantity 扣减数量
     * @return 扣减结果
     */
    public String deductStock(String productId, int quantity) {
        String stockKey = "stock:product:" + productId;
        String lockKey = "lock:product:" + productId;
        String identifier = UUID.randomUUID().toString();
        int lockTtl = 3000; // 锁过期时间（毫秒）

        // 执行 Lua 脚本
        Long result = redisTemplate.execute(
            stockDeductionScript,
            Arrays.asList(stockKey, lockKey),
            quantity, lockTtl, identifier
        );

        if (result == null) {
            return "扣减失败，未知错误";
        }

        switch (result.intValue()) {
            case -1:
                return "获取锁失败，请稍后重试";
            case 0:
                return "库存不足";
            default:
                return "扣减成功，剩余库存: " + result;
        }
    }
}