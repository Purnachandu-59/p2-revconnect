package org.example.fullstackrevconnect.modules.service;

import org.example.fullstackrevconnect.modules.dto.NotificationDto;
import org.example.fullstackrevconnect.modules.entity.enums.NotificationType;

import java.util.List;

public interface NotificationService {

    void createNotification(Long receiverId,
                            Long senderId,
                            NotificationType type,
                            String message,
                            String referenceId);

    List<NotificationDto> getUserNotifications(Long userId);

    void markAsRead(Long id);

    long getUnreadCount(Long userId);
}