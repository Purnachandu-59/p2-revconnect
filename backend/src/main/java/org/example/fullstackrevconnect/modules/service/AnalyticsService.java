package org.example.fullstackrevconnect.modules.service;

import org.example.fullstackrevconnect.modules.dto.*;

import java.util.List;

public interface AnalyticsService {

    public AnalyticsSummary getAnalyticsSummary(Long userId, int days);

    public List<WeeklyEngagementDTO> getWeeklyEngagement(Long userId, int days);

    public List<TopPostDTO> getTopPosts(Long userId, int days);

    public List<ReachImpressionDTO> getReachImpressions(Long userId, int days);

    public List<FollowerGrowthDTO> getFollowerGrowth(Long userId, int days);
}
