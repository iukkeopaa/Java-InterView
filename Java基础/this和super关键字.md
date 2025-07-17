�� Java �У�this��super�ؼ������ض�����Ҫ���ã�

**һ��this �ؼ��ֵ�����**

1. ���õ�ǰ����

    - �ڳ�Ա�����У�this������ø÷����ĵ�ǰ���󡣿���ͨ��this�����ʵ�ǰ����ĳ�Ա�����ͳ�Ա���������磺

   ```
   class Person {
       private String name;
       private int age;
   
       public Person(String name, int age) {
           this.name = name;
           this.age = age;
       }
   
       public void displayInfo() {
           System.out.println("Name: " + this.name + ", Age: " + this.age);
       }
   }
   ```



�ڹ��췽���У�this�������ֳ�Ա�����Ͳ�������������������ͻ��

2. ��Ϊ�����Ĳ������ݵ�ǰ����

    - ���Խ���ǰ������Ϊ�������ݸ��������������磺

   ```
   class SomeClass {
       void someMethod(SomeClass obj) {
           // ������
       }
   
       void anotherMethod() {
           someMethod(this);
       }
   }
   ```



3. ���ñ�����������췽����

    - ��һ����Ĺ��췽���У�����ʹ��this������ͬһ���е��������췽����������ڹ��췽���ĵ�һ�С����磺

   ```
   class Student {
       private String name;
       private int age;
   
       public Student() {
           this("Unknown", 0);
       }
   
       public Student(String name, int age) {
           this.name = name;
           this.age = age;
       }
   }
   ```



**����super �ؼ��ֵ�����**

1. ���ø���Ĺ��췽����

    - ������Ĺ��췽���У�����ʹ��super�����ø���Ĺ��췽��������������๹�췽���ĵ�һ�С���������ȷ���ڴ����������ʱ������ȷ�س�ʼ�����ಿ�֡����磺

   ```
   class Parent {
       public Parent() {
           System.out.println("Parent constructor.");
       }
   }
   
   class Child extends Parent {
       public Child() {
           super();
           System.out.println("Child constructor.");
       }
   }
   ```



2. ���ʸ���ĳ�Ա�����ͷ�����

    - �������ж������븸��ͬ���ĳ�Ա�����򷽷�ʱ������ʹ��super����ȷ�ط��ʸ����еĳ�Ա�����򷽷������磺

   ```
   class Parent {
       int num = 10;
   
       void show() {
           System.out.println("Parent method.");
       }
   }
   
   class Child extends Parent {
       int num = 20;
   
       void display() {
           System.out.println("Parent's num: " + super.num);
           System.out.println("Child's num: " + this.num);
           super.show();
       }
   }
   ```