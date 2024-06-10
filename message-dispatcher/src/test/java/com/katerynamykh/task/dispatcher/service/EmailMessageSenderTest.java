package com.katerynamykh.task.dispatcher.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import com.katerynamykh.task.dispatcher.exception.EmailSendingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmailMessageSenderTest {
    @MockBean
    private JavaMailSender mailSender;
    @Autowired
    EmailMessageSender emailMessageSender;
    private static SimpleMailMessage message;

    @BeforeAll
    static void setUpBeforeClass() {
        message = new SimpleMailMessage();
        message.setFrom("fromMail");
        message.setTo("toMail");
        message.setSubject("subject");
        message.setText("body");
    }

    @Test
    @DisplayName("Verify send() method works")
    void send_Ok() throws EmailSendingException {
        emailMessageSender.send(message.getTo(), message.getSubject(), message.getText());

        Mockito.verify(mailSender, Mockito.times(1)).send(Mockito.any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Verify send() method works, retrieve the EmailSendingException")
    void send_EmailSendingException() {
        Mockito.doThrow(new MailException("Failed to send email") {
        }).when(mailSender).send(Mockito.any(SimpleMailMessage.class));

        assertThrows(EmailSendingException.class, () -> {
            emailMessageSender.send(message.getTo(), message.getSubject(), message.getText());
        });
        Mockito.verify(mailSender, Mockito.times(1)).send(Mockito.any(SimpleMailMessage.class));
    }
}
