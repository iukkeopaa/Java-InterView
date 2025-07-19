package Singleton;

/**
 * @Description: 静态内部类
 * @Author: wjh
 * @Date: 2025/7/19 14:58
 */
public class Singleton2 {

    private static class SingletonHolder {
        private static final Singleton2 INSTANCE = new Singleton2();
    }
    private Singleton2(){}
    public static Singleton2 getInstance(){
        return SingletonHolder.INSTANCE;
    }
}


//当外部类 Singleton 被加载的时候，并不会创建静态内部类 SingletonInner 的实例对象。只
//有当调? getInstance() ?法时， SingletonInner 才会被加载，这个时候才会创建单例对象
//INSTANCE 。 INSTANCE 的唯?性、创建过程的线程安全性，都由 JVM 来保证。

//这种?式同样简单?效，?需加锁，线程安全，并且?持延时加载。