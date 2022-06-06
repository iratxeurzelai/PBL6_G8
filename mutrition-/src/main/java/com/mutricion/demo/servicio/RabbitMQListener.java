package com.mutricion.demo.servicio;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQListener implements MessageListener {

	public void onMessage(Message message) {
		System.out.println("Consuming Message - " + new String(message.getBody()));
	}
	
}