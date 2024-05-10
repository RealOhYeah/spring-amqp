package com.itheima.publisher.amqp;


import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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


}
