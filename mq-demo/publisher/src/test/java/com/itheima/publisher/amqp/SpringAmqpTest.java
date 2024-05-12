package com.itheima.publisher.amqp;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Slf4j
@SpringBootTest
public class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试消息发送(发队列)
     */
    @Test
    public void testSendMessage() {

        String queueName = "simple.queue";
        String message = "hello,spring amqp";

        rabbitTemplate.convertAndSend(queueName, message);

    }

    /**
     * workQueue
     * 向队列中不停发送消息，模拟消息堆积。
     */
    @Test
    public void testWorkQueue() throws InterruptedException {
        // 队列名称
        String queueName = "work.queue";
        // 消息
        String message = "hello, message_";
        for (int i = 0; i < 50; i++) {
            // 发送消息，每20毫秒发送一次，相当于每秒发送50条消息
            rabbitTemplate.convertAndSend(queueName, message + i);
            Thread.sleep(20);
        }
    }


    /**
     * 向新声明的交换机发送消息(hmall.fanout)
     */
    @Test
    public void testFanoutExchange() {
        // 交换机名称
        String exchangeName = "hmall.fanout";
        // 消息
        String message = "hello, everyone!";
        rabbitTemplate.convertAndSend(exchangeName, null, message);
    }

    /**
     * 向direct类型的交换机来发送消息(hmall.direct)
     */
    @Test
    public void testSendDirectExchange() {
        // 交换机名称
        String exchangeName = "hmall.direct";
        // 消息
        String message = "红色警报！日本乱排核废水，导致海洋生物变异，惊现哥斯拉！";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "red", message);
    }

    /**
     * 向direct类型的交换机来发送消息(hmall.direct)
     */
    @Test
    public void testSendDirectExchangeTwo() {
        // 交换机名称
        String exchangeName = "hmall.direct";
        // 消息
        String message = "最新报道，哥斯拉是居民自治巨型气球，虚惊一场！";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "blue", message);
    }

    /**
     * 向topicExchange发送消息
     */
    @Test
    public void testSendTopicExchange() {
        // 交换机名称
        String exchangeName = "hmall.topic";
        // 消息
        String message = "喜报！孙悟空大战哥斯拉，胜!";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "china.news", message);
    }

    /**
     * 向topicExchange发送消息
     */
    @Test
    public void testSendTopicExchangeTwo() {
        // 交换机名称
        String exchangeName = "hmall.topic";
        // 消息
        String message = "蓝色通知，警报解除，哥斯拉是放的气球";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "japan.news", message);
    }

    /**
     * 向topicExchange发送消息
     */
    @Test
    public void testSendTopicExchangeThree() {
        // 交换机名称
        String exchangeName = "hmall.topic";
        // 消息
        String message = "今天天气不错，我的心情挺好";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "china.news", message);
    }


    @Test
    public void testSendMap() throws InterruptedException {
        // 准备消息
        Map<String,Object> msg = new HashMap<>();
        msg.put("name", "小明");
        msg.put("age", 21);
        // 发送消息
        rabbitTemplate.convertAndSend("object.queue", msg);
    }

    /**
     * 测试生产者的确认性
     * @throws InterruptedException
     */
    @Test
    void testConfirmCallback() throws InterruptedException {
        // 1.创建cd
        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
        // 2.添加ConfirmCallback
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("消息回调失败", ex);
            }

            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                log.debug("收到confirm callback回执");
                if(result.isAck()){
                    // 消息发送成功
                    log.debug("消息发送成功，收到ack");
                }else{
                    // 消息发送失败
                    log.error("消息发送失败，收到nack， 原因：{}", result.getReason());
                }
            }
        });

        rabbitTemplate.convertAndSend("hmall.direct123", "red2", "hello", cd);

        Thread.sleep(2000);
    }

    /**
     * 测试一百万条消息发送到mq(非持久化)
     * 内存被占满，产生阻塞的影响（paged out），此时mq不能访问
     */
    @Test
    void testPageOutOne() {
        Message message = MessageBuilder
                .withBody("hello".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT).build();
        for (int i = 0; i < 1000000; i++) {
            rabbitTemplate.convertAndSend("lazy.queue", message);
        }
    }
    /**
     * 测试一百万条消息发送到mq(持久化)
     * 将数据持久化到本地磁盘，然后内存可以继续接收消息，不阻塞
     */
    @Test
    void testPageOutTwo() {
        Message message = MessageBuilder
                .withBody("hello".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
        for (int i = 0; i < 1000000; i++) {
            rabbitTemplate.convertAndSend("lazy.queue", message);
        }
    }



}
