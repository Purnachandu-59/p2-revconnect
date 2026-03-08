package org.example.fullstackrevconnect.modules.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.fullstackrevconnect.modules.dto.NotificationDto;
import org.example.fullstackrevconnect.modules.entity.Notification;
import org.example.fullstackrevconnect.modules.entity.enums.NotificationType;
import org.example.fullstackrevconnect.modules.repository.NotificationRepository;
import org.example.fullstackrevconnect.modules.service.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public void createNotification(Long receiverId,
                                   Long senderId,
                                   NotificationType type,
                                   String message,
                                   String referenceId) {

        if (receiverId.equals(senderId)) return;

        Notification notification = Notification.builder()
                .receiverId(receiverId)
                .senderId(senderId)
                .type(type)
                .message(message)
                .referenceId(referenceId)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationDto> getUserNotifications(Long userId) {

        return notificationRepository
                .findByReceiverIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(n -> NotificationDto.builder()
                        .id(n.getId())
                        .message(n.getMessage())
                        .isRead(n.isRead())
                        .createdAt(n.getCreatedAt())
                        .build())
                .toList();
    }

    @Override
    public void markAsRead(Long id) {

        Notification notification = notificationRepository.findById(id)
                .orElseThrow();

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public long getUnreadCount(Long userId) {

        return notificationRepository
                .countByReceiverIdAndIsReadFalse(userId);
    }
}