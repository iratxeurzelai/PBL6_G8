package com.javatechie.rabbitmq.demo.publisher;

import com.javatechie.rabbitmq.demo.config.MessagingConfig;
import com.javatechie.rabbitmq.demo.dto.Usuario;
import com.javatechie.rabbitmq.demo.dto.UsuarioStatus;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/applicacion")
public class OrderPublisher {

    @Autowired
    private RabbitTemplate template;

    @PostMapping("/{appName}")
    public String bookOrder(@RequestBody Usuario order, @PathVariable String appName) {
        order.setUsernameId(UUID.randomUUID().toString());
    
        UsuarioStatus orderStatus = new UsuarioStatus(order, "PROCESS", "order placed succesfully in " + appName);
        template.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, orderStatus);
        return "Success !! " + orderStatus.getUsuario().getNombre() + " " + orderStatus.getUsuario().getApellido();
    }
}
