package org.example.fullstackrevconnect.service;

import org.example.fullstackrevconnect.modules.dto.*;
import org.example.fullstackrevconnect.modules.entity.Connection;
import org.example.fullstackrevconnect.modules.entity.Post;
import org.example.fullstackrevconnect.modules.repository.ConnectionRepository;
import org.example.fullstackrevconnect.modules.repository.PostRepository;
import org.example.fullstackrevconnect.modules.service.impl.AnalyticsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticalServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private ConnectionRepository connectionRepository;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    @Test
    void testGetAnalyticsSummary_chandu() {

        Post post = mock(Post.class);
        when(post.getLikes()).thenReturn(10);
        when(post.getCommentCount()).thenReturn(5);
        when(post.getShares()).thenReturn(2L);
        when(post.getReach()).thenReturn(100L);
        when(post.getImpressions()).thenReturn(200L);
        when(post.getCreatedAt()).thenReturn(LocalDateTime.now().minusDays(1));

        Page<Post> page = new PageImpl<>(List.of(post));
        when(postRepository.findByAuthorIdOrderByCreatedAtDesc(anyLong(), any()))
                .thenReturn(page);

        Connection connection = mock(Connection.class);
        when(connectionRepository
                .findByStatusAndSenderIdOrStatusAndReceiverId(any(), anyLong(), any(), anyLong()))
                .thenReturn(List.of(connection));

        AnalyticsSummary summary =
                analyticsService.getAnalyticsSummary(1L, 7);

        assertEquals(100L, summary.getTotalReach());
        assertEquals(200L, summary.getTotalImpressions());
        assertEquals(10L, summary.getTotalLikes());
        assertEquals(5L, summary.getTotalComments());
        assertEquals(2L, summary.getTotalShares());
        assertEquals(1L, summary.getTotalFollowers());
    }

    @Test
    void testGetWeeklyEngagement_chandu() {

        Post post = mock(Post.class);
        when(post.getLikes()).thenReturn(10);
        when(post.getCommentCount()).thenReturn(5);
        when(post.getShares()).thenReturn(2L);
        when(post.getCreatedAt()).thenReturn(LocalDateTime.now().minusDays(1));

        Page<Post> page = new PageImpl<>(List.of(post));
        when(postRepository.findByAuthorIdOrderByCreatedAtDesc(anyLong(), any()))
                .thenReturn(page);

        List<WeeklyEngagementDTO> result =
                analyticsService.getWeeklyEngagement(1L, 7);

        assertEquals(10L, result.get(0).getLikes());
        assertEquals(5L, result.get(0).getComments());
        assertEquals(2L, result.get(0).getShares());
    }

    @Test
    void testGetTopPosts_chandu() {

        Post post = mock(Post.class);
        when(post.getId()).thenReturn("chandu-post-1");
        when(post.getContent()).thenReturn("Post by chandu");
        when(post.getLikes()).thenReturn(10);
        when(post.getCommentCount()).thenReturn(5);
        when(post.getShares()).thenReturn(2L);
        when(post.getCreatedAt()).thenReturn(LocalDateTime.now().minusDays(1));

        Page<Post> page = new PageImpl<>(List.of(post));
        when(postRepository.findByAuthorIdOrderByCreatedAtDesc(anyLong(), any()))
                .thenReturn(page);

        List<TopPostDTO> result =
                analyticsService.getTopPosts(1L, 7);

        assertEquals("Post by chandu", result.get(0).getContent());
    }

    @Test
    void testGetReachImpressions_chandu() {

        Post post = mock(Post.class);
        when(post.getReach()).thenReturn(100L);
        when(post.getImpressions()).thenReturn(200L);
        when(post.getCreatedAt()).thenReturn(LocalDateTime.now().minusDays(1));

        Page<Post> page = new PageImpl<>(List.of(post));
        when(postRepository.findByAuthorIdOrderByCreatedAtDesc(anyLong(), any()))
                .thenReturn(page);

        List<ReachImpressionDTO> result =
                analyticsService.getReachImpressions(1L, 7);

        assertEquals(100L, result.get(0).getReach());
        assertEquals(200L, result.get(0).getImpressions());
    }

    @Test
    void testGetFollowerGrowth_chandu() {

        Connection connection = mock(Connection.class);
        when(connection.getCreatedAt())
                .thenReturn(LocalDateTime.now().minusDays(1));

        when(connectionRepository
                .findByStatusAndSenderIdOrStatusAndReceiverId(any(), anyLong(), any(), anyLong()))
                .thenReturn(List.of(connection));

        List<FollowerGrowthDTO> result =
                analyticsService.getFollowerGrowth(1L, 7);

        assertEquals(1L, result.get(0).getNewFollowers());
    }
}