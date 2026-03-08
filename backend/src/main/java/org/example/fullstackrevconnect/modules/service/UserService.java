package org.example.fullstackrevconnect.modules.service;

import org.example.fullstackrevconnect.modules.dto.UpdateUserRequest;
import org.example.fullstackrevconnect.modules.dto.UserResponse;
import org.example.fullstackrevconnect.modules.entity.User;

import java.util.List;

public interface UserService {

    UserResponse getProfile(String username);

    UserResponse updateProfile(String username, UpdateUserRequest request);

    List<User> getAllUsersExcept(Long userId);

    User getUserByUsername(String username);

    UserResponse getProfileById(Long id);

    List<UserResponse> searchUsers(String query);
}