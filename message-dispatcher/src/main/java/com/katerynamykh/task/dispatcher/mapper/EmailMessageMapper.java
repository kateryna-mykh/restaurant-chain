package com.katerynamykh.task.dispatcher.mapper;

import com.katerynamykh.task.dispatcher.config.MapperConfig;
import com.katerynamykh.task.dispatcher.dto.MessageSavedNotificationDto;
import com.katerynamykh.task.dispatcher.model.EmailMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface EmailMessageMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "errorMessage", ignore = true)
    @Mapping(target = "creationTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "sendAttempt", ignore = true)
    EmailMessage toModel(MessageSavedNotificationDto messageDto);
}
