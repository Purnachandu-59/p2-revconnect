package org.example.fullstackrevconnect.service;

import org.example.fullstackrevconnect.modules.dto.UpdateUserRequest;
import org.example.fullstackrevconnect.modules.dto.UserResponse;
import org.example.fullstackrevconnect.modules.entity.User;
import org.example.fullstackrevconnect.modules.repository.UserRepository;
import org.example.fullstackrevconnect.modules.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl service;



    @Test
    void testGetProfile_success() {

        User user = new User();
        user.setId(1L);
        user.setUsername("chandu");
        user.setEmail("chandu@gmail.com");
        user.setBio("developer");
        user.setProfileImage("img.jpg");

        when(userRepository.findByUsername("chandu"))
                .thenReturn(Optional.of(user));

        UserResponse response = service.getProfile("chandu");

        assertEquals("chandu", response.getUsername());
        assertEquals("developer", response.getBio());
    }

    @Test
    void testGetProfile_notFound() {

        when(userRepository.findByUsername("chandu"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.getProfile("chandu"));
    }



    @Test
    void testUpdateProfile_success() {

        User user = new User();
        user.setId(1L);
        user.setUsername("chandu");
        user.setEmail("chandu@gmail.com");

        UpdateUserRequest request = new UpdateUserRequest();
        request.setBio("updated bio");
        request.setProfileImage("new.jpg");

        when(userRepository.findByUsername("chandu"))
                .thenReturn(Optional.of(user));

        UserResponse response =
                service.updateProfile("chandu", request);

        assertEquals("updated bio", response.getBio());
        assertEquals("new.jpg", response.getProfileImage());

        verify(userRepository).save(user);
    }

    @Test
    void testUpdateProfile_notFound() {

        UpdateUserRequest request = new UpdateUserRequest();

        when(userRepository.findByUsername("chandu"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.updateProfile("chandu", request));
    }

    @Test
    void testGetAllUsersExcept_success() {

        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        List<User> result = service.getAllUsersExcept(1L);

        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getId());
    }



    @Test
    void testGetUserByUsername_success() {

        User user = new User();
        user.setUsername("chandu");

        when(userRepository.findByUsername("chandu"))
                .thenReturn(Optional.of(user));

        User result = service.getUserByUsername("chandu");

        assertEquals("chandu", result.getUsername());
    }

    @Test
    void testGetUserByUsername_notFound() {

        when(userRepository.findByUsername("chandu"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.getUserByUsername("chandu"));
    }



    @Test
    void testGetProfileById_success() {

        User user = new User();
        user.setId(1L);
        user.setUsername("chandu");
        user.setEmail("chandu@gmail.com");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserResponse response = service.getProfileById(1L);

        assertEquals("chandu", response.getUsername());
    }

    @Test
    void testGetProfileById_notFound() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.getProfileById(1L));
    }



    @Test
    void testSearchUsers_success() {

        User user = new User();
        user.setId(1L);
        user.setUsername("chandu");
        user.setEmail("chandu@gmail.com");

        when(userRepository.findByUsernameContainingIgnoreCase("cha"))
                .thenReturn(List.of(user));

        List<UserResponse> result = service.searchUsers("cha");

        assertEquals(1, result.size());
        assertEquals("chandu", result.get(0).getUsername());
    }

    @Test
    void testSearchUsers_empty() {

        when(userRepository.findByUsernameContainingIgnoreCase("xyz"))
                .thenReturn(List.of());

        List<UserResponse> result = service.searchUsers("xyz");

        assertTrue(result.isEmpty());
    }
}