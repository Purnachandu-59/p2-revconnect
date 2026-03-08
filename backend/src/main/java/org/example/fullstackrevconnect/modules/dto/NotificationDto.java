package org.example.fullstackrevconnect.modules.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {

    private Long id;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
}