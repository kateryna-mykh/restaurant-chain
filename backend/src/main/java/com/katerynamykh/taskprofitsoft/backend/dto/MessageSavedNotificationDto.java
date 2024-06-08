package com.katerynamykh.taskprofitsoft.backend.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record MessageSavedNotificationDto(
		String subject, 
		String message,
		String fromService, 
		List<String> receiverEmails) {
	
}
