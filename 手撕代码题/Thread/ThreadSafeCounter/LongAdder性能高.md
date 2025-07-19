### **1. AtomicLong 的瓶颈：单一 CAS 竞争**

`AtomicLong`使用**单个 volatile 变量**存储值，并通过 CAS（Compare-And-Swap）操作实现原子性。在高并发下，大量线程频繁对同一个变量进行 CAS 操作时，会出现以下问题：



- **总线风暴**：多个 CPU 核心频繁尝试修改同一内存地址，导致缓存失效和总线带宽竞争。
- **自旋消耗**：CAS 失败的线程会不断自旋重试，消耗 CPU 资源。






### **2. LongAdder 的优化：分段存储与聚合**

`LongAdder`采用**分散热点**的思路，将单一变量的竞争分散到多个`Cell`中：



- **Cell 数组**：内部维护一个`Cell`数组，每个`Cell`类似一个独立的`AtomicLong`。
- **Thread-local 绑定**：线程首次访问时，会通过哈希值映射到一个`Cell`进行操作，减少冲突。
- **聚合结果**：最终结果通过累加所有`Cell`的值和基础值（未冲突时直接使用）得到。






### **3. 核心优势**

#### **（1）减少 CAS 竞争**

当多个线程同时更新时，它们会被分配到不同的`Cell`，从而避免对同一变量的竞争。例如：



- 线程 A → Cell [0]
- 线程 B → Cell [1]
- 线程 C → Cell [2]

#### **（2）减少缓存行伪共享（False Sharing）**

`Cell`使用`@sun.misc.Contended`注解填充缓存行，避免多个`Cell`被放入同一缓存行，减少缓存失效。

#### **（3）自适应扩展**

当发现多个线程映射到同一个`Cell`（哈希冲突）时，`LongAdder`会动态扩容`Cell`数组（最大为 CPU 核心数），进一步分散竞争。

### **4. 适用场景对比**

| 场景           | AtomicLong               | LongAdder                      |
| -------------- | ------------------------ | ------------------------------ |
| **低并发更新** | 简单高效，直接操作变量   | 额外开销（数组、哈希）         |
| **高并发更新** | 大量竞争导致性能下降     | 分段设计显著提升吞吐量         |
| **读多写少**   | 直接读取变量，无额外开销 | 需要聚合所有 Cell，稍慢        |
| **统计求和**   | 直接获取值               | 需要调用`sum()`或`longValue()` |

### **5. 源码关键逻辑**








```java
// LongAdder.increment() 简化逻辑
public void increment() {
    Cell[] as; long b, v; int m; Cell a;
    // 无竞争时，直接CAS更新base
    if ((as = cells) == null || !casBase(b = base, b + 1L)) {
        boolean uncontended = true;
        // 有竞争时：
        // 1. 尝试获取线程对应的Cell并更新
        // 2. 若Cell不存在或CAS失败，调用longAccumulate
        if (as == null || (m = as.length - 1) < 0 ||
            (a = as[getProbe() & m]) == null ||
            !(uncontended = a.cas(v = a.value, v + 1L)))
            longAccumulate(1L, null, uncontended);
    }
}

// sum() 方法：聚合所有Cell的值和base
public long sum() {
    Cell[] as = cells; Cell a;
    long sum = base;
    if (as != null) {
        for (int i = 0; i < as.length; ++i) {
            if ((a = as[i]) != null)
                sum += a.value;
        }
    }
    return sum;
}
```

### **总结**

`LongAdder`通过**空间换时间**的策略，将单一变量的竞争分散到多个`Cell`中，大幅减少了高并发下的 CAS 冲突和线程自旋，从而提升吞吐量。但如果业务场景中写操作较少，`AtomicLong`的简单设计反而更高效。