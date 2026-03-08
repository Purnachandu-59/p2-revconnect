package org.example.fullstackrevconnect.modules.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AnalyticsSummary {

    private Long totalReach;
    private Long totalImpressions;
    private Long totalLikes;
    private Long totalComments;
    private Long totalShares;
    private Long totalFollowers;
    private Double engagementRate;
}