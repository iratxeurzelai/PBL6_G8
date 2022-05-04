package com.javatechie.rabbitmq.demo.consumer;

import com.javatechie.rabbitmq.demo.config.MessagingConfig;
import com.javatechie.rabbitmq.demo.dto.UsuarioStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class User {

    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeMessageFromQueue(UsuarioStatus orderStatus) {
        System.out.println("Message recieved from queue : " + orderStatus);
    }
}
