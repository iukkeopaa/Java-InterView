### һ�����в���Ķ�ͷ��������������ģʽ��

#### ��������

�� RabbitMQ �Ķ��в��� **Ĭ�ϵĹ�ƽ�ַ���round-robin��** ���ƣ�����Ϣ����ʱ�����ϴ�ʱ�����ܳ��ֶ�ͷ������

- �����ߴӶ����а�˳���ȡ��Ϣ����������Ϣ�����ʱ�������縴�Ӽ��㡢IO �ȴ�����������Ϣ��ʹ��Ҳ����ȴ���
- �������������� **�����߶�**�����Ƕ��б���������Ϊ�������崦��Ч���½���

#### ʾ������

1. �������� 3 ����Ϣ��M1�����Ӽ��㣬�� 10s����M2���������� 1s����M3���������� 1s����
2. �����߰�˳���ȡ��Ϣ�������ȴ����� M1 ���ܿ�ʼ M2 �� M3��
3. �����M2 �� M3 �������������߱��ض����У��ȴ� M1 ��ɣ������ʱ 12s��

#### �������

1. **����Ԥȡ������Prefetch Count��**
   ͨ������������һ���Ի�ȡ����Ϣ���������������Ϣ������������������

   java



����









   ```java
   // Java �ͻ���ʾ��
   channel.basicQos(1); // ÿ��ֻԤȡ1����Ϣ����������ȡ��һ��
   ```

- �� `prefetch_count=1` ʱ��RabbitMQ ��ȷ�������ߴ����굱ǰ��Ϣ���ٷ�����һ�������Ȿ�ض��л�ѹ��

2. **��Ϣ���ȼ�����**
   Ϊ������Ϣ���ø������ȼ���ȷ����Ҫ�������ȴ���

   bash











   ```bash
   # �������ȼ�����
   rabbitmqctl set_policy ha-high-priority "^high-priority\." '{"ha-mode":"all","x-max-priority":10}'
   ```

- ������Ϣʱָ�����ȼ���

  java



     ����

     

     

     

     

     ```java
     AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
         .priority(5) // ���ȼ���Χ0-9��Ĭ��0
         .build();
     channel.basicPublish(exchange, routingKey, properties, message.getBytes());
     ```

3. **�����������**
   ����������Ϊ���С���񣬼��ٵ�����Ϣ�Ĵ���ʱ�䡣

### �����������ͬ��ʱ�Ķ�ͷ����

#### ��������

�� RabbitMQ ʹ�� **������У�Mirrored Queues��** ��߿�����ʱ�������У�leader����Ӷ��У�follower����ͬ�����̿��ܵ��¶�ͷ������

- �����б���ȴ����дӶ���ȷ�Ͻ�����Ϣ�󣬲Ż��������߷���ȷ�ϣ�ACK����
- ��ĳ���Ӷ����������ӳٻ���������ͬ�������������лᱻ�������޷����������Ϣ��

#### �������

1. **����ȷ��ģʽ**
   ͨ������ `publisher_confirm` ���𣬽���ͬ��Ҫ��

    - `wait-for-confirms`�������ߵȴ����о���ڵ�ȷ�ϣ���ȫ����������
    - `wait-for-confirms-on-replica-set`��ֻ�ȴ����־���ڵ�ȷ�ϣ�ƽ�ⰲȫ�����ܣ���

2. **���پ���ڵ�����**
   ����ľ���ڵ������ͬ���������������ҵ���������ú���ľ�������ͨ�� 3 �����ɣ���

   bash











   ```bash
   # ���þ�����в��ԣ����3������
   rabbitmqctl set_policy ha-all "^" '{"ha-mode":"exactly","ha-params":3}'
   ```

3. **��ز�������Ͻڵ�**
   ͨ�� `rabbitmqctl cluster_status` ʵʱ��ؽڵ�״̬����ʱ���ֲ�����ͬ���쳣�Ľڵ㡣

### �����־û���Ϣд�����ʱ������

#### ��������

����������Ϊ **�־û���Durable��** ʱ����Ϣ��д����̺�Żᱻȷ�ϣ������� IO ���ܲ��ѣ����ܵ��¶�ͷ������

- ����ͷ������Ϣд�뻺����������Ϣ����ȴ�����ʹ���ǲ���Ҫ�־û���

#### �������

1. **�Ż���������**

    - ʹ�� SSD ��� HDD���������д���ܡ�

    - ���� RabbitMQ �Ĵ���ͬ�����ԣ�

      bash











     ```bash
     # rabbitmq.conf ����
     disk_free_limit.absolute = 500MB  # ���̿��ÿռ����500MBʱ��ͣ����
     ```

2. **����־û���ǳ־û���Ϣ**
   ����Ҫ�ĳ־û���Ϣ����ͨ�ķǳ־û���Ϣ�ֱ��͵���ͬ���У������໥Ӱ�죺

   java



����









   ```java
   // �־û���Ϣ
   channel.basicPublish(exchange, routingKey, 
       new AMQP.BasicProperties.Builder().deliveryMode(2).build(), 
       message.getBytes());
   
   // �ǳ־û���Ϣ��deliveryMode=1��
   channel.basicPublish(exchange, routingKey, 
       new AMQP.BasicProperties.Builder().deliveryMode(1).build(), 
       message.getBytes());
   ```

### �ġ�����е�ȫ����Դ����

#### ��������

��������й���ͬһ��������ʱ����ĳ�����е�����ռ�ô�����Դ���� CPU���ڴ棩�����ܵ����������еĴ���������

#### �������

1. **ʹ�ö�������������**
   Ϊ�ؼ����з���ר���������ߣ�������Դ������

   java



����









   ```java
   // �������������Ӻ������߳�
   ConnectionFactory factory = new ConnectionFactory();
   Connection connection1 = factory.newConnection();
   Connection connection2 = factory.newConnection();
   
   Channel channel1 = connection1.createChannel();
   Channel channel2 = connection2.createChannel();
   
   // ����1��������
   channel1.basicConsume(queue1, true, consumer1);
   
   // ����2��������
   channel2.basicConsume(queue2, true, consumer2);
   ```

2. **��Դ����**
   ͨ���������������� Docker������̸��룬ȷ����ͬ���е������������ڶ�������Դ�����С�

### �塢�ܽ�

RabbitMQ �еĶ�ͷ������ҪԴ�� **�����ߴ���˳������**��**�������ͬ���ӳ�**��**���� IO ƿ��** �� **��Դ����**�����˼·������

1. **�Ż���Ϣ�ַ�**��ͨ�� `prefetch_count` �����ȼ����п�����Ϣ������
2. **ƽ�������������**���������þ������������ȷ��ģʽ��
3. **Ӳ���������Ż�**��ʹ�� SSD����������ͬ�����ԡ�
4. **��Դ����**��Ϊ�ؼ����з���ר����Դ��


# ===========================================

### һ������ԭ������ͳ���

#### 1. **TTL ���Ƶ��µĶ�ͷ����**

- **���Ȿ��**��
  RabbitMQ �� TTL ����**���Լ�����**����������Ϣ�������ͷ��ʱ�Ż����Ƿ���ڡ�������ͷ�����ڴ���δ������Ϣ����ʹ��β��Ϣ�ѹ��ڣ�Ҳ�޷�����ʱɾ����·�������Ŷ��С�

- ���ͳ���

  ��

    - �����߸���д������� TTL ����Ϣ������ͷ���ѻ�δ������Ϣ��
    - �����ߴ����ٶ�����������Ϣ�ڶ�����ͣ��ʱ�䳤����β������Ϣ�޷���ʱ����

#### 2. **���Ŷ��д�����ʱ���µ�����**

- **���Ȿ��**��
  ���������е���Ϣ�����ԭ����ܾ������ڣ��������Ŷ��к������Ŷ��е������ߴ����������㣬�ᵼ�����Ŷѻ�������Ӱ�������еĿ����ԡ�

- ���ͳ���

  ��

    - �������������ϸ�� TTL��������Ϣͬʱ���ڽ������Ŷ��С�
    - ���Ŷ������������ò������粢�����͡����̴߳������޷�������Ϣ�����ٶȡ�

### ��������Ӱ���밸������

#### 1. **TTL ��ͷ������Ӱ��**

- **ʾ��**��
  �������� 100 ����Ϣ��ǰ 50 �� TTL=60s���� 50 �� TTL=10s����������ÿ�봦�� 1 ����Ϣ���� 50 ����Ϣ��ȴ� 50 ����ܱ�����ʵ�ʹ���ʱ���Ϊ 60s�������趨�� 10s����

- ���

  ��

    - ʱ�侫��ʧ�أ���Ϣʵ�ʹ���ʱ�����Զ���趨�� TTL��
    - ��Դ�˷ѣ�������Ϣռ�ö��пռ䣬�����ڴ�ѹ����

#### 2. **���Ŷ���������Ӱ��**

- **ʾ��**��
  ���������������쳣�ܾ�������Ϣ���������Ŷ���ÿ����� 1000 ����Ϣ�������Ŷ��������߽��ܴ��� 100 �� / �롣

- ���

  ��

    - ���Ŷѻ������Ŷ��п������ͣ����ܴ������̸澯��ϵͳ������
    - ������������Ӧ�������Ŷ�����ͨ���������ͬ���ģ��ѻ����ܵ���������д��������

### �����������

#### 1. **�Ż� TTL ��ͷ����**

- **���� 1�����ٶ��г��ȣ�������Ϣ�ѻ�**
  ͨ�����ö�����󳤶ȣ�`x-max-length`��������ֽ�����`x-max-length-bytes`������ֹ����ͷ�����۹���δ������Ϣ��

  java



����









  ```java
  Map<String, Object> args = new HashMap<>();
  args.put("x-max-length", 1000);  // �������洢1000����Ϣ
  channel.queueDeclare("ttl.queue", true, false, false, args);
  ```

- **���� 2��ʹ�����ȼ����� + ��Ϣ�� TTL**
  Ϊÿ����Ϣ���ö��� TTL����ͨ�����ȼ�ȷ��������Ϣ���ȴ���

  java



����









  ```java
  // �������ȼ����У�������ȼ�10��
  Map<String, Object> args = new HashMap<>();
  args.put("x-max-priority", 10);
  channel.queueDeclare("priority.ttl.queue", true, false, false, args);
  
  // ������Ϣʱ������ʱ��Խ�����ȼ�Խ��
  AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
      .expiration("5000")  // 5�����
      .priority(10)        // ������ȼ�
      .build();
  channel.basicPublish("", "priority.ttl.queue", properties, "urgent message".getBytes());
  ```

- **���� 3��ʹ�� RabbitMQ Delayed Message Plugin**
  �ò��֧�������ӳ�ʱ�䣬�Ҳ�������ͷ��飬���׽�� TTL ��ͷ������

  java



����









  ```java
  // �����ӳ���Ϣ�������ʽ��
  AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
      .headers(Collections.singletonMap("x-delay", 30000))  // 30���ӳ�
      .build();
  channel.basicPublish("x-delayed-message", "routingKey", properties, "delayed message".getBytes());
  ```

#### 2. **������Ŷ�������**

- **���� 1��������Ŷ��������ߴ�������**

    - ����������������ͨ�����̻߳��ʵ�������������Ŷ��С�
    - �Ż������߼������ٵ�����Ϣ�Ĵ���ʱ�䣨���첽����������������

- **���� 2���������Ŷ��еĶ�����Դ**
  Ϊ���Ŷ��з���ר�������ӳء��̳߳أ������������о�����Դ��

  java



����









  ```java
  // �������������Ŷ������Ӻ�������
  ConnectionFactory factory = new ConnectionFactory();
  Connection dlqConnection = factory.newConnection();
  Channel dlqChannel = dlqConnection.createChannel();
  dlqChannel.basicConsume("dlq.queue", true, new DefaultConsumer(dlqChannel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, 
                                 AMQP.BasicProperties properties, byte[] body) {
          // ���ٴ�������
      }
  });
  ```

- **���� 3���ּ���������**
  �����������ʹ���������Ŷ��У��ֱ����ò�ͬ�����Ѳ��ԣ�

  java



����









  ```java
  // ������ͬ���͵����Ŷ���
  channel.queueDeclare("dlq.retryable", true, false, false, null);  // �����Ե�����
  channel.queueDeclare("dlq.fatal", true, false, false, null);       // �������������
  
  // ���ݾܾ�ԭ��·�ɵ���ͬ����
  if (isRetryable(error)) {
      channel.basicPublish("dlx.exchange", "retryable", null, message.getBytes());
  } else {
      channel.basicPublish("dlx.exchange", "fatal", null, message.getBytes());
  }
  ```

### �ġ������Ԥ��

- **���г��ȼ��**��ͨ�� RabbitMQ Management API �� Prometheus ������Ŷ��г��ȣ�������ֵ�澯��
- **��Ϣ�������ʼ��**���Ա����Ŷ��е��������ʺ��������ʣ���ʱ���ִ���ƿ����
- **TTL ������֤**�����ڷ��ʹ���ͬ TTL �Ĳ�����Ϣ����֤ʵ�ʹ���ʱ���Ƿ����Ԥ�ڡ�

### �塢�ܽ�

�����Ŷ��к� TTL �����У���ͷ������ҪԴ��**��Ϣ���ڼ��Ķ��Ի���**��**���Ŵ�����������**�����˼·������

1. **�Ż� TTL ʵ��**�����ٶ��жѻ���ʹ�����ȼ����л�ר���ӳٲ����
2. **�������Ŵ�������**��������Դ���á��ּ�������������������ơ�
3. **���Ƽ����ϵ**��ʵʱ��֪�������գ���ʱ�������ԡ�

ͨ����Щ��ʩ��������Ч�������Ŷ��к� TTL �����еĶ�ͷ������ȷ����Ϣϵͳ�ĸ�Ч�ȶ����С�


## 