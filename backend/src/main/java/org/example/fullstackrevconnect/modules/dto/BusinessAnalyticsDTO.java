package org.example.fullstackrevconnect.modules.dto;

import java.util.List;

public class BusinessAnalyticsDTO {

    private long totalFollowers;
    private long totalConnections;
    private long profileViews;
    private long postImpressions;
    private long postEngagement;
    private long websiteClicks;
    private List<Integer> monthlyGrowth;

    public BusinessAnalyticsDTO(
            long totalFollowers,
            long totalConnections,
            long profileViews,
            long postImpressions,
            long postEngagement,
            List<Integer> monthlyGrowth
    ) {
        this.totalFollowers = totalFollowers;
        this.totalConnections = totalConnections;
        this.profileViews = profileViews;
        this.postImpressions = postImpressions;
        this.postEngagement = postEngagement;
        this.websiteClicks = websiteClicks;
        this.monthlyGrowth = monthlyGrowth;
    }

    public long getTotalFollowers() { return totalFollowers; }
    public long getTotalConnections() { return totalConnections; }
    public long getProfileViews() { return profileViews; }
    public long getPostImpressions() { return postImpressions; }
    public long getPostEngagement() { return postEngagement; }
    public long getWebsiteClicks() { return websiteClicks; }
    public List<Integer> getMonthlyGrowth() { return monthlyGrowth; }
}