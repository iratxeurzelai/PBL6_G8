package com.mutricion.demo.rabbitmq;


import org.apache.commons.codec.binary.Base64;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RabbitMQSender {
    
    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping(value="/msg")
    public void enviarMensaje(
        @PathVariable("mensaje") byte [] bytes
        //@RequestBody("user") int userId,
        //@RequestBody("paciente") int pacienteId
        ) {
        
        String base64String= Base64.encodeBase64String(bytes);

        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME_PUESTA, RabbitMQConfig.ROUTING_KEY_PUESTA, base64String.getBytes());
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME_DAR, RabbitMQConfig.ROUTING_KEY_DAR, base64String.getBytes());
        //rabbitTemplate.addListener(channel);
    }

}
