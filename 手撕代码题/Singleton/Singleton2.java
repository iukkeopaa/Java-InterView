package Singleton;

/**
 * @Description: ��̬�ڲ���
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


//���ⲿ�� Singleton �����ص�ʱ�򣬲����ᴴ����̬�ڲ��� SingletonInner ��ʵ������ֻ
//�е���? getInstance() ?��ʱ�� SingletonInner �Żᱻ���أ����ʱ��Żᴴ����������
//INSTANCE �� INSTANCE ��Ψ?�ԡ��������̵��̰߳�ȫ�ԣ����� JVM ����֤��

//����?ʽͬ����?Ч��?��������̰߳�ȫ������?����ʱ���ء�