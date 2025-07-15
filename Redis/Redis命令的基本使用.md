## Redis实现缓存，分布式锁，共享seesion的命令操作

```redis
SET user:profile:{userid} {JSON数据} EX 3600 # 存储?户资料，并设置1?时过期
```

```redis
SET lock:resource_name {random_value} EX 10 NX # 获取锁，10秒后?动释放
```

```redis
SET session:{sessionid} {session_data} EX 1800 # 存储?户会话，30分钟过期

```


## sadd 命令的时间复杂度是多少

sadd支持一次添加多个元素，返回值为实际添加成功的元素数量，时间复杂度为N

## 单线程的Redis QPS 能到多少

一个普通服务器的 Redis 实例通常可以达到每秒十万左右的 QPS。