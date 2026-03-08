package org.example.fullstackrevconnect.service;

import java.sql.Connection;
import org.example.fullstackrevconnect.modules.dto.BusinessAnalyticsDTO;
import org.example.fullstackrevconnect.modules.entity.Post;
import org.example.fullstackrevconnect.modules.entity.ProfileView;
import org.example.fullstackrevconnect.modules.entity.User;
import org.example.fullstackrevconnect.modules.repository.*;
import org.example.fullstackrevconnect.modules.service.impl.BusinessAnalyticsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusinessAnalyticsServiceImplTest {

    @Mock
    private ConnectionRepository connectionRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ProfileViewRepository profileViewRepository;

    @InjectMocks
    private BusinessAnalyticsServiceImpl service;



    @Test
    void testCalculateLikes_success() {

        Long userId = 1L;

        User user1 = mock(User.class);
        User user2 = mock(User.class);
        User user3 = mock(User.class);

        Post post1 = mock(Post.class);
        Post post2 = mock(Post.class);

        when(post1.getLikedUsers()).thenReturn(List.of(user1, user2));
        when(post2.getLikedUsers()).thenReturn(List.of(user3));

        when(postRepository.findByAuthorId(userId))
                .thenReturn(List.of(post1, post2));

        long likes = service.calculateLikes(userId);

        assertEquals(3L, likes);
    }

    @Test
    void testCalculateLikes_nullLikedUsers() {

        Long userId = 1L;

        Post post = mock(Post.class);
        when(post.getLikedUsers()).thenReturn(null);

        when(postRepository.findByAuthorId(userId))
                .thenReturn(List.of(post));

        long likes = service.calculateLikes(userId);

        assertEquals(0L, likes);
    }

    @Test
    void testGenerateMonthlyGrowth_success() {

        Long userId = 1L;

        User user = mock(User.class);
        Post post = mock(Post.class);

        when(post.getLikedUsers()).thenReturn(List.of(user));

        when(postRepository.findByAuthorId(userId))
                .thenReturn(List.of(post));

        List<Integer> growth = service.generateMonthlyGrowth(userId);

        assertEquals(6, growth.size());
    }

    @Test
    void testTrackProfileView_success() {

        service.trackProfileView(1L, 2L);

        verify(profileViewRepository).save(any(ProfileView.class));
    }

    @Test
    void testTrackProfileView_selfView_shouldNotSave() {

        service.trackProfileView(1L, 1L);

        verify(profileViewRepository, never()).save(any(ProfileView.class));
    }
}