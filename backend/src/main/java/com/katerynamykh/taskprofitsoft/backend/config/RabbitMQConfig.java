package com.katerynamykh.taskprofitsoft.backend.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "myExchange";
    public static final String ROUTING_KEY = "myExchange_key";

    @Bean
    public DirectExchange messageDirectExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate tamplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTamplate = new RabbitTemplate(connectionFactory);
        rabbitTamplate.setMessageConverter(jsonConverter());
        return rabbitTamplate;
    }
}
