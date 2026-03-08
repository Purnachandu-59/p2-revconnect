package org.example.fullstackrevconnect.modules.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String content;

    @Column(columnDefinition = "LONGTEXT")
    private String imageUrl;

    private Long authorId;
    private String authorName;
    private String authorAvatar;
    private Long reach = 0L;
    private Long impressions = 0L;
    private Long shares = 0L;

    private LocalDateTime createdAt = LocalDateTime.now();


    @ManyToMany
    @JoinTable(
            name = "post_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private List<User> likedUsers = new ArrayList<>();


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    @Transient
    public int getLikes() {
        return likedUsers.size();
    }


    @Transient
    public int getCommentCount() {
        return comments != null ? comments.size() : 0;
    }


    @Transient
    private boolean likedByCurrentUser;
}