package org.example.fullstackrevconnect.modules.controller;

import lombok.RequiredArgsConstructor;
import org.example.fullstackrevconnect.modules.dto.CommentRequest;
import org.example.fullstackrevconnect.modules.dto.PostRequest;
import org.example.fullstackrevconnect.modules.entity.*;
import org.example.fullstackrevconnect.modules.entity.Comment;
import org.example.fullstackrevconnect.modules.entity.Post;
import org.example.fullstackrevconnect.modules.entity.User;
import org.example.fullstackrevconnect.modules.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@CrossOrigin
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Post> createPost(
            @RequestBody PostRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        User user = postService.getUserByUsername(userDetails.getUsername());

        return ResponseEntity.ok(postService.createPost(request, user));
    }

    @GetMapping("/feed")
    public ResponseEntity<Page<Post>> getFeed(
            @RequestParam int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        User user = postService.getUserByUsername(userDetails.getUsername());

        return ResponseEntity.ok(postService.getFeed(page, size, user));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Post> toggleLike(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        User user = postService.getUserByUsername(userDetails.getUsername());

        return ResponseEntity.ok(postService.toggleLike(id, user));
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Comment> addComment(
            @PathVariable String id,
            @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        User user = postService.getUserByUsername(userDetails.getUsername());

        return ResponseEntity.ok(
                postService.addComment(id, request.getContent(), user)
        );
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable String id) {
        return ResponseEntity.ok(postService.getComments(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        User user = postService.getUserByUsername(userDetails.getUsername());

        postService.deletePost(id, user);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<Page<Post>> getMyPosts(
            @RequestParam int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = postService.getUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(postService.getPostsByUser(user, page, size));
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<Post> sharePost(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(postService.sharePost(id));
    }
}