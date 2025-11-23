package com.example.tunehub.service;

import com.example.tunehub.dto.NotificationResponseDTO;
import com.example.tunehub.model.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UsersMapper.class)
public interface NotificationMapper {
    NotificationResponseDTO NotificationToNotificationResponseDTO(Notification n);
}
