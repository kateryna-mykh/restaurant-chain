package com.katerynamykh.taskprofitsoft.backend.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	@Value("${rabbitmq.queue.name}")
	private String queueName;
	@Value("${rabbitmq.exchange.name}")
	private String exchangeName;
	@Value("${rabbitmq.routing.key}")
	private String routingKey;

	@Bean
	public Queue emailQueue() {
		return new Queue(queueName);
	}

	@Bean
	public DirectExchange messageTopicExchange() {
		return new DirectExchange(exchangeName);
	}

	@Bean
	public Binding emailMessageBinding() {
		return BindingBuilder.bind(emailQueue())
				.to(messageTopicExchange())
				.with(routingKey);
	}
}