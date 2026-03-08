package org.example.fullstackrevconnect.service;

import org.example.fullstackrevconnect.modules.dto.PostRequest;
import org.example.fullstackrevconnect.modules.entity.*;
import org.example.fullstackrevconnect.modules.entity.enums.NotificationType;
import org.example.fullstackrevconnect.modules.repository.*;
import org.example.fullstackrevconnect.modules.service.NotificationService;
import org.example.fullstackrevconnect.modules.service.impl.PostServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private PostViewRepository postViewRepository;

    @InjectMocks
    private PostServiceImpl service;



    @Test
    void testGetUserByUsername_success() {

        User user = new User();
        user.setUsername("chandu");

        when(userRepository.findByUsername("chandu"))
                .thenReturn(Optional.of(user));

        User result = service.getUserByUsername("chandu");

        assertEquals("chandu", result.getUsername());
    }

    @Test
    void testGetUserByUsername_notFound() {

        when(userRepository.findByUsername("chandu"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.getUserByUsername("chandu"));
    }



    @Test
    void testCreatePost_success() {

        User user = new User();
        user.setId(1L);
        user.setUsername("chandu");
        user.setProfileImage("img.jpg");

        PostRequest request = new PostRequest();
        request.setContent("Hello");
        request.setImageUrl("img.png");

        Post saved = new Post();
        saved.setContent("Hello");

        when(postRepository.save(any(Post.class)))
                .thenReturn(saved);

        Post result = service.createPost(request, user);

        assertEquals("Hello", result.getContent());
    }



    @Test
    void testToggleLike_like() {

        User user = new User();
        user.setId(1L);
        user.setUsername("chandu");

        Post post = new Post();
        post.setId("p1");
        post.setAuthorId(2L);
        post.setLikedUsers(new ArrayList<>());

        when(postRepository.findById("p1"))
                .thenReturn(Optional.of(post));

        when(postRepository.save(any(Post.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Post result = service.toggleLike("p1", user);

        assertTrue(result.getLikedUsers().contains(user));

        verify(notificationService).createNotification(
                eq(2L),
                eq(1L),
                eq(NotificationType.LIKE),
                contains("liked your post"),
                eq("p1")
        );
    }

    @Test
    void testToggleLike_unlike() {

        User user = new User();
        user.setId(1L);

        Post post = new Post();
        post.setId("p1");
        post.setAuthorId(2L);
        post.setLikedUsers(new ArrayList<>(List.of(user)));

        when(postRepository.findById("p1"))
                .thenReturn(Optional.of(post));

        when(postRepository.save(any(Post.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Post result = service.toggleLike("p1", user);

        assertFalse(result.getLikedUsers().contains(user));
    }



    @Test
    void testAddComment_success() {

        User user = new User();
        user.setId(1L);
        user.setUsername("chandu");
        user.setProfileImage("img.jpg");

        Post post = new Post();
        post.setId("p1");
        post.setAuthorId(2L);
        post.setComments(new ArrayList<>());

        when(postRepository.findById("p1"))
                .thenReturn(Optional.of(post));

        Comment savedComment = new Comment();
        savedComment.setContent("Nice");

        when(commentRepository.save(any(Comment.class)))
                .thenReturn(savedComment);

        Comment result = service.addComment("p1", "Nice", user);

        assertEquals("Nice", result.getContent());

        verify(notificationService).createNotification(
                eq(2L),
                eq(1L),
                eq(NotificationType.COMMENT),
                contains("commented"),
                eq("p1")
        );
    }



    @Test
    void testDeletePost_success() {

        User user = new User();
        user.setId(1L);

        Post post = new Post();
        post.setAuthorId(1L);

        when(postRepository.findById("p1"))
                .thenReturn(Optional.of(post));

        service.deletePost("p1", user);

        verify(postRepository).delete(post);
    }

    @Test
    void testDeletePost_notOwner() {

        User user = new User();
        user.setId(1L);

        Post post = new Post();
        post.setAuthorId(2L);

        when(postRepository.findById("p1"))
                .thenReturn(Optional.of(post));

        assertThrows(RuntimeException.class,
                () -> service.deletePost("p1", user));
    }



    @Test
    void testSharePost_success() {

        Post post = new Post();
        post.setShares(5L);

        when(postRepository.findById("p1"))
                .thenReturn(Optional.of(post));

        when(postRepository.save(any(Post.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Post result = service.sharePost("p1");

        assertEquals(6L, result.getShares());
    }
}