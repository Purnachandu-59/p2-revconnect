package org.example.fullstackrevconnect.service;

import org.example.fullstackrevconnect.common.security.CustomUserDetailsService;
import org.example.fullstackrevconnect.common.security.JwtUtil;
import org.example.fullstackrevconnect.modules.dto.*;
import org.example.fullstackrevconnect.modules.entity.User;
import org.example.fullstackrevconnect.modules.entity.enums.Role;
import org.example.fullstackrevconnect.modules.entity.enums.SecurityQuestion;
import org.example.fullstackrevconnect.modules.repository.UserRepository;
import org.example.fullstackrevconnect.modules.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @InjectMocks
    private AuthServiceImpl authService;



    @Test
    void testRegister_defaultRole() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("chandu");
        request.setEmail("chandu@gmail.com");
        request.setPassword("123");
        request.setSecurityQuestion(null);
        request.setSecurityAnswer("answer");

        when(passwordEncoder.encode(anyString())).thenReturn("encoded");

        authService.register(request);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegister_withCustomRole() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("chandu");
        request.setEmail("chandu@gmail.com");
        request.setPassword("123");
        request.setRole(Role.USER);
        request.setSecurityAnswer("answer");

        when(passwordEncoder.encode(anyString())).thenReturn("encoded");

        authService.register(request);

        verify(userRepository).save(any(User.class));
    }



    @Test
    void testLogin_success() {

        LoginRequest request = new LoginRequest();
        request.setUsername("chandu");
        request.setPassword("123");

        User user = User.builder()
                .username("chandu")
                .role(Role.USER)
                .build();

        UserDetails userDetails = mock(UserDetails.class);

        when(userDetailsService.loadUserByUsername("chandu"))
                .thenReturn(userDetails);

        when(userRepository.findByUsername("chandu"))
                .thenReturn(Optional.of(user));

        when(jwtUtil.generateToken(any(), anyString()))
                .thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        assertEquals("jwt-token", response.getToken());
        assertEquals("USER", response.getRole());
    }

    @Test
    void testLogin_userNotFound() {

        LoginRequest request = new LoginRequest();
        request.setUsername("chandu");
        request.setPassword("123");

        when(userDetailsService.loadUserByUsername("chandu"))
                .thenReturn(mock(UserDetails.class));

        when(userRepository.findByUsername("chandu"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> authService.login(request));
    }




    @Test
    void testGetSecurityQuestion_notSet() {

        User user = User.builder()
                .username("chandu")
                .build();

        when(userRepository.findByUsername("chandu"))
                .thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class,
                () -> authService.getSecurityQuestion("chandu"));
    }



    @Test
    void testResetPassword_success() {

        User user = User.builder()
                .username("chandu")
                .securityAnswer("encoded-answer")
                .build();

        when(userRepository.findByUsername("chandu"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("answer", "encoded-answer"))
                .thenReturn(true);

        when(passwordEncoder.encode("newPass"))
                .thenReturn("encoded-new");

        authService.resetPasswordBySecurityQuestion(
                "chandu", "answer", "newPass"
        );

        verify(userRepository).save(user);
    }

    @Test
    void testResetPassword_wrongAnswer() {

        User user = User.builder()
                .username("chandu")
                .securityAnswer("encoded-answer")
                .build();

        when(userRepository.findByUsername("chandu"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrong", "encoded-answer"))
                .thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> authService.resetPasswordBySecurityQuestion(
                        "chandu", "wrong", "newPass"
                ));
    }
}