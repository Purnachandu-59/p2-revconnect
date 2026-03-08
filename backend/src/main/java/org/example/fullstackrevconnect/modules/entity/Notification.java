package org.example.fullstackrevconnect.modules.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.fullstackrevconnect.modules.entity.enums.NotificationType;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long receiverId;

    private Long senderId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String message;

    private String referenceId; // postId or connectionId

    private boolean isRead;

    private LocalDateTime createdAt;
}
