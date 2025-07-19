package Singleton;

/**
 * @Description: ˫����
 * @Author: wjh
 * @Date: 2025/7/19 15:01
 */
public class Singleton3 {

    private volatile static Singleton3 uniqueInstance;
    // ˽�л����췽��
    private Singleton3(){}
    public static Singleton3 getInstance(){
        //���ж϶����Ƿ��Ѿ�ʵ������û��ʵ�������Ž����������
        if(uniqueInstance == null){
            //��������
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
* ### **Ϊʲô��Ҫ `volatile`��**

`uniqueInstance = new Singleton3();` ���д����� JVM ��ʵ���ϻ�ֽ�Ϊ�������裺



1. **�����ڴ�ռ�**
2. **��ʼ������**
3. **������ָ���ڴ�ռ�**



���� JVM ��**ָ���������Ż�**������ 2 �� 3 ���ܻᱻ�ߵ���ִ��˳���Ϊ��



1. �����ڴ�ռ�
2. **������ָ���ڴ�ռ䣨��ʱ������δ��ʼ����**
3. ��ʼ������



���߳� A ִ���경�� 3�������ѷǿգ�����δִ�в��� 2 ʱ�����߳� B ǡ�ý����һ�� `if (uniqueInstance == null)` ��飬������Ϊ�����ѳ�ʼ����ֱ�ӷ���δ��ɳ�ʼ���� `uniqueInstance`������ B �̷߳��ʵ��������Ķ�������Ǳ�ڴ���

### **`volatile` ������**

`volatile` �ؼ��ֵĺ��������ǣ�



1. **��ָֹ��������**��ȷ�� `new Singleton3()` ���������谴˳��ִ�У�1��2��3��������ߵ���
2. **��֤�ɼ���**����һ���߳��޸��� `volatile` �����������߳���������������ֵ��



��ˣ����� `volatile` ��



- �߳� A ������ɶ����������ʼ�������� 1��2��3�������� `uniqueInstance` �Ż�������߳̿ɼ���
- �߳� B �����ڶ���δ��ʼ��ʱ������Ϊ `uniqueInstance` �ѷǿգ��Ӷ������ȡ���������Ķ���

### **�ܽ�**

`volatile` �ؼ�����˫�ؼ�������ĵ���ģʽ����**��Ҫ��**����ͨ����ָֹ��������ͱ�֤�ɼ��ԣ�ȷ�����̻߳����µ����������ȷ��ʼ���ͷ��ʡ������� `volatile`�����ܻ�����̻߳�ȡ��δ��ȫ��ʼ���������������³���������������Ԥ�ڵĽ��
* */