package com.itheima.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutConfiguration {

    /**
     * 声明fanout类型交换机
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        // 两种声明方法都可以
        // return ExchangeBuilder.fanoutExchange("hmall.fanout2").build();
        return new FanoutExchange("hmall.fanout2");
    }

    /**
     * 声明默认持久化的fanout.queue3队列
     * @return
     */
    @Bean
    public Queue fanoutQueue3(){
        // 两种声明方法都可以
        // return QueueBuilder.durable("fanout.queue3").build();
        return new Queue("fanout.queue3");
    }

    /**
     * 绑定队列和交换机
     * @param fanoutExchange
     * @param fanoutQueue3
     * @return
     */
    @Bean
    public Binding fanoutBinding3(FanoutExchange fanoutExchange,Queue fanoutQueue3){
        return BindingBuilder.bind(fanoutQueue3).to(fanoutExchange);

    }

    /**
     * 声明默认持久化的fanout.queue4队列
     * @return
     */
    @Bean
    public Queue fanoutQueue4(){
        // 两种声明方法都可以
         return QueueBuilder.durable("fanout.queue4").build();
//        return new Queue("fanout.queue4");
    }


    /**
     * 绑定队列和交换机
     * @return
     */
    @Bean
    public Binding fanoutBinding4(){
        return BindingBuilder.bind(fanoutQueue4()).to(fanoutExchange());

    }
}
