package org.example.fullstackrevconnect.modules.controller;

import lombok.RequiredArgsConstructor;
import org.example.fullstackrevconnect.modules.dto.*;
import org.example.fullstackrevconnect.modules.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        authService.register(request);

        return ResponseEntity.ok().body(
                Map.of("message", "User registered successfully")
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/security-question")
    public ResponseEntity<?> getQuestion(
            @RequestBody SecurityQuestionRequest request) {

        String question = authService.getSecurityQuestion(request.getUsername());
        return ResponseEntity.ok(question);
    }

    @PostMapping("/reset-by-security")
    public ResponseEntity<?> resetPassword(
            @RequestBody ResetBySecurityRequest request) {

        authService.resetPasswordBySecurityQuestion(
                request.getUsername(),
                request.getAnswer(),
                request.getNewPassword()
        );

        return ResponseEntity.ok("Password updated successfully");
    }
}

