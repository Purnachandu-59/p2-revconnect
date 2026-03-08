package org.example.fullstackrevconnect.modules.entity;


import jakarta.persistence.*;
import lombok.*;
import org.example.fullstackrevconnect.modules.entity.enums.Role;
import org.example.fullstackrevconnect.modules.entity.enums.SecurityQuestion;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String bio;

    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SecurityQuestion securityQuestion;

    private String securityAnswer;
}