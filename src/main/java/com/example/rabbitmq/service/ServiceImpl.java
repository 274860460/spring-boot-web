package com.example.rabbitmq.service;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceImpl {

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setChannelTransacted(true);
        return rabbitTemplate;
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional
    public void save() {
        rabbitTemplate.convertAndSend("hello_task", "1");
//        int i = 1 / 0;
    }

    @Transactional
    public void get() {
        Message message = rabbitTemplate.receive("hello_task");
        String s = new String(message.getBody());
        System.out.println(s);
        int i = 1 / 0;
    }
}
