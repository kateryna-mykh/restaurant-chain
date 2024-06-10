package com.katerynamykh.task.dispatcher.service;

import com.katerynamykh.task.dispatcher.dto.MessageSavedNotificationDto;
import com.katerynamykh.task.dispatcher.exception.EmailSendingException;
import com.katerynamykh.task.dispatcher.mapper.EmailMessageMapper;
import com.katerynamykh.task.dispatcher.model.EmailMessage;
import com.katerynamykh.task.dispatcher.model.MessageStatus;
import com.katerynamykh.task.dispatcher.repository.EmailMessageRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailMessageServiceImpl implements EmailMessageService {
    private final EmailMessageRepository repository;
    private final EmailMessageMapper messageMapper;
    private final EmailMessageSender messageSender;

    @Override
    public void processMessage(MessageSavedNotificationDto messageDto) {
        EmailMessage savedMessage = save(messageDto);
        sendMessageAndUpdateStatus(savedMessage, messageDto.receiverEmails(), messageDto.subject(),
                messageDto.message(), false);
    }

    @Override
    @Scheduled(cron = "0 0/5 * * * ?")
    public void resendMessagesWithErrorStatus() {
        List<EmailMessage> allNotSended = findAllNotSended();
        if (!allNotSended.isEmpty()) {
            for (EmailMessage emailMessage : allNotSended) {
                sendMessageAndUpdateStatus(emailMessage, emailMessage.getReceiverEmails(),
                        emailMessage.getSubject(), emailMessage.getMessage(), true);
            }
        }
    }

    private EmailMessage save(MessageSavedNotificationDto messageDto) {
        EmailMessage modelEmailMessage = messageMapper.toModel(messageDto);
        modelEmailMessage.setStatus(MessageStatus.NEW.name());
        modelEmailMessage.setSendAttempt(0);
        modelEmailMessage.setCreationTime(Instant.now());
        return repository.save(modelEmailMessage);
    }

    private List<EmailMessage> findAllNotSended() {
        return repository.findAllByStatus(MessageStatus.ERROR.name());
    }

    private void sendMessageAndUpdateStatus(EmailMessage emailMessage, String[] to, String subject,
            String body, boolean isRetry) {
        try {
            messageSender.send(to, subject, body);
            emailMessage.setStatus(MessageStatus.SENT.name());
        } catch (EmailSendingException e) {
            String errorMessage = e.getClass().getSimpleName() + ": " + e.getMessage();
            emailMessage.setStatus(MessageStatus.ERROR.name());
            emailMessage.setErrorMessage(errorMessage);
            if (isRetry) {
                emailMessage.setSendAttempt(emailMessage.getSendAttempt() + 1);
                emailMessage.setUpdatedTime(Instant.now());
            }
        }
        repository.save(emailMessage);
    }
}
