package com.tickets.viajes.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "viaje-events";

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    } //Para mandar el mensaje a rabbit

    @Bean
    public TopicExchange viajeEventsExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    } //Para mandar el mensaje a rabbit

    @Bean
    public Queue viajeConfirmedQueue() { //cola para atrapar el confirmed de pagos
        return new Queue("viaje-confirmed-queue", true);
    } //Para recibir el mensaje de pagos, confirmado

    @Bean
    public Binding confirmedBinding(Queue viajeConfirmedQueue, TopicExchange viajeEventsExchange) {
        return BindingBuilder.bind(viajeConfirmedQueue).to(viajeEventsExchange).with("viaje.confirmed");
    } //Para recibir el mensaje de pagos, confirmado

    @Bean
    public Queue viajeCancelledQueue() { //cola para atrapar el cancelled de pagos
        return new Queue("viaje-cancelled-queue", true);
    } //Para recibir el mensaje de pagos, cancelado

    @Bean
    public Binding cancelledBinding(Queue viajeCancelledQueue, TopicExchange viajeEventsExchange) {
        return BindingBuilder.bind(viajeCancelledQueue).to(viajeEventsExchange).with("viaje.cancelled");
    } //Para recibir el mensaje de pagos, cancelado
}
