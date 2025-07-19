```lua
-- redis_lock_stock.lua
-- �ֲ�ʽ�� + �ۼ�����ԭ�Ӳ���

-- KEYS[1]: �������� "stock:product:1001"
-- KEYS[2]: �������� "lock:product:1001"
-- ARGV[1]: ����Ŀ������
-- ARGV[2]: ���Ĺ���ʱ�䣨���룩
-- ARGV[3]: Ψһ��ʶ���� UUID�������ͷ���ʱ��֤��

local stock_key = KEYS[1]
local lock_key = KEYS[2]
local requested = tonumber(ARGV[1])
local lock_ttl = tonumber(ARGV[2])
local identifier = ARGV[3]

-- ���Ի�ȡ����ʹ�� SETNX ԭ�Ӳ�����
local acquired = redis.call('SET', lock_key, identifier, 'NX', 'PX', lock_ttl)

if not acquired then
    return -1  -- ��ȡ��ʧ�ܣ����� -1
end

-- ��ȡ��ǰ���
local current = tonumber(redis.call('GET', stock_key) or 0)

-- ������Ƿ����
if current < requested or current <= 0 then
    -- ��治�㣬�ͷ��������� 0
    redis.call('DEL', lock_key)
    return 0
end

-- �ۼ����
local new_stock = redis.call('DECRBY', stock_key, requested)

-- �ͷ���
redis.call('DEL', lock_key)

-- ���ؿۼ����ʵ�ʿ��
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
        // ��ʼ�� Lua �ű�
        stockDeductionScript = new DefaultRedisScript<>();
        stockDeductionScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis-lock-stock.lua")));
        stockDeductionScript.setResultType(Long.class);
    }

    /**
     * �ۼ���棨���ֲ�ʽ����
     * @param productId ��ƷID
     * @param quantity �ۼ�����
     * @return �ۼ����
     */
    public String deductStock(String productId, int quantity) {
        String stockKey = "stock:product:" + productId;
        String lockKey = "lock:product:" + productId;
        String identifier = UUID.randomUUID().toString();
        int lockTtl = 3000; // ������ʱ�䣨���룩

        // ִ�� Lua �ű�
        Long result = redisTemplate.execute(
            stockDeductionScript,
            Arrays.asList(stockKey, lockKey),
            quantity, lockTtl, identifier
        );

        if (result == null) {
            return "�ۼ�ʧ�ܣ�δ֪����";
        }

        switch (result.intValue()) {
            case -1:
                return "��ȡ��ʧ�ܣ����Ժ�����";
            case 0:
                return "��治��";
            default:
                return "�ۼ��ɹ���ʣ����: " + result;
        }
    }
}