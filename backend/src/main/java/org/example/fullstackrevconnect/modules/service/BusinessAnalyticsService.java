package org.example.fullstackrevconnect.modules.service;

import org.example.fullstackrevconnect.modules.dto.BusinessAnalyticsDTO;

import java.util.List;

public interface BusinessAnalyticsService {

    public BusinessAnalyticsDTO getBusinessAnalytics(Long userId);

    public long calculateLikes(Long userId);

    public List<Integer> generateMonthlyGrowth(Long userId);

    public void trackProfileView(Long profileUserId, Long viewerUserId);
}
