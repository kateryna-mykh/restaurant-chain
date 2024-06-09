package com.katerynamykh.task.dispatcher.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "email-messages")
public class EmailMessage{
	@Id
	private String id;
	
	@Field(type = FieldType.Text)
	private String subject;
	
	@Field(type = FieldType.Text)
	private String message;
	
	@Field(type = FieldType.Keyword)
	private String fromService;

	@Field(type = FieldType.Keyword)
	private String[] receiverEmails;
	
	@Field(type = FieldType.Keyword)
	private String status;
	
	@Field(type = FieldType.Text)
	private String errorMessage;
	
	@Field(type = FieldType.Date)
	private Instant creationTime;
	
	@Field(type = FieldType.Date)
	private Instant updatedTime;
	
	@Field(type = FieldType.Integer)
	private Integer sendAttempt = 0;
}
