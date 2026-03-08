package org.example.fullstackrevconnect.modules.repository;

import org.example.fullstackrevconnect.modules.entity.ProfileView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ProfileViewRepository extends JpaRepository<ProfileView, Long> {


    long countByViewedUserId(Long viewedUserId);

    long countByViewedUserIdAndViewedAtAfter(Long viewedUserId, LocalDateTime date);


    boolean existsByViewedUserIdAndViewerUserId(Long viewedUserId, Long viewerUserId);

}