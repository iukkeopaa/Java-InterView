-- 定义局部变量，映射传入的KEYS参数（Redis键名）
-- KEYS[1]：计数器的计数键（记录单位时间内的操作次数）
local counter_count_key = KEYS[1]
-- KEYS[2]：计数器的时间戳键（记录计数的最后重置时间）
local counter_timestamp_key = KEYS[2]
-- KEYS[3]：验证码ID对应的键（标记是否需要验证验证码）
local verify_captcha_id = KEYS[3]

-- 定义局部变量，映射传入的ARGV参数（脚本参数）
-- ARGV[1]：触发验证码的阈值（单位时间内操作次数超过此值则需要验证）
local verify_captcha_threshold = tonumber(ARGV[1])
-- ARGV[2]：当前时间戳（毫秒级，用于判断时间窗口）
local current_time_millis = tonumber(ARGV[2])
-- ARGV[3]：验证码ID键的过期时间（秒，控制标记的有效期）
local verify_captcha_id_expire_time = tonumber(ARGV[3])
-- ARGV[4]：是否强制验证验证码的标志（1=强制验证，0=按需验证）
local always_verify_captcha = tonumber(ARGV[4])

-- 时间窗口阈值（毫秒）：超过此时间则重置计数器（此处固定为1000ms，即1秒）
local differenceValue = 1000

-- 若强制验证验证码（always_verify_captcha=1）
if always_verify_captcha == 1 then
    -- 设置验证码ID键为"yes"（标记需要验证）
    redis.call('set', verify_captcha_id, 'yes')
    -- 设置验证码ID键的过期时间
    redis.call('expire', verify_captcha_id, verify_captcha_id_expire_time)
    -- 返回"true"表示需要验证
    return 'true'
end

-- 非强制验证场景：获取当前计数器的计数（若键不存在则默认0）
local count = tonumber(redis.call('get', counter_count_key) or "0")
-- 获取计数器最后一次重置的时间戳（若键不存在则默认0）
local lastResetTime = tonumber(redis.call('get', counter_timestamp_key) or "0")

-- 判断当前时间是否超出时间窗口（当前时间 - 最后重置时间 > 1000ms）
if current_time_millis - lastResetTime > differenceValue then
    -- 超出时间窗口：重置计数为0
    count = 0
    redis.call('set', counter_count_key, count)
    -- 更新最后重置时间为当前时间
    redis.call('set', counter_timestamp_key, current_time_millis)
end

-- 计数递增（当前操作计入次数）
count = count + 1

-- 若当前计数超过阈值（达到需要验证验证码的条件）
if count > verify_captcha_threshold then
    -- 重置计数为0（触发验证后，重新开始计数）
    count = 0
    redis.call('set', counter_count_key, count)
    -- 更新最后重置时间为当前时间
    redis.call('set', counter_timestamp_key, current_time_millis)
    -- 标记需要验证验证码（设置验证码ID键为"yes"）
    redis.call('set', verify_captcha_id, 'yes')
    -- 设置验证码ID键的过期时间
    redis.call('expire', verify_captcha_id, verify_captcha_id_expire_time)
    -- 返回"true"表示需要验证
    return 'true'
end

-- 未超过阈值：更新计数
redis.call('set', counter_count_key, count)
-- 标记不需要验证验证码（设置验证码ID键为"no"）
redis.call('set', verify_captcha_id, 'no')
-- 设置验证码ID键的过期时间
redis.call('expire', verify_captcha_id, verify_captcha_id_expire_time)
-- 返回"false"表示不需要验证
return 'false'



该脚本用于实现「单位时间内操作次数超限则触发验证码验证」的功能，主要逻辑如下：

支持强制验证（always_verify_captcha=1），直接标记需要验证并返回。
非强制场景下，通过「计数器 + 时间窗口」控制：
时间窗口为 1 秒（differenceValue=1000ms），超过窗口则重置计数。
1 秒内操作次数超过阈值（verify_captcha_threshold）时，触发验证码验证，并重置计数。
未超过阈值时，仅更新计数，标记不需要验证。
用 Redis 键记录计数、时间戳和验证码状态，并设置过期时间避免键长期残留。


package com.damai.service;

import com.damai.captcha.model.common.ResponseModel;
import com.damai.captcha.model.vo.CaptchaVO;
import com.baidu.fsg.uid.UidGenerator;
import com.damai.core.RedisKeyManage;
import com.damai.redis.RedisKeyBuild;
import com.damai.service.lua.CheckNeedCaptchaOperate;
import com.damai.vo.CheckNeedCaptchaDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料
 * @description: 判断是否需要验证码
 * @author: 阿星不是程序员
 **/
@Service
public class UserCaptchaService {

    @Value("${verify_captcha_threshold:10}")
    private int verifyCaptchaThreshold;

    @Value("${verify_captcha_id_expire_time:60}")
    private int verifyCaptchaIdExpireTime;

    @Value("${always_verify_captcha:0}")
    private int alwaysVerifyCaptcha;

    @Autowired
    private CaptchaHandle captchaHandle;

    @Autowired
    private UidGenerator uidGenerator;

    @Autowired
    private CheckNeedCaptchaOperate checkNeedCaptchaOperate;

    public CheckNeedCaptchaDataVo checkNeedCaptcha() {
        long currentTimeMillis = System.currentTimeMillis();
        long id = uidGenerator.getUid();
        List<String> keys = new ArrayList<>();
        keys.add(RedisKeyBuild.createRedisKey(RedisKeyManage.COUNTER_COUNT).getRelKey());
        keys.add(RedisKeyBuild.createRedisKey(RedisKeyManage.COUNTER_TIMESTAMP).getRelKey());
        keys.add(RedisKeyBuild.createRedisKey(RedisKeyManage.VERIFY_CAPTCHA_ID,id).getRelKey());
        String[] data = new String[4];
        data[0] = String.valueOf(verifyCaptchaThreshold);
        data[1] = String.valueOf(currentTimeMillis);
        data[2] = String.valueOf(verifyCaptchaIdExpireTime);
        data[3] = String.valueOf(alwaysVerifyCaptcha);
        Boolean result = checkNeedCaptchaOperate.checkNeedCaptchaOperate(keys, data);
        CheckNeedCaptchaDataVo checkNeedCaptchaDataVo = new CheckNeedCaptchaDataVo();
        checkNeedCaptchaDataVo.setCaptchaId(id);
        checkNeedCaptchaDataVo.setVerifyCaptcha(result);
        return checkNeedCaptchaDataVo;
    }

    public ResponseModel getCaptcha(CaptchaVO captchaVO) {
        return captchaHandle.getCaptcha(captchaVO);
    }

    public ResponseModel verifyCaptcha(final CaptchaVO captchaVO) {
        return captchaHandle.checkCaptcha(captchaVO);
    }
}
