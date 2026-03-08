package org.example.fullstackrevconnect.modules.repository;

import org.example.fullstackrevconnect.modules.entity.PostView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostViewRepository extends JpaRepository<PostView, Long> {

    List<PostView> findByPostId(String postId);

    List<PostView> findByPostIdAndViewedAtAfter(String postId, LocalDateTime date);

    boolean existsByPostIdAndUserId(String postId, Long userId);
}