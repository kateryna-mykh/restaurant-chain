package com.katerynamykh.task.dispatcher.dto;

public record MessageSavedNotificationDto(
        String subject, 
        String message, 
        String fromService,
        String[] receiverEmails) {

}
