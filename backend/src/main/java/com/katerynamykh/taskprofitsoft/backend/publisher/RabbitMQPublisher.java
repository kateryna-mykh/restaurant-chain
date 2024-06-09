package com.katerynamykh.taskprofitsoft.backend.publisher;

import com.katerynamykh.taskprofitsoft.backend.config.RabbitMQConfig;
import com.katerynamykh.taskprofitsoft.backend.dto.MessageSavedNotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQPublisher {
	private final RabbitTemplate rabbitTemplate;

	public void sendMessage(Object message) {
		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, message);
	}

	public void createAndSendMessage(String subject, String body, String fromService) {
		MessageSavedNotificationDto message = MessageSavedNotificationDto.builder()
				.subject(subject)
				.fromService(fromService)
				.message(body)
				.receiverEmails(RabbitMQConfig.ADMIN_EMAIL)
				.build();
		sendMessage(message);
	}
}
