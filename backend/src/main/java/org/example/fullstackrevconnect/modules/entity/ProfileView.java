package org.example.fullstackrevconnect.modules.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "profile_views")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private Long viewedUserId;


    @Column(nullable = false)
    private Long viewerUserId;


    @Column(nullable = false)
    private LocalDateTime viewedAt;


    @PrePersist
    public void prePersist() {
        this.viewedAt = LocalDateTime.now();
    }
}