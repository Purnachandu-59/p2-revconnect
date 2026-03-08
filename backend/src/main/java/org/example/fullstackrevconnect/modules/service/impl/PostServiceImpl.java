package org.example.fullstackrevconnect.modules.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.fullstackrevconnect.modules.dto.PostRequest;
import org.example.fullstackrevconnect.modules.entity.*;
import org.example.fullstackrevconnect.modules.entity.enums.NotificationType;
import org.example.fullstackrevconnect.modules.repository.CommentRepository;
import org.example.fullstackrevconnect.modules.repository.PostRepository;
import org.example.fullstackrevconnect.modules.repository.PostViewRepository;
import org.example.fullstackrevconnect.modules.repository.UserRepository;
import org.example.fullstackrevconnect.modules.service.NotificationService;
import org.example.fullstackrevconnect.modules.service.PostService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;
    private final PostViewRepository postViewRepository;

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public Post createPost(PostRequest request, User user) {

        Post post = new Post();
        post.setContent(request.getContent());
        post.setImageUrl(request.getImageUrl());
        post.setAuthorId(user.getId());
        post.setAuthorName(user.getUsername());
        post.setAuthorAvatar(user.getProfileImage());

        return postRepository.save(post);
    }

    @Override
    public Page<Post> getFeed(int page, int size, User currentUser) {

        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(
                PageRequest.of(page, size));

        posts.forEach(post -> {

            PostView view = PostView.builder()
                    .postId(post.getId())
                    .userId(currentUser.getId())
                    .build();

            postViewRepository.save(view);

            post.setImpressions(post.getImpressions() + 1);

            boolean alreadyViewed =
                    postViewRepository.existsByPostIdAndUserId(
                            post.getId(),
                            currentUser.getId()
                    );

            if (!alreadyViewed) {
                post.setReach(post.getReach() + 1);
            }

            boolean liked = post.getLikedUsers()
                    .stream()
                    .anyMatch(user -> user.getId().equals(currentUser.getId()));

            post.setLikedByCurrentUser(liked);
        });

        return posts;
    }

    @Override
    public Post toggleLike(String postId, User user) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        boolean alreadyLiked = post.getLikedUsers().contains(user);

        if (alreadyLiked) {
            post.getLikedUsers().remove(user);
        } else {
            post.getLikedUsers().add(user);

            notificationService.createNotification(
                    post.getAuthorId(),
                    user.getId(),
                    NotificationType.LIKE,
                    user.getUsername() + " liked your post",
                    postId
            );
        }

        Post saved = postRepository.save(post);

        boolean liked = saved.getLikedUsers()
                .stream()
                .anyMatch(u -> u.getId().equals(user.getId()));

        saved.setLikedByCurrentUser(liked);

        return saved;
    }

    @Override
    public Comment addComment(String postId, String content, User user) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setAuthorId(user.getId());
        comment.setAuthorName(user.getUsername());
        comment.setAuthorAvatar(user.getProfileImage());
        comment.setPost(post);

        Comment saved = commentRepository.save(comment);
        post.getComments().add(saved);

        notificationService.createNotification(
                post.getAuthorId(),
                user.getId(),
                NotificationType.COMMENT,
                user.getUsername() + " commented on your post",
                postId
        );

        return saved;
    }

    @Override
    public List<Comment> getComments(String postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
    }

    @Override
    public void deletePost(String postId, User user) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getAuthorId().equals(user.getId())) {
            throw new RuntimeException("You can delete only your own posts");
        }

        postRepository.delete(post);
    }

    @Override
    public Page<Post> getPostsByUser(User user, int page, int size) {

        return postRepository.findByAuthorIdOrderByCreatedAtDesc(
                user.getId(),
                PageRequest.of(page, size)
        );
    }

    @Override
    public Post sharePost(String postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setShares(post.getShares() + 1);

        return postRepository.save(post);
    }
}