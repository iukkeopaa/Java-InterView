Full GC �����ĳ���

1��System.gc

2��promotion failed (�������ʧ��,����eden���Ĵ����������S���Ų��£��ֳ���ֱ�ӽ�

����Old���ַŲ��£���ôPromotion Failed,�ᴥ��FullGC)

3��CMS��Concurrent-Mode-Failure

����CMS���չ�������Ҫ��Ϊ�Ĳ�: 1.CMS initial mark 2.CMS Concurrent mark 3.CMS

remark 4.CMS Concurrent sweep����2��gc�߳����û��߳�ͬʱִ�У���ô�û��߳����ɿ�

��ͬʱ���������� �����������϶��޷�����Ԥ���Ŀռ�ͻ����CMS-Mode-Failure�� �л�

ΪSerialOld���߳���mark-sweep-compact��

4��������������ƽ����С�����������ʣ��ռ� ��Ϊ�˱��������������������ʧ�ܣ�

��ʹ��G1,CMS ʱ��FullGC������ʱ�� �� Serial+SerialOld��

��ʹ��ParalOldʱ��FullGC������ʱ���� ParallNew +ParallOld.