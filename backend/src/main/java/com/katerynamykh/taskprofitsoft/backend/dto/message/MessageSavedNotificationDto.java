package com.katerynamykh.taskprofitsoft.backend.dto.message;

import lombok.Builder;

@Builder
public record MessageSavedNotificationDto(
		String subject, 
		String message,
		String fromService, 
		String[] receiverEmails) {
	
}
