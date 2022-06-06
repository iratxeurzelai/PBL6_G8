package com.mutricion.demo.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGER_NAME = "Prueba";
    public static final String EXCHANGER_NAME2 = "Prueba2";
    public static final String EXCHANGER_NAME3 = "Prueba3";
    private static final boolean DURABLE = false;
    private static final boolean AUTO_DELETE = false;

    public static final String ROUTING_KEY_PUESTA = "SinProcesar";
    public static final String ROUTING_KEY_OBTENCION = "Procesada";
    public static final String ROUTING_KEY_DAR = "Cambiar";
    public static final String ROUTING_KEY_RECIBIR = "Cambiado";
    public static final String ROUTING_KEY_RECOMENDAR = "Recomendar";
    public static final String ROUTING_KEY_RECOMENDACION = "Recomendacion";

    public static final String QUEUE_NAME_PUESTA="Dejar";
    public static final String QUEUE_NAME_OBTENCION="Obtener";
    public static final String QUEUE_NAME_DAR="Dar";
    public static final String QUEUE_NAME_RECIBIR="Recibir";
    public static final String QUEUE_NAME_RECOMENDAR="Finde";
    public static final String QUEUE_NAME_RECOMENDACION="Finde2";
    
    @Bean
    Queue queue1() {
        return new Queue(QUEUE_NAME_PUESTA, DURABLE);
    }

    @Bean
    Queue queue2() {
        return new Queue(QUEUE_NAME_OBTENCION, DURABLE);
    }

    @Bean
    Queue queue3() {
        return new Queue(QUEUE_NAME_DAR, DURABLE);
    }

    @Bean
    Queue queue4() {
        return new Queue(QUEUE_NAME_RECIBIR, DURABLE);
    }

    @Bean
    Queue queue5() {
        return new Queue(QUEUE_NAME_RECOMENDAR, DURABLE);
    }

    @Bean
    Queue queue6() {
        return new Queue(QUEUE_NAME_RECOMENDACION, DURABLE);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(EXCHANGER_NAME,DURABLE, AUTO_DELETE);
    }

    @Bean
    DirectExchange exchange2() {
        return new DirectExchange(EXCHANGER_NAME2,DURABLE, AUTO_DELETE);
    }

    @Bean
    DirectExchange exchange3() {
        return new DirectExchange(EXCHANGER_NAME3,DURABLE, AUTO_DELETE);
    }

    @Bean
    Binding binding1(Queue queue1, DirectExchange exchange) {
        return BindingBuilder.bind(queue1).to(exchange).with(ROUTING_KEY_PUESTA);
    }

    @Bean
    Binding binding2(Queue queue2, DirectExchange exchange) {
        return BindingBuilder.bind(queue2).to(exchange).with(ROUTING_KEY_OBTENCION);
    }

    @Bean
    Binding binding3(Queue queue3, DirectExchange exchange2) {
        return BindingBuilder.bind(queue3).to(exchange2).with(ROUTING_KEY_DAR);
    }

    @Bean
    Binding binding4(Queue queue4, DirectExchange exchange2) {
        return BindingBuilder.bind(queue4).to(exchange2).with(ROUTING_KEY_RECIBIR);
    }

    @Bean
    Binding binding5(Queue queue5, DirectExchange exchange3) {
        return BindingBuilder.bind(queue5).to(exchange3).with(ROUTING_KEY_RECOMENDAR);
    }

    @Bean
    Binding binding6(Queue queue6, DirectExchange exchange3) {
        return BindingBuilder.bind(queue6).to(exchange3).with(ROUTING_KEY_RECOMENDACION);
    }
    
    @Bean
    Receiver receiver() {
        return new Receiver();
    }
    
    @Bean
    Receiver2 receiver2() {
        return new Receiver2();
    }

    @Bean
    Receiver3 receiver3() {
        return new Receiver3();
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, Receiver.RECEIVE_METHOD_NAME);
    }
    
    @Bean
    MessageListenerAdapter listenerAdapter2(Receiver2 receiver2) {
        return new MessageListenerAdapter(receiver2, Receiver2.RECEIVE_METHOD_NAME2);
    }

    @Bean
    MessageListenerAdapter listenerAdapter3(Receiver3 receiver3) {
        return new MessageListenerAdapter(receiver3, Receiver3.RECEIVE_METHOD_NAME3);
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME_OBTENCION);
        container.setMessageListener(listenerAdapter);
        container.setDefaultRequeueRejected(false);
        
        return container;
    }
    
    @Bean
    SimpleMessageListenerContainer container2(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter2) {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME_RECIBIR);
        container.setMessageListener(listenerAdapter2);
        container.setDefaultRequeueRejected(false);
        
        return container;
    }

    @Bean
    SimpleMessageListenerContainer container3(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter3) {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME_RECOMENDACION);
        container.setMessageListener(listenerAdapter3);
        container.setDefaultRequeueRejected(false);
        
        return container;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }
    
    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}