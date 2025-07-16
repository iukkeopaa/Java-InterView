## Java中的ArrayList的扩容机制

默认情况下，ArrayList的初始容量为10.当发生扩容时，ArrayList会创建一个新的数组，其容量为原数组的1.5倍，然后讲原数组中的元素复制到新数组中，复制的过程通过Arrays.copyof（）实现

### 初始容量的区别

1. 1.7的时候是在调用构造函数的时候就开辟空间
2. 1.8是在调用add方法的时候，开辟空间，节约内存，只有真正使用的时候才创建数组。


## ConcurrentHashMap和Hashtable的区别是什么

二者都是线程安全的，但是前者在多线程的情况下性能由于后者。

ConcurrentHashMap采用了分段式锁，并且实现了读写分离，只对写操作上锁（采用CAS+synchronized）,对于node节点为null的时候直接使用CAS写入值，如果不为null则使用synchronized锁，锁的颗粒度更细，仅仅锁住需要操作的地方，而非全表。读操作不上锁，可以并发读，不会阻塞，效率更高。

## Java中的HashMap的扩容机制是什么


### 1. 初始容量与负载因子

- **初始容量**：`HashMap` 底层数组的初始大小为 16，这个数值可以通过构造函数进行调整。
- **负载因子**：默认值是 0.75，它是用来衡量数组填满程度的一个指标。

### 2. 扩容触发条件

当 `HashMap` 中的元素数量超过阈值（即 `容量 × 负载因子`）时，就会触发扩容操作。例如，默认情况下，当元素数量超过 `16 × 0.75 = 12` 时，`HashMap` 就会进行扩容。

### 3. 扩容过程

- **容量翻倍**：扩容时，数组的大小会变为原来的 2 倍。比如，原来容量是 16，扩容后就变成 32。
- **重新哈希**：扩容后，所有元素都需要重新计算哈希值，然后根据新的哈希值重新分配到新数组的相应位置，这个过程被称为 **rehash**。

### 4. JDK 8 的优化

JDK 8 对扩容机制进行了优化，主要体现在链表处理上：



- **高低位链表**：在重新哈希时，会将链表分为高位和低位两部分。如果元素的哈希值与原容量进行按位与运算结果为 0，就放入低位链表，否则放入高位链表。
- **索引计算优化**：元素在新数组中的位置要么和原来相同（低位链表），要么是原来的位置加上原容量（高位链表）。这种优化减少了重新计算哈希值的开销。

## Java中Hashmap的扩容为什么是2的n次方

1. 哈希值与数组索引的映射优化
   HashMap 使用 (n - 1) & hash 来计算元素在数组中的索引位置，这里的 n 代表数组的容量。当 n 是 2 的 n 次方时，(n - 1) 会形成一个全为 1 的二进制掩码，例如：

当 n = 16 时，n - 1 = 15，二进制表示为 0000 1111
当 n = 32 时，n - 1 = 31，二进制表示为 0001 1111

进行 按位与运算（&） 时，这样的掩码能让哈希值的低位充分参与到索引计算中，从而 减少哈希碰撞。相反，如果 n 不是 2 的 n 次方，比如 n = 15，那么 n - 1 = 14，二进制是 0000 1110，此时进行按位与运算时，最低位始终为 0，这就使得索引只能是偶数，奇数位置永远不会被使用，极大地增加了哈希碰撞的概率。
2. 扩容时的高效迁移
   JDK 8 对扩容机制做了优化，当容量是 2 的 n 次方时，扩容后的元素迁移可以通过 高低位链表 高效完成：

假设原容量 oldCap = 16，扩容后 newCap = 32。对于某个元素，其哈希值 hash 的二进制表示为 ... xxxx xxxx。
在计算索引时，hash & (oldCap - 1) 是看低 4 位；而 hash & (newCap - 1) 是看低 5 位。
关键优化点：如果 hash & oldCap == 0，那么该元素在新数组中的索引和原来相同；如果 hash & oldCap != 0，新索引则是 原索引 + oldCap。

例如：

若 hash = 0001 0101，oldCap = 16，原索引为 0001 0101 & 0000 1111 = 0101 (5)。
扩容后，newCap = 32，计算 hash & 16，即 0001 0101 & 0001 0000 = 0001 0000 ≠ 0，所以新索引为 5 + 16 = 21。

这种优化让重新哈希的过程无需重新计算每一个元素的哈希值，时间复杂度从 O (n) 降低到了接近 O (1)。
3. 与哈希函数的协同工作
   HashMap 的哈希函数为：

java
static final int hash(Object key) {
int h;
return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}

该函数通过将哈希值的高位和低位进行异或运算，增强了低位的随机性。而当容量是 2 的 n 次方时，能更好地配合这种哈希函数，进一步减少哈希碰撞。
4. 历史原因与兼容性
   从 JDK 1.2 引入 HashMap 开始，容量为 2 的 n 次方这个设计就已经存在。后续为了保持兼容性，同时也因为这种设计确实高效，所以一直沿用到了现在。
   总结
   HashMap 扩容采用 2 的 n 次方主要有以下好处：

索引计算更高效：可以用 (n - 1) & hash 替代取模运算 hash % n，在计算机中，按位与运算的性能要远高于取模运算。
减少哈希碰撞：使哈希值的低位能充分参与到索引计算中。
扩容迁移更高效：JDK 8 借助高低位链表，能快速地将元素分配到新数组中。

## HashMao的默认负载因子为什么是0.75

负载因子用来衡量HashMap的满载程度，公式为：实际存储的元素数量/容量

- 较低的负载因子（如0.5）会导致HashMap需要频繁扩容，空间利用率较低，不过因为冲突少，查找效率更高，同时扩容操作频繁会增加rehashing的开销
- 较高的负载因子（如1.0）会减少扩容的次数，空间利用率高，但是会增加哈希冲突的概率，降低查找的效率
- 所以0.75是在时间和空间上取得更好的平衡
- 高并发读取的场景下，使用较低的负载因子，以来减少哈希冲突，提高读取性能
- 内存受到限制，可以提高负载因子以减少扩容次数和内存消耗，但是会减低写入和查询的性能

## 为什么HashMap的数组要大于等于64才装换成红黑树

- 避免频繁的树化
- 减少内存占用：红黑树比链表需要更多的内存，尤其是在节点较少的情况下，红黑树的额外指针和结构占用更大

### 不能抛弃链表，直接使用红黑树吗？

- 红黑树的节点带下是普通的节点的两倍，所以为了节省内存空间不会直接使用红黑树
- 使用超过8的时候转换的原因是和泊松分布有关：在0.75的负载因子的情况下，冲突节点长度为8的概率很低，并且红黑树比较耗内存。并且链表在长度不长的时候遍历还是很快的。
这里的考虑最多的是时间和空间之间的平衡，红黑树占用空间内存大，所以节点少就不用红黑树

### 为什么节点小于等于6要从红黑树转换成链表

因为要留个缓冲余地，举个例子：一个接待你反复添加，从8-9链表成为了红黑树，又删除了从9-8又从红黑树成为了链表。所以要余一点，并且树化和反树化是需要开销的。


## WeakHashMap

### 1. **弱引用的特性**

在 Java 的垃圾回收机制里，弱引用是最弱的一种引用类型。当一个对象仅仅被弱引用指向，没有其他强引用指向它时，这个对象就会在下一次垃圾回收时被回收，不管当前内存是否充足。

### 2. **WeakHashMap 的核心特性**

- **自动移除键值对**：一旦键所引用的对象被垃圾回收，`WeakHashMap` 会在下次访问（如 `get()`、`size()` 等操作）时自动移除对应的键值对。
- **适合缓存场景**：非常适合实现缓存，因为当系统内存不足时，缓存中的数据会自动被清理，避免内存溢出。
- **线程不安全**：和 `HashMap` 一样，`WeakHashMap` 不是线程安全的。如果需要在多线程环境下使用，可以通过 `Collections.synchronizedMap` 进行包装。

### 3. **与 HashMap 的对比**

| **特性**         | **WeakHashMap**                | **HashMap**                              |
| ---------------- | ------------------------------ | ---------------------------------------- |
| **键的引用类型** | 弱引用                         | 强引用                                   |
| **垃圾回收行为** | 键被回收时，对应键值对自动移除 | 即使键所引用的对象被回收，键值对仍会保留 |
| **内存占用**     | 较低，适合缓存                 | 较高，取决于存储的元素数量               |
| **线程安全性**   | 不安全                         | 不安全                                   |
| **适用场景**     | 缓存、临时映射                 | 常规键值对存储                           |

### 4. **实际的运用**

- 动态代理缓存：Java的冬天代理生成过程中可能会生成多尔戈代理类实例，而这些实例不再使用的时候会浪费内存。
- Event Listener管理


## ConcurrentHashMap的get方法是否需要加锁

不需要加锁

### **1. `get`方法的无锁实现**

`ConcurrentHashMap`的`get`方法通过**volatile 语义**和**CAS 操作**保证了线程安全，无需显式加锁：



- **volatile 数组**：`ConcurrentHashMap`的底层数组被声明为`volatile`，确保数组引用的可见性。
- **volatile 节点**：数组中的每个节点（Node）的`value`和`next`字段也被声明为`volatile`，保证读取操作能立即看到其他线程对节点的修改。



以下是简化的`get`方法源码：



java











```java
public V get(Object key) {
    Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
    int h = spread(key.hashCode()); // 计算哈希值
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (e = tabAt(tab, (n - 1) & h)) != null) { // 通过volatile读获取数组元素
        if ((eh = e.hash) == h) { // 头节点匹配
            if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                return e.val; // 直接返回volatile值
        }
        else if (eh < 0) // 红黑树或转发节点
            return (p = e.find(h, key)) != null ? p.val : null;
        while ((e = e.next) != null) { // 链表遍历
            if (e.hash == h &&
                ((ek = e.key) == key || (ek != null && key.equals(ek))))
                return e.val;
        }
    }
    return null;
}

// 通过Unsafe的getObjectVolatile保证volatile读语义
static final <K,V> Node<K,V> tabAt(Node<K,V>[] tab, int i) {
    return (Node<K,V>)U.getObjectVolatile(tab, ((long)i << ASHIFT) + ABASE);
}
```






### **2. 线程安全的保证**

- **可见性**：通过`volatile`关键字，`get`方法能立即看到其他线程对`ConcurrentHashMap`的写操作（如`put`、`remove`）。
- **原子性**：`get`操作是原子的，读取过程中不会看到中间状态。即使多个线程同时修改 Map，`get`也不会抛出`ConcurrentModificationException`。
- **无锁设计**：`get`方法不使用锁（synchronized 或 Lock），因此不会阻塞其他线程的读写操作。

### **3. 特殊情况的处理**

- **红黑树节点**：当链表长度超过阈值（8）时，`ConcurrentHashMap`会将链表转换为红黑树。`get`方法在遍历红黑树时可能会使用`synchronized`锁定树节点，但这种情况非常罕见（仅在树结构调整时）。
- **扩容期间**：当 Map 正在扩容时，`get`方法可能会访问到`ForwardingNode`，此时会通过`ForwardingNode`的指针在新数组中查找元素，整个过程依然是无锁的。

### **4. 为什么不需要加锁？**

- **读写分离**：`ConcurrentHashMap`采用分段锁（JDK 7）或 CAS+synchronized（JDK 8）实现写操作的并发控制，而读操作完全无锁。
- **内存模型保证**：Java 内存模型（JMM）确保`volatile`变量的写操作对后续读操作可见，因此`get`方法无需加锁即可读取最新值。
- **性能优化**：无锁的`get`方法极大提升了并发性能，尤其在读多写少的场景下优势明显。

### **5. 与其他 Map 的对比**

| **Map 实现**        | **get 方法是否加锁** | **线程安全机制**                |
| ------------------- | -------------------- | ------------------------------- |
| `HashMap`           | 否（非线程安全）     | 无                              |
| `Hashtable`         | 是（全局锁）         | `synchronized`方法              |
| `ConcurrentHashMap` | 否                   | volatile+CAS+synchronized（写） |

### **总结**

**`ConcurrentHashMap`的`get`方法不需要加锁**，因为它通过`volatile`和 CAS 操作保证了线程安全和可见性，同时避免了锁带来的性能开销。这使得`ConcurrentHashMap`在读多写少的场景下表现出极高的效率


## Java中unsafe类

### 1. **获取 Unsafe 实例**

`Unsafe` 的构造函数是私有的，并且实例的获取方式也受到限制：



java











```java
import sun.misc.Unsafe;
import java.lang.reflect.Field;

public class UnsafeExample {
    private static final Unsafe unsafe;
    
    static {
        try {
            // 通过反射获取Unsafe的theUnsafe实例
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException("无法获取Unsafe实例", e);
        }
    }
}
```

### 2. **核心功能与应用场景**

#### 2.1 **内存操作**

- **直接内存分配**：能够绕过 Java 堆，直接在本地内存中分配和释放空间，适用于开发需要处理大量数据的高性能组件，如 NIO 的 DirectByteBuffer。



java











```java
// 分配内存
long address = unsafe.allocateMemory(1024); // 分配1024字节
// 写入数据
unsafe.putLong(address, 0, 12345L);
// 读取数据
long value = unsafe.getLong(address, 0);
// 释放内存
unsafe.freeMemory(address);
```



- **内存复制**：可用于实现高效的内存块复制操作。



java











```java
unsafe.copyMemory(srcAddress, destAddress, size);
```

#### 2.2 **CAS 操作（Compare-and-Swap）**

这是无锁并发编程的基础，`Unsafe` 提供了原子更新操作：



java











```java
// AtomicInteger的底层实现依赖于Unsafe
public final int getAndAddInt(Object o, long offset, int delta) {
    int v;
    do {
        v = getIntVolatile(o, offset);
    } while (!compareAndSwapInt(o, offset, v, v + delta));
    return v;
}
```

#### 2.3 **线程调度**

- **park/unpark**：可替代 `Object.wait()` 和 `Thread.suspend()`，用于更高效地实现锁和并发工具。



java











```java
// 阻塞当前线程
unsafe.park(false, 0L);
// 唤醒指定线程
unsafe.unpark(thread);
```

#### 2.4 **类和实例操作**

- **实例化对象**：能够绕过构造函数创建对象，这在序列化或者代理类生成时很有用。



java











```java
// 不调用构造函数创建对象
MyClass obj = (MyClass) unsafe.allocateInstance(MyClass.class);
```



- **修改私有字段**：可以直接访问和修改对象的私有字段。



java











```java
// 获取字段的内存偏移量
long offset = unsafe.objectFieldOffset(MyClass.class.getDeclaredField("privateField"));
// 修改私有字段
unsafe.putInt(obj, offset, 123);
```

#### 2.5 **内存屏障**

在 JDK 8 中引入，用于保证内存操作的顺序性和可见性：



java











```java
// 保证读操作不会重排序
unsafe.loadFence();
// 保证写操作不会重排序
unsafe.storeFence();
// 保证所有内存操作不会重排序
unsafe.fullFence();
```

### 3. **风险与注意事项**

- **安全风险**：使用 `Unsafe` 可能会破坏 Java 的安全机制，比如访问未授权的内存地址，从而导致 JVM 崩溃。
- **可移植性差**：`Unsafe` 是 Sun/Oracle JDK 的私有类，在其他 JVM 实现（如 OpenJ9）中可能不可用，或者行为存在差异。
- **内存管理风险**：直接内存分配需要手动释放，否则会造成内存泄漏，而且这种泄漏无法被 JVM 的垃圾回收机制处理。
- **版本兼容性风险**：`Unsafe` 的方法在不同的 JDK 版本中可能有不同的实现，甚至某些方法会被移除。

### 4. **替代方案**

在大多数情况下，应该优先使用 Java 提供的安全 API，而不是 `Unsafe`：



- **原子操作**：可以使用 `java.util.concurrent.atomic` 包中的原子类。
- **并发工具**：`java.util.concurrent` 包提供了各种高性能的并发工具。
- **直接内存**：对于直接内存操作，可以使用 `java.nio.DirectByteBuffer`。
- **反射**：如果只是需要访问私有字段，优先使用 Java 的反射 API。

### 5. **典型应用场景**

- **高性能框架**：在一些高性能框架中会用到，例如 Netty、Hadoop、Kafka 等。
- **并发工具**：Java 的并发包（`java.util.concurrent`）底层依赖 `Unsafe` 实现原子操作和锁机制。
- **序列化框架**：某些序列化框架会使用 `Unsafe` 来提高对象实例化的效率。
- **内存数据库**：像 H2 数据库就使用 `Unsafe` 进行直接内存访问。

## 如果在扩容的时候，我们正好查询了当前正在移动的桶怎么办，如何保证线程安全

### **1. 扩容时的并发处理机制**

`ConcurrentHashMap`在扩容时采用**增量式迁移**策略，即每次只迁移一个桶（链表或红黑树），而不是一次性迁移整个哈希表。在迁移过程中，原数组和新数组会并存，查询操作可能会访问到正在迁移的桶。此时，关键机制是**ForwardingNode**：



- **ForwardingNode**：是一种特殊的节点，当某个桶迁移完成后，会在原数组的位置放置一个`ForwardingNode`，指向新数组的对应位置。
- **volatile 语义**：数组引用和节点的`next`指针都被声明为`volatile`，确保查询线程能及时看到迁移后的新数组。

### **2. 查询操作的处理流程**

当查询操作访问到正在迁移的桶时，会按照以下逻辑处理：



1. **检查当前桶是否已迁移**：如果该位置是`ForwardingNode`，说明该桶已迁移到新数组。
2. **通过 ForwardingNode 访问新数组**：查询线程会直接通过`ForwardingNode`的指针访问新数组的对应位置，继续查找元素。
3. **未迁移的桶正常查询**：如果桶尚未迁移，查询线程会在原数组上正常查找元素。



以下是简化的`get`方法源码，展示了对`ForwardingNode`的处理：



java











```java
public V get(Object key) {
    Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
    int h = spread(key.hashCode());
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (e = tabAt(tab, (n - 1) & h)) != null) {
        if ((eh = e.hash) == h) {
            // ... 正常查找头节点 ...
        }
        else if (eh < 0) {
            // 处理特殊节点：ForwardingNode、TreeBin等
            return (p = e.find(h, key)) != null ? p.val : null;
        }
        // ... 链表或红黑树遍历 ...
    }
    return null;
}
```



在`ForwardingNode`的`find`方法中，会直接在新数组上进行查找：



java











```java
static final class ForwardingNode<K,V> extends Node<K,V> {
    final Node<K,V>[] nextTable;
    ForwardingNode(Node<K,V>[] tab) {
        super(MOVED, null, null, null);
        this.nextTable = tab;
    }
    
    Node<K,V> find(int h, Object k) {
        // 在新数组上继续查找
        Node<K,V>[] tab = nextTable;
        // ... 查找逻辑 ...
    }
}
```

### **3. 线程安全的保证**

- **无锁读操作**：查询操作不需要加锁，通过`volatile`保证可见性。即使在扩容期间，查询线程也能正确访问到最新的数据（原数组或新数组）。
- **原子性迁移**：迁移操作是原子的，每个桶只会被一个线程迁移一次。迁移完成后，该位置会被设置为`ForwardingNode`，确保后续查询能正确路由到新数组。
- **写操作的协调**：在扩容期间，如果其他线程需要对正在迁移的桶进行写操作（如`put`、`remove`），也会先帮助完成迁移，再执行操作。

### **4. 示例场景**

假设线程 A 正在扩容，将桶 16 迁移到新数组的桶 16 和桶 32：



1. **迁移前**：查询线程 B 访问桶 16，直接在原数组上查找。
2. **迁移中**：线程 A 将桶 16 的元素拆分到新数组的桶 16 和桶 32，并在原数组桶 16 处放置`ForwardingNode`。
3. **迁移后**：查询线程 B 再次访问桶 16 时，遇到`ForwardingNode`，通过它访问新数组的桶 16 或桶 32。

### **5. 与其他并发 Map 的对比**

| **Map 实现**                  | **扩容时查询的处理方式**                         |
| ----------------------------- | ------------------------------------------------ |
| `ConcurrentHashMap`           | 使用 ForwardingNode 路由到新数组，无锁且线程安全 |
| `Collections.synchronizedMap` | 全局锁阻塞所有读写操作，直到扩容完成             |
| `HashMap`                     | 扩容时可能抛出`ConcurrentModificationException`  |

### **总结**

`ConcurrentHashMap`通过**ForwardingNode**和**volatile 语义**确保了在扩容期间查询操作的线程安全：



- **无锁设计**：查询操作不需要等待扩容完成，直接通过`ForwardingNode`访问新数组。
- **高效协作**：扩容线程和查询线程可以并发工作，通过`ForwardingNode`实现平滑过渡。
- **可见性保证**：`volatile`确保查询线程能及时看到扩容后的新数组。



这种设计使得`ConcurrentHashMap`在高并发场景下既能保证线程安全，又能维持出色的性能。

## 为什么ConcurrentHashMap中不允许key或者value为null

- 为了避免二义性和混淆以及潜在的并发问题
- 为了简化处理

### 那为什么HashMap可以

- HashMap的设计初衷是单线程，它有containskey来判断key是否存在
- ConcurrentHashMap不能用containkeys，因为多线程的情况下会有二义性，比如说刚判断完key不存在，然后一个线程插入了这个key