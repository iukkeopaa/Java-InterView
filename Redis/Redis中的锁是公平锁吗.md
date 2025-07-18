### һ�����ĸ����ƽ�� vs �ǹ�ƽ��

- **��ƽ��**������߳� / �ͻ��˻�ȡ����˳���ϸ���ѭ ���������Ȼ�ȡ�� ��ԭ�������Ŷӣ���������ֺ�����Ŀͻ��� ����ӡ� ��ȡ���������
- **�ǹ�ƽ��**�����ͷź����еȴ��Ŀͻ���������������ܳ��ֺ�����Ŀͻ����Ȼ�ȡ��������ӡ���������ͨ�����ߣ������ܵ���ĳЩ�ͻ��˳��ڼ�����

### ����Redis �ֲ�ʽ����Ĭ��ʵ�֣��ǹ�ƽ��

����� Redis �ֲ�ʽ��ʵ�ֻ���`SET`�����`NX`��`EX`�����������磺



bash











```bash
# ���Ի�ȡ������Ϊlock_key��ֵΪΨһ��ʶ����UUID�������ڼ�������ʱ���ã�NX��������ʱ��10�루EX��
SET lock_key <uuid> NX EX 10
```





![img](data:image/png;base64,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)



����ʵ�ֵ�**�ǹ�ƽ��������**��



1. �����ͷţ�`DEL lock_key`�������еȴ��Ŀͻ��˻����·���`SET`���������Redis ���̴߳�������ʱ��˭��`SET`�����ȵ����������˭�ͻ�ɹ���ȡ����
2. ���������ӳ١��ͻ��˴����ٶȲ���ȣ���������Ŀͻ��˿��������������ȵ��� Redis������ӡ� ��ȡ�����ƻ� ���������Ȼ�ȡ�� ��˳��

### �������ʵ�� Redis ��ƽ����

Ҫʵ�ֹ�ƽ������Ҫ�������ά�� ���ȴ����С���ȷ��������˳�������������ʵ����**Redisson ��`FairLock`**�������߼��ǣ�



1. �ͻ���������ʱ������һ��**���򼯺ϣ�Sorted Set��** ������Լ��ı�ʶ��������ʱ���Ϊ��������֤˳�򣩡�
2. ֻ�ж�����������λ�Ŀͻ��ˣ����ܳ��Ի�ȡ���������ͻ��������ǰ��ͻ��˵��ͷ��¼���ͨ�� Redis ��`Pub/Sub`���ƣ���
3. ���ͷ�ʱ���Ƴ���λ�ͻ��˵ı�ʶ����֪ͨ��һλ�ͻ��˳��Ի�ȡ����



���ַ�ʽ�ϸ�֤�� ���������Ȼ�ȡ���������������� Redis ������ά�����С��������ģ��������Ե��ڷǹ�ƽ����

### �ġ���ƽ�� vs �ǹ�ƽ����ȡ��

| ά��       | �ǹ�ƽ����Ĭ�ϣ�         | ��ƽ������ Redisson FairLock�� |
| ---------- | ------------------------ | ------------------------------ |
| ����       | ���ߣ��޶���ά��������   | �Եͣ���ά�����кͷ������ģ�   |
| ��ƽ��     | �ޣ����ܲ�ӣ�           | �У��ϸ�˳��               |
| ʵ�ָ��Ӷ� | �򵥣�����`SET`���    | ���ӣ������ + �������ƣ�      |
| ���ó���   | ׷������ܣ�������ڼ��� | ���ϸ�˳����ֲ�ʽ������ȣ� |

### �塢�ܽ�

- **Ĭ������£�Redis �ֲ�ʽ���Ƿǹ�ƽ��**������`SET NX EX`�ļ�ʵ�ֲ���֤˳����������� Redis ��ʱ��������
- **��ƽ�������ʵ��**��ͨ��ά���ȴ����У��� Redisson ��`FairLock`��ȷ��˳�򣬵��������������ܡ�
- ѡ��ʱ��Ȩ�⣺��������������÷ǹ�ƽ�������ܸ��ߣ������ڱ��뱣֤˳��ʱ���ù�ƽ����