package org.example.fullstackrevconnect.modules.controller;

import lombok.RequiredArgsConstructor;
import org.example.fullstackrevconnect.modules.dto.*;
import org.example.fullstackrevconnect.modules.entity.User;
import org.example.fullstackrevconnect.modules.service.AnalyticsService;
import org.example.fullstackrevconnect.modules.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final UserService userService;


    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('CREATOR','BUSINESS')")
    public AnalyticsSummary getSummary(
            Authentication authentication,
            @RequestParam(defaultValue = "30") int days) {

        User user = userService.getUserByUsername(authentication.getName());
        return analyticsService.getAnalyticsSummary(user.getId(), days);
    }


    @GetMapping("/weekly")
    @PreAuthorize("hasAnyRole('CREATOR','BUSINESS')")
    public List<WeeklyEngagementDTO> getWeekly(
            Authentication authentication,
            @RequestParam(defaultValue = "7") int days) {

        User user = userService.getUserByUsername(authentication.getName());
        return analyticsService.getWeeklyEngagement(user.getId(), days);
    }

    @GetMapping("/top-posts")
    @PreAuthorize("hasAnyRole('CREATOR','BUSINESS')")
    public List<TopPostDTO> getTopPosts(
            Authentication authentication,
            @RequestParam(defaultValue = "30") int days) {

        User user = userService.getUserByUsername(authentication.getName());
        return analyticsService.getTopPosts(user.getId(), days);
    }


    @GetMapping("/reach")
    @PreAuthorize("hasAnyRole('CREATOR','BUSINESS')")
    public List<ReachImpressionDTO> getReach(
            Authentication authentication,
            @RequestParam(defaultValue = "30") int days) {

        User user = userService.getUserByUsername(authentication.getName());
        return analyticsService.getReachImpressions(user.getId(), days);
    }

    @GetMapping("/follower-growth")
    @PreAuthorize("hasAnyRole('CREATOR','BUSINESS')")
    public List<FollowerGrowthDTO> getFollowerGrowth(
            Authentication authentication,
            @RequestParam(defaultValue = "30") int days) {

        User user = userService.getUserByUsername(authentication.getName());
        return analyticsService.getFollowerGrowth(user.getId(), days);
    }
}