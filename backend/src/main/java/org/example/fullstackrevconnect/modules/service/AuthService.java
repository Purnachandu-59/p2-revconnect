package org.example.fullstackrevconnect.modules.service;

import org.example.fullstackrevconnect.modules.dto.AuthResponse;

import org.example.fullstackrevconnect.modules.dto.LoginRequest;

import org.example.fullstackrevconnect.modules.dto.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    String getSecurityQuestion(String username);

    void resetPasswordBySecurityQuestion(

            String username,

            String answer,

            String newPassword

    );

}
