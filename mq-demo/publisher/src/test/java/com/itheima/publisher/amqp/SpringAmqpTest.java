package com.itheima.publisher.amqp;


import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage() {

        String queueName = "simple.queue";
        String message = "hello,spring amqp";

        rabbitTemplate.convertAndSend(queueName, message);

    }

}
