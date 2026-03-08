package org.example.fullstackrevconnect.modules.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.fullstackrevconnect.modules.dto.*;
import org.example.fullstackrevconnect.modules.entity.*;
import org.example.fullstackrevconnect.modules.repository.*;
import org.example.fullstackrevconnect.modules.service.AnalyticsService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final PostRepository postRepository;
    private final ConnectionRepository connectionRepository;

    public AnalyticsSummary getAnalyticsSummary(Long userId, int days) {

        LocalDateTime fromDate = LocalDateTime.now().minusDays(days);

        List<Post> posts = postRepository
                .findByAuthorIdOrderByCreatedAtDesc(
                        userId,
                        PageRequest.of(0, 1000)
                ).getContent()
                .stream()
                .filter(p -> p.getCreatedAt().isAfter(fromDate))
                .toList();

        long totalReach = posts.stream().mapToLong(Post::getReach).sum();
        long totalImpressions = posts.stream().mapToLong(Post::getImpressions).sum();
        long totalLikes = posts.stream().mapToLong(Post::getLikes).sum();
        long totalComments = posts.stream().mapToLong(Post::getCommentCount).sum();
        long totalShares = posts.stream().mapToLong(Post::getShares).sum();

        long totalFollowers =
                connectionRepository
                        .findByStatusAndSenderIdOrStatusAndReceiverId(
                                Connection.Status.ACCEPTED, userId,
                                Connection.Status.ACCEPTED, userId
                        ).size();

        double engagementRate = 0.0;
        if (totalImpressions > 0) {
            engagementRate =
                    ((double) (totalLikes + totalComments + totalShares)
                            / totalImpressions) * 100;
        }

        return new AnalyticsSummary(
                totalReach,
                totalImpressions,
                totalLikes,
                totalComments,
                totalShares,
                totalFollowers,
                engagementRate
        );
    }

    public List<WeeklyEngagementDTO> getWeeklyEngagement(Long userId, int days) {

        LocalDateTime fromDate = LocalDateTime.now().minusDays(days);

        List<Post> posts = postRepository
                .findByAuthorIdOrderByCreatedAtDesc(
                        userId,
                        PageRequest.of(0, 1000)
                ).getContent()
                .stream()
                .filter(p -> p.getCreatedAt().isAfter(fromDate))
                .toList();

        Map<String, WeeklyEngagementDTO> map = new LinkedHashMap<>();

        for (Post post : posts) {

            String day = post.getCreatedAt()
                    .getDayOfWeek()
                    .toString();

            map.putIfAbsent(day,
                    new WeeklyEngagementDTO(day, 0L, 0L, 0L));

            WeeklyEngagementDTO dto = map.get(day);

            dto.setLikes(dto.getLikes() + post.getLikes());
            dto.setComments(dto.getComments() + post.getCommentCount());
            dto.setShares(dto.getShares() + post.getShares());
        }

        return new ArrayList<>(map.values());
    }

    public List<TopPostDTO> getTopPosts(Long userId, int days) {

        LocalDateTime fromDate = LocalDateTime.now().minusDays(days);

        return postRepository
                .findByAuthorIdOrderByCreatedAtDesc(
                        userId,
                        PageRequest.of(0, 100)
                ).getContent()
                .stream()
                .filter(p -> p.getCreatedAt().isAfter(fromDate))
                .sorted((a, b) -> Long.compare(
                        (b.getLikes() + b.getCommentCount() + b.getShares()),
                        (a.getLikes() + a.getCommentCount() + a.getShares())
                ))                .limit(5)
                .map(p -> new TopPostDTO(
                        p.getId(),
                        p.getContent(),
                        (long) p.getLikes(),
                        (long) p.getCommentCount(),
                        p.getShares(),
                        p.getReach()
                ))
                .collect(Collectors.toList());
    }

    public List<ReachImpressionDTO> getReachImpressions(Long userId, int days) {

        LocalDateTime fromDate = LocalDateTime.now().minusDays(days);

        return postRepository
                .findByAuthorIdOrderByCreatedAtDesc(
                        userId,
                        PageRequest.of(0, 1000)
                ).getContent()
                .stream()
                .filter(p -> p.getCreatedAt().isAfter(fromDate))
                .map(p -> new ReachImpressionDTO(
                        p.getCreatedAt().toLocalDate().toString(),
                        p.getReach(),
                        p.getImpressions()
                ))
                .collect(Collectors.toList());
    }

    public List<FollowerGrowthDTO> getFollowerGrowth(Long userId, int days) {

        LocalDateTime fromDate = LocalDateTime.now().minusDays(days);

        List<Connection> connections =
                connectionRepository
                        .findByStatusAndSenderIdOrStatusAndReceiverId(
                                Connection.Status.ACCEPTED, userId,
                                Connection.Status.ACCEPTED, userId
                        );

        Map<String, Long> growthMap = new LinkedHashMap<>();

        for (Connection connection : connections) {

            if (connection.getCreatedAt() != null &&
                    connection.getCreatedAt().isAfter(fromDate)) {

                String date = connection.getCreatedAt()
                        .toLocalDate()
                        .toString();

                growthMap.put(date,
                        growthMap.getOrDefault(date, 0L) + 1);
            }
        }

        return growthMap.entrySet()
                .stream()
                .map(e -> new FollowerGrowthDTO(
                        e.getKey(),
                        e.getValue()
                ))
                .collect(Collectors.toList());
    }
}