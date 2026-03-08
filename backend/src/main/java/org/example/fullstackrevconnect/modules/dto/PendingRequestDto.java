package org.example.fullstackrevconnect.modules.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PendingRequestDto {

    private Long connectionId;
    private Long senderId;
    private String senderUsername;
    private String senderProfileImage;
}