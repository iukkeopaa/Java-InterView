### **核心特性**

1. **高性能**：通过 `ConcurrentHashMap` 和 `ConcurrentLinkedQueue` 实现无锁操作，利用 `Striped64` 减少线程竞争，吞吐量远超 Guava Cache。
2. **灵活的回收策略**：支持基于容量（size-based）、时间（time-based）和引用（reference-based）的缓存回收。
3. **异步加载**：利用 Java CompletableFuture 实现非阻塞加载，提升异步场景下的性能。
4. **精准的统计**：提供命中率、加载时间等详细统计信息，帮助优化缓存策略。
5. **写入传播**：支持将缓存变更同步到底层数据源（如数据库）。

### **缓存回收策略**

Caffeine 提供三种主要的回收策略：

1. **基于容量的回收（Size-Based Eviction）**
   当缓存条目数超过指定容量时，根据 `W-TinyLFU` 算法淘汰最冷的数据。

 









   ```java
   Cache<String, Object> cache = Caffeine.newBuilder()
       .maximumSize(10_000)  // 最大缓存条目数
       .build();
   ```

2. **基于时间的回收（Time-Based Eviction）**

    - **写入后过期（expireAfterWrite）**：最后一次写入后经过固定时间过期。
    - **访问后过期（expireAfterAccess）**：最后一次读取或写入后经过固定时间过期。
    - **自定义策略（refreshAfterWrite）**：写入后经过固定时间自动刷新（需配合 `CacheLoader`）。










   ```java
   Cache<String, Object> cache = Caffeine.newBuilder()
       .expireAfterWrite(10, TimeUnit.MINUTES)  // 写入后10分钟过期
       .expireAfterAccess(5, TimeUnit.MINUTES)  // 访问后5分钟过期
       .refreshAfterWrite(1, TimeUnit.MINUTES)  // 写入后1分钟自动刷新
       .build(key -> loadFromDatabase(key));  // 需提供CacheLoader
   ```






3. **基于引用的回收（Reference-Based Eviction）**
   使用弱引用（`WeakReferences`）或软引用（`SoftReferences`）让缓存条目能被 JVM 垃圾回收。



### **异步缓存与加载**

Caffeine 支持异步加载数据，适合高并发场景：









```java
AsyncCache<String, Object> asyncCache = Caffeine.newBuilder()
    .expireAfterWrite(10, TimeUnit.MINUTES)
    .buildAsync(key -> loadFromDatabaseAsync(key));  // 返回CompletableFuture

// 使用示例
CompletableFuture<Object> future = asyncCache.get("key");
future.thenAccept(value -> System.out.println("Loaded: " + value));
```








   ```java
   Cache<String, Object> cache = Caffeine.newBuilder()
       .weakKeys()     // 键使用弱引用
       .weakValues()   // 值使用弱引用
       .softValues()   // 值使用软引用（根据JVM内存自动回收）
       .build();
   ```






### **统计与监控**

通过 `recordStats()` 启用统计功能，获取缓存性能指标：









```java
Cache<String, Object> cache = Caffeine.newBuilder()
    .maximumSize(10_000)
    .recordStats()  // 启用统计
    .build();

// 获取统计信息
CacheStats stats = cache.stats();
System.out.println("命中率: " + stats.hitRate());
System.out.println("平均加载时间: " + stats.averageLoadPenalty());
System.out.println("淘汰次数: " + stats.evictionCount());
```

```java

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

public class UserCache {
    private final Cache<Long, User> cache;

    public UserCache() {
        this.cache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .recordStats()
            .build(this::loadUser);
    }

    public User getUser(Long userId) {
        return cache.get(userId);
    }

    private User loadUser(Long userId) {
        // 从数据库或其他数据源加载用户
        return databaseService.getUserById(userId);
    }

    public void invalidateUser(Long userId) {
        cache.invalidate(userId);  // 手动失效缓存
    }
}
```