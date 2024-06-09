package com.katerynamykh.taskprofitsoft.backend.dto;

import lombok.Builder;

@Builder
public record MessageSavedNotificationDto(
		String subject, 
		String message,
		String fromService, 
		String[] receiverEmails) {
	
}
