package com.katerynamykh.taskprofitsoft.backend.dto;

import java.util.List;

public record MessageSavedNotificationDto(
		String subject, 
		String message,
		String fromService, 
		List<String> receiverEmails) {
	
}
