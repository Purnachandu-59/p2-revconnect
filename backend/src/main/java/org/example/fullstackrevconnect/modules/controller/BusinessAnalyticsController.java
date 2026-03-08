package org.example.fullstackrevconnect.modules.controller;

import org.example.fullstackrevconnect.modules.dto.BusinessAnalyticsDTO;
import org.example.fullstackrevconnect.modules.entity.User;
import org.example.fullstackrevconnect.modules.repository.UserRepository;
import org.example.fullstackrevconnect.modules.service.BusinessAnalyticsService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/business")
@CrossOrigin(origins = "http://localhost:4200")
public class BusinessAnalyticsController {

    private final BusinessAnalyticsService businessAnalyticsService;
    private final UserRepository userRepository;

    public BusinessAnalyticsController(
            BusinessAnalyticsService businessAnalyticsService,
            UserRepository userRepository
    ) {
        this.businessAnalyticsService = businessAnalyticsService;
        this.userRepository = userRepository;
    }

    @GetMapping("/analytics")
    public BusinessAnalyticsDTO getAnalytics(Authentication authentication) {


        String username = authentication.getName();


        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));


        return businessAnalyticsService.getBusinessAnalytics(user.getId());
    }

    @PostMapping("/profile/view/{profileUserId}")
    public void trackProfileView(
            @PathVariable Long profileUserId,
            Authentication authentication) {

        String username = authentication.getName();

        User viewer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        businessAnalyticsService.trackProfileView(profileUserId, viewer.getId());
    }
}