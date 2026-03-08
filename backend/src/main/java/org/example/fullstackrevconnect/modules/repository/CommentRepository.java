package org.example.fullstackrevconnect.modules.repository;
import org.example.fullstackrevconnect.modules.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {

    List<Comment> findByPostIdOrderByCreatedAtAsc(String postId);
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.authorId = :authorId")
    long countCommentsForBusiness(@Param("authorId") Long authorId);
}