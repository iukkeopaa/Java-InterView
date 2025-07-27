## 业务场景

```java

 */
public class RepeatExecuteLimitConstants {

    /**
     * 消费者 API 数据消息业务场景的常量名称。
     * 用于标识与消费者 API 数据消息相关的防重复幂等操作。
     */
    public static final String CONSUMER_API_DATA_MESSAGE = "consumer_api_data_message";

    /**
     * 创建节目订单业务场景的常量名称。
     * 用于标识创建节目订单相关的防重复幂等操作。
     */
    public static final String CREATE_PROGRAM_ORDER = "create_program_order";

    /**
     * 取消节目订单业务场景的常量名称。
     * 用于标识取消节目订单相关的防重复幂等操作。
     */
    public final static String CANCEL_PROGRAM_ORDER = "cancel_program_order";

    /**
     * 通过消息队列创建节目订单业务场景的常量名称。
     * 用于标识通过消息队列创建节目订单相关的防重复幂等操作。
     */
    public static final String CREATE_PROGRAM_ORDER_MQ = "create_program_order_mq";

    /**
     * 节目缓存反向操作消息队列业务场景的常量名称。
     * 用于标识节目缓存反向操作消息队列相关的防重复幂等操作。
     * 注意：该字段目前未被使用。
     */
    public static final String PROGRAM_CACHE_REVERSE_MQ = "program_cache_reverse_mq";

    /**
     * 支付或取消节目订单业务场景的常量名称。
     * 用于标识支付或取消节目订单相关的防重复幂等操作。
     */
    public final static String PAY_OR_CANCEL_PROGRAM_ORDER = "pay_or_cancel_program_order";

    /**
     * 减少剩余数量业务场景的常量名称。
     * 用于标识减少剩余数量相关的防重复幂等操作。
     */
    public final static String REDUCE_REMAIN_NUMBER = "reduce_remain_number";
}
```

## 锁的标记前缀   public static final String PREFIX_NAME = "REPEAT_EXECUTE_LIMIT";