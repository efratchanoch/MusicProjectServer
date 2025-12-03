package com.example.tunehub.service;

import com.example.tunehub.dto.NotificationFollowDTO;
import com.example.tunehub.dto.NotificationResponseDTO;
import com.example.tunehub.dto.NotificationLikesAndFavoritesDTO;
import com.example.tunehub.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UsersMapper.class)
public interface NotificationMapper {
    @Mapping(source = "read", target = "isRead")  // 🚨 explicit mapping
    NotificationResponseDTO NotificationToNotificationResponseDTO(Notification n);

    @Mapping(source = "read", target = "read")  // 🚨 explicit mapping
    NotificationLikesAndFavoritesDTO NotificationToNotificationLikesAndFavoritesDTO(Notification n);

    @Mapping(source = "read", target = "isRead")  // 🚨 explicit mapping
    NotificationFollowDTO NotificationToNotificationFollowDTO(Notification n);
}
