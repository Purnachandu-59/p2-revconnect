package org.example.fullstackrevconnect.modules.dto;

import lombok.Data;

@Data
public class ResetBySecurityRequest {
    private String username;
    private String answer;
    private String newPassword;
}