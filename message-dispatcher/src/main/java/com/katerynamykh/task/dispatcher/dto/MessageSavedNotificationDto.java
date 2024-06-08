package com.katerynamykh.task.dispatcher.dto;

import java.util.List;

public record MessageSavedNotificationDto(
		String subject, 
		String message,
		String fromService, 
		List<String> receiverEmails) {
	
}
