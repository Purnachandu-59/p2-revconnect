package org.example.fullstackrevconnect.modules.service.impl;

import org.example.fullstackrevconnect.modules.dto.BusinessAnalyticsDTO;
import org.example.fullstackrevconnect.modules.entity.Connection;
import org.example.fullstackrevconnect.modules.entity.Post;
import org.example.fullstackrevconnect.modules.entity.ProfileView;
import org.example.fullstackrevconnect.modules.repository.ConnectionRepository;
import org.example.fullstackrevconnect.modules.repository.PostRepository;
import org.example.fullstackrevconnect.modules.repository.CommentRepository;
import org.example.fullstackrevconnect.modules.service.BusinessAnalyticsService;
import org.springframework.stereotype.Service;
import org.example.fullstackrevconnect.modules.repository.ProfileViewRepository;

import java.util.List;

@Service
public class BusinessAnalyticsServiceImpl implements BusinessAnalyticsService {

    private final ConnectionRepository connectionRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ProfileViewRepository profileViewRepository;

    public BusinessAnalyticsServiceImpl(
            ConnectionRepository connectionRepository,
            PostRepository postRepository,
            CommentRepository commentRepository,
            ProfileViewRepository profileViewRepository
    ) {
        this.connectionRepository = connectionRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.profileViewRepository = profileViewRepository;
    }

    public BusinessAnalyticsDTO getBusinessAnalytics(Long userId) {


        long totalFollowers =
                connectionRepository.countByReceiverIdAndStatus(
                        userId, Connection.Status.ACCEPTED);


        long totalConnections =
                connectionRepository
                        .countBySenderIdAndStatusOrReceiverIdAndStatus(
                                userId, Connection.Status.ACCEPTED,
                                userId, Connection.Status.ACCEPTED
                        );


        long profileViews =
                profileViewRepository.countByViewedUserId(userId);


        Long impressions =
                postRepository.getTotalImpressions(userId);
        long postImpressions = impressions != null ? impressions : 0;


        long totalLikes = calculateLikes(userId);


        long totalComments =
                commentRepository.countCommentsForBusiness(userId);

        long postEngagement = totalLikes + totalComments;


        return new BusinessAnalyticsDTO(
                totalFollowers,
                totalConnections,
                profileViews,
                postImpressions,
                postEngagement,
                generateMonthlyGrowth(userId)
        );
    }

    public long calculateLikes(Long userId) {

        List<Post> posts = postRepository.findByAuthorId(userId);

        return posts.stream()
                .mapToLong(post ->
                        post.getLikedUsers() != null
                                ? post.getLikedUsers().size()
                                : 0)
                .sum();
    }

    public List<Integer> generateMonthlyGrowth(Long userId) {

        int totalPosts = (int) postRepository.findByAuthorId(userId).size();
        int totalLikes = (int) calculateLikes(userId);

        return List.of(
                totalPosts,
                totalPosts + 3,
                totalPosts + 6,
                totalLikes,
                totalLikes + 5,
                totalLikes + 10
        );
    }


    public void trackProfileView(Long profileUserId, Long viewerUserId) {

        if (!profileUserId.equals(viewerUserId)) {

            ProfileView view = ProfileView.builder()
                    .viewedUserId(profileUserId)
                    .viewerUserId(viewerUserId)
                    .build();

            profileViewRepository.save(view);
        }
    }

}