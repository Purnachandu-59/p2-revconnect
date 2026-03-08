package org.example.fullstackrevconnect.modules.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.fullstackrevconnect.modules.dto.AuthResponse;
import org.example.fullstackrevconnect.modules.dto.LoginRequest;
import org.example.fullstackrevconnect.modules.dto.RegisterRequest;
import org.example.fullstackrevconnect.modules.entity.enums.Role;
import org.example.fullstackrevconnect.modules.entity.User;
import org.example.fullstackrevconnect.modules.repository.UserRepository;
import org.example.fullstackrevconnect.modules.service.AuthService;
import org.example.fullstackrevconnect.common.security.CustomUserDetailsService;
import org.example.fullstackrevconnect.common.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;


    @Override
    public void register(RegisterRequest request) {

        Role finalRole;

        if (request.getRole() == null) {
            finalRole = Role.USER;
        } else {
            finalRole = request.getRole();
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(finalRole)
                .securityQuestion(request.getSecurityQuestion())
                .securityAnswer(passwordEncoder.encode(request.getSecurityAnswer()))
                .build();

        userRepository.save(user);
    }


    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.getUsername());

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(
                userDetails,
                user.getRole().name()
        );

        return new AuthResponse(
                token,
                user.getRole().name()
        );
    }

    @Override
    public String getSecurityQuestion(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getSecurityQuestion() == null) {
            throw new RuntimeException("Security question not set for this user");
        }

        return user.getSecurityQuestion().getQuestion();
    }

    @Override
    public void resetPasswordBySecurityQuestion(
            String username,
            String answer,
            String newPassword) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean matches = passwordEncoder.matches(
                answer,
                user.getSecurityAnswer()
        );

        if (!matches) {
            throw new RuntimeException("Incorrect answer");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}