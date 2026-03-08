package org.example.fullstackrevconnect.modules.controller;

import lombok.RequiredArgsConstructor;

import org.example.fullstackrevconnect.modules.dto.UpdateUserRequest;
import org.example.fullstackrevconnect.modules.dto.UserResponse;
import org.example.fullstackrevconnect.modules.entity.User;
import org.example.fullstackrevconnect.modules.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(
            Authentication authentication) {

        String username = authentication.getName();
        return ResponseEntity.ok(userService.getProfile(username));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(
            Authentication authentication,
            @RequestBody UpdateUserRequest request) {

        String username = authentication.getName();
        return ResponseEntity.ok(
                userService.updateProfile(username, request));
    }

    @GetMapping("/discover")
    public List<User> discoverUsers(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        return userService.getAllUsersExcept(user.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getProfileById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(
            @RequestParam String query
    ) {
        return ResponseEntity.ok(userService.searchUsers(query));
    }
}

