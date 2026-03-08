package org.example.fullstackrevconnect.service;

import org.example.fullstackrevconnect.modules.dto.NotificationDto;
import org.example.fullstackrevconnect.modules.entity.Notification;
import org.example.fullstackrevconnect.modules.entity.enums.NotificationType;
import org.example.fullstackrevconnect.modules.repository.NotificationRepository;
import org.example.fullstackrevconnect.modules.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;



    @Test
    void testCreateNotification_success() {

        notificationService.createNotification(
                2L,
                1L,
                NotificationType.CONNECTION_REQUEST,
                "chandu sent request",
                "100"
        );

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void testCreateNotification_selfNotification_shouldNotSave() {

        notificationService.createNotification(
                1L,
                1L,
                NotificationType.CONNECTION_REQUEST,
                "self test",
                "100"
        );

        verify(notificationRepository, never()).save(any());
    }



    @Test
    void testGetUserNotifications_success() {

        Notification notification = Notification.builder()
                .id(1L)
                .receiverId(2L)
                .senderId(1L)
                .type(NotificationType.CONNECTION_REQUEST)
                .message("chandu sent request")
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(notificationRepository
                .findByReceiverIdOrderByCreatedAtDesc(2L))
                .thenReturn(List.of(notification));

        List<NotificationDto> result =
                notificationService.getUserNotifications(2L);

        assertEquals(1, result.size());
        assertEquals("chandu sent request", result.get(0).getMessage());
        assertFalse(result.get(0).isRead());
    }



    @Test
    void testMarkAsRead_success() {

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setRead(false);

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(notification));

        notificationService.markAsRead(1L);

        assertTrue(notification.isRead());
        verify(notificationRepository).save(notification);
    }

    @Test
    void testMarkAsRead_notFound() {

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(Exception.class,
                () -> notificationService.markAsRead(1L));
    }



    @Test
    void testGetUnreadCount_success() {

        when(notificationRepository
                .countByReceiverIdAndIsReadFalse(2L))
                .thenReturn(5L);

        long count = notificationService.getUnreadCount(2L);

        assertEquals(5L, count);
    }
}