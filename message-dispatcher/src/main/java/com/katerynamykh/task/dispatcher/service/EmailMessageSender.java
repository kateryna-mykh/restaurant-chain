package com.katerynamykh.task.dispatcher.service;

import com.katerynamykh.task.dispatcher.exception.EmailSendingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailMessageSender {
	private final JavaMailSender mailSender;
	@Value("${spring.mail.username}")
	private String fromEmail;

	public void send(String[] to, String subject, String body) throws EmailSendingException {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromEmail);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		try {
			mailSender.send(message);
		} catch (Exception e) {
			throw new EmailSendingException("Failed to send email", e);
		}
	}

	public void send(String to, String subject, String body) throws EmailSendingException {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromEmail);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		try {
			mailSender.send(message);
		} catch (Exception e) {
			throw new EmailSendingException("Failed to send email", e);
		}
	}
}
