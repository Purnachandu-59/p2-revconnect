package org.example.fullstackrevconnect.modules.dto;
import lombok.Data;
import org.example.fullstackrevconnect.modules.entity.enums.Role;
import org.example.fullstackrevconnect.modules.entity.enums.SecurityQuestion;

@Data
public class RegisterRequest
{
    private String username;
    private String email;
    private String password;
    private Role role;
    private SecurityQuestion securityQuestion;
    private String securityAnswer;
}
