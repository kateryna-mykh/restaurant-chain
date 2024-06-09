package com.katerynamykh.task.dispatcher.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	public static final String QUEUE_NAME = "new_enity";
	public static final String EXCHANGE_NAME = "myExchange";
	public static final String ROUTING_KEY = "myExchange_key";

	@Bean
	public Queue newEntityEmailQueue() {
		return new Queue(QUEUE_NAME);
	}

	@Bean
	public DirectExchange messageDirectExchange() {
		return new DirectExchange(EXCHANGE_NAME);
	}

	@Bean
	public Binding newEntityEmailBinding() {
		return BindingBuilder.bind(newEntityEmailQueue())
				.to(messageDirectExchange())
				.with(ROUTING_KEY);
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
