### **һ������ѯ Bug �ĸ���ԭ��**

Java NIO ��`Selector`�� Linux ϵͳ�ϻ���`epoll`ʵ�֣���`epoll_wait`�����쳣ʱ��`Selector`������ȷ����״̬������`select()`�������Ϸ��� 0�����¼������γ���ѭ�����������������ڸ߲����������ӳ�����Ƶ�����֡�

### **����Netty �Ľ������**

Netty ͨ��**���ϼ�� + �ؽ�����**�������ѯ���⣬�����߼�λ��`NioEventLoop`���У�

#### **1. ������ѯ**

Netty ��ÿ��`select()`�������¼ʱ�����������һ��`select()`ǰ��飺



- ���`select()`���� 0�����¼����Һ�ʱС��Ԥ����ֵ��Ĭ�� 500ms��������Ϊ���ܷ�������ѯ��
- ������Σ�Ĭ�� 512 �Σ�����������������ж�Ϊ����ѯ��



**�ؼ����루NioEventLoop.java��**��



java



����









```java
private void select(boolean oldWakenUp) throws IOException {
    Selector selector = this.selector;
    try {
        int selectCnt = 0;
        long currentTimeNanos = System.nanoTime();
        long selectDeadLineNanos = currentTimeNanos + delayNanos(currentTimeNanos);

        for (;;) {
            // ���㳬ʱʱ��
            long timeoutMillis = (selectDeadLineNanos - currentTimeNanos + 500000L) / 1000000L;
            if (timeoutMillis <= 0) {
                if (selectCnt == 0) {
                    selector.selectNow();
                    selectCnt = 1;
                }
                break;
            }

            // ִ��select()
            int selectedKeys = selector.select(timeoutMillis);
            selectCnt++;

            // ����Ƿ���wakeup������¼�
            if (selectedKeys > 0 || oldWakenUp || wakenUp.get() || hasTasks() || hasScheduledTasks()) {
                break;
            }

            // ��⵽select��ʱ���̣�����Ϊ����ѯ��
            if (Thread.interrupted()) {
                // �����ж�
                selectCnt = 1;
                break;
            }

            long time = System.nanoTime();
            // ���select��ʱС��1���Ҵ���512�Σ��ж�Ϊ����ѯ
            if (time - TimeUnit.MILLISECONDS.toNanos(timeoutMillis) >= currentTimeNanos) {
                selectCnt = 1;
            } else if (SELECTOR_AUTO_REBUILD_THRESHOLD > 0 && selectCnt >= SELECTOR_AUTO_REBUILD_THRESHOLD) {
                // �ؽ�Selector
                rebuildSelector();
                selector = this.selector;
                selector.selectNow();
                selectCnt = 1;
                break;
            }

            currentTimeNanos = time;
        }

        if (selectCnt > MIN_PREMATURE_SELECTOR_RETURNS) {
            // ��¼������־�����ܴ��ڿ���ѯ
            logger.trace("Selector.select() returned prematurely {} times in a row for Selector {}.",
                    selectCnt - 1, selector);
        }
    } catch (CancelledKeyException e) {
        // ����ȡ���ļ�
    }
}
```

#### **2. �ؽ� Selector**

����⵽����ѯʱ��Netty �ᴴ��һ���µ�`Selector`��������ע���`Channel`�Ӿɵ�`Selector`Ǩ�Ƶ��µ�`Selector`�����رվɵ�`Selector`��



**�ؼ����루NioEventLoop.java��**��



java



����









```java
public void rebuildSelector() {
    final Selector oldSelector = selector;
    final SelectorTuple newSelectorTuple;

    try {
        // �����µ�Selector
        newSelectorTuple = openSelector();
    } catch (Exception e) {
        // �����쳣
        return;
    }

    // ��ȡ����ע���Channel
    int nChannels = 0;
    Set<SelectionKey> keys = oldSelector.keys();
    for (SelectionKey key : keys) {
        Object a = key.attachment();
        try {
            if (!key.isValid() || key.channel().keyFor(newSelectorTuple.unwrappedSelector) != null) {
                continue;
            }

            int interestOps = key.interestOps();
            key.cancel();
            
            // ����Selector������ע��Channel
            SelectionKey newKey = key.channel().register(newSelectorTuple.unwrappedSelector, interestOps, a);
            if (a instanceof AbstractNioChannel) {
                // ����Channel��SelectionKey
                ((AbstractNioChannel) a).selectionKey = newKey;
            }
            nChannels++;
        } catch (Exception e) {
            // ����ע���쳣
        }
    }

    // �滻��SelectorΪ��Selector
    this.selector = newSelectorTuple.selector;
    this.unwrappedSelector = newSelectorTuple.unwrappedSelector;

    try {
        // �رվ�Selector
        oldSelector.close();
    } catch (Throwable t) {
        // ��¼�ر��쳣
    }

    // ��¼�ؽ���־
    logger.info("Migrated {} channel(s) to new Selector.", nChannels);
}
```






### **���������Ż���ʩ**

���˿���ѯ�����ؽ���Netty ��ͨ�����·�ʽ���� NIO ���ȶ��ԣ�

#### **1. ���� Selector ����**

- **���߳�ģ��**��ÿ��`NioEventLoop`ֻ��һ���̸߳��𣬱�����߳�ͬʱ����ͬһ��`Selector`���µľ������⡣

#### **2. �Ż����ѻ���**

- ʹ��`wakenUp`��־λ���ٲ���Ҫ��`selector.wakeup()`���ã�����ϵͳ������

#### **3. ���������¼�**

- ��`Selector`���ص�`SelectionKey`������������ѭ����������������Ч�ʡ�

### **�ġ��ܽ�**

Netty ͨ��**����ѯ��� + Selector �ؽ�**���ƣ���Ч����� Java NIO �Ŀ���ѯ Bug����֤���ڸ߲��������µ��ȶ��ԡ����ַ�����Ȼ�޷��Ӹ������޸� JDK ���⣬��ͨ�� �����Ϸ��� �� �Զ��ָ��� �Ĳ��ԣ�ʹ��ܾ߱��������޸���������Ϊҵ�紦�����ѯ����ı�׼ʵ����