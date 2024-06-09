package com.katerynamykh.task.dispatcher.service;

import com.katerynamykh.task.dispatcher.dto.MessageSavedNotificationDto;
import com.katerynamykh.task.dispatcher.model.EmailMessage;
import java.util.List;

public interface EmailMessageService {
	EmailMessage save(MessageSavedNotificationDto messageDto);
	
	List<String> findAllNotSended(String status);
	
	public void processMessage(MessageSavedNotificationDto messageDto);
}
