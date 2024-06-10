package com.katerynamykh.task.dispatcher.service;

import com.katerynamykh.task.dispatcher.dto.MessageSavedNotificationDto;

public interface EmailMessageService {
    void processMessage(MessageSavedNotificationDto messageDto);

    void resendMessagesWithErrorStatus();
}
