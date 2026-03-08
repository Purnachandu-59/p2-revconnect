package org.example.fullstackrevconnect.modules.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.fullstackrevconnect.modules.dto.UpdateUserRequest;
import org.example.fullstackrevconnect.modules.dto.UserResponse;
import org.example.fullstackrevconnect.modules.entity.User;
import org.example.fullstackrevconnect.modules.repository.UserRepository;
import org.example.fullstackrevconnect.modules.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getProfile(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .profileImage(user.getProfileImage())
                .build();
    }

    @Override
    public UserResponse updateProfile(String username,
                                      UpdateUserRequest request) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setBio(request.getBio());
        user.setProfileImage(request.getProfileImage());

        userRepository.save(user);

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .profileImage(user.getProfileImage())
                .build();
    }

    @Override
    public List<User> getAllUsersExcept(Long userId) {
        return userRepository.findAll()
                .stream()
                .filter(user -> !user.getId().equals(userId))
                .toList();
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserResponse getProfileById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getBio(),
                user.getProfileImage()
        );
    }

    @Override
    public List<UserResponse> searchUsers(String query) {

        return userRepository
                .findByUsernameContainingIgnoreCase(query)
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getBio(),
                        user.getProfileImage()
                ))
                .toList();
    }
}