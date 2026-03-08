package org.example.fullstackrevconnect.modules.controller;

import lombok.RequiredArgsConstructor;
import org.example.fullstackrevconnect.modules.dto.NotificationDto;
import org.example.fullstackrevconnect.modules.entity.User;
import org.example.fullstackrevconnect.modules.service.NotificationService;
import org.example.fullstackrevconnect.modules.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping
    public List<NotificationDto> getNotifications(Authentication authentication) {
        User user = userService.getUserByUsername(authentication.getName());
        return notificationService.getUserNotifications(user.getId());
    }

    @GetMapping("/unread-count")
    public long getUnreadCount(Authentication authentication) {
        User user = userService.getUserByUsername(authentication.getName());
        return notificationService.getUnreadCount(user.getId());
    }

    @PutMapping("/{id}/read")
    public void markRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }
}
