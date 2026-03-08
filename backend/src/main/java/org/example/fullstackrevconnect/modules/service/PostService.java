package org.example.fullstackrevconnect.modules.service;

import org.example.fullstackrevconnect.modules.dto.PostRequest;
import org.example.fullstackrevconnect.modules.entity.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {

    User getUserByUsername(String username);

    Post createPost(PostRequest request, User user);

    Page<Post> getFeed(int page, int size, User currentUser);

    Post toggleLike(String postId, User user);

    Comment addComment(String postId, String content, User user);

    List<Comment> getComments(String postId);

    void deletePost(String postId, User user);

    Page<Post> getPostsByUser(User user, int page, int size);

    Post sharePost(String postId);
}