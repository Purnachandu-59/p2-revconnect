package org.example.fullstackrevconnect.modules.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConnectionUserDto {

    private Long userId;
    private String username;
    private String bio;
    private String profileImage;
}
