package com.katerynamykh.task.dispatcher.subscriber;

import com.katerynamykh.task.dispatcher.config.RabbitMQConfig;
import com.katerynamykh.task.dispatcher.dto.MessageSavedNotificationDto;
import com.katerynamykh.task.dispatcher.service.EmailMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQListener {
	private final EmailMessageService emailService;

	@RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
	public void receiveMessage(MessageSavedNotificationDto message) {
		emailService.processMessage(message);
	}
}
