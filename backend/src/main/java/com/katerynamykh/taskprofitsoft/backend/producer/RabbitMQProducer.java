package com.katerynamykh.taskprofitsoft.backend.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQProducer {
	@Value("${rabbitmq.exchange.name}")
	private String exchange;

	@Value("${rabbitmq.routing.key}")
	private String routingKey;
	
	private final RabbitTemplate rabbitTemplate;

	public void sendMessage(Object message) {
		rabbitTemplate.convertAndSend(exchange, routingKey, message);
	}
}
