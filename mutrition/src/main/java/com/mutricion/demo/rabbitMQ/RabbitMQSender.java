package com.mutricion.demo.rabbitMQ;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RabbitMQSender {
    @Autowired
    private RabbitTemplate template;

    @GetMapping(value="/msg")
    public void enviarMensaje(  ) {
    
        template.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY);
    }
}
