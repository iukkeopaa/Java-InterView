package Singleton;

/**
 * @Description: 双重锁
 * @Author: wjh
 * @Date: 2025/7/19 15:01
 */
public class Singleton3 {

    private volatile static Singleton3 uniqueInstance;
    // 私有化构造方法
    private Singleton3(){}
    public static Singleton3 getInstance(){
        //先判断对象是否已经实例过，没有实例化过才进入加锁代码
        if(uniqueInstance == null){
            //类对象加锁
            synchronized (Singleton3.class){
                if(uniqueInstance == null){
                    uniqueInstance = new Singleton3();
                }
            }
        }
        return uniqueInstance;
    }
}

/*
* ### **为什么需要 `volatile`？**

`uniqueInstance = new Singleton3();` 这行代码在 JVM 中实际上会分解为三个步骤：



1. **分配内存空间**
2. **初始化对象**
3. **将引用指向内存空间**



由于 JVM 的**指令重排序优化**，步骤 2 和 3 可能会被颠倒，执行顺序变为：



1. 分配内存空间
2. **将引用指向内存空间（此时对象尚未初始化）**
3. 初始化对象



当线程 A 执行完步骤 3（引用已非空）但尚未执行步骤 2 时，若线程 B 恰好进入第一个 `if (uniqueInstance == null)` 检查，会误以为对象已初始化，直接返回未完成初始化的 `uniqueInstance`，导致 B 线程访问到不完整的对象，引发潜在错误。

### **`volatile` 的作用**

`volatile` 关键字的核心作用是：



1. **禁止指令重排序**：确保 `new Singleton3()` 的三个步骤按顺序执行（1→2→3），不会颠倒。
2. **保证可见性**：当一个线程修改了 `volatile` 变量，其他线程能立即看到最新值。



因此，加上 `volatile` 后：



- 线程 A 必须完成对象的完整初始化（步骤 1→2→3）后，引用 `uniqueInstance` 才会对其他线程可见。
- 线程 B 不会在对象未初始化时就误认为 `uniqueInstance` 已非空，从而避免获取到不完整的对象。

### **总结**

`volatile` 关键字在双重检查锁定的单例模式中是**必要的**，它通过禁止指令重排序和保证可见性，确保多线程环境下单例对象的正确初始化和访问。若不加 `volatile`，可能会出现线程获取到未完全初始化对象的情况，导致程序崩溃或产生不可预期的结果
* */