package com.katerynamykh.task.dispatcher.repository;

import com.katerynamykh.task.dispatcher.model.EmailMessage;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EmailMessageRepository extends ElasticsearchRepository<EmailMessage, String> {
	List<EmailMessage> findAllByStatus(String status);
}
