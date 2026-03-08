package org.example.fullstackrevconnect.modules.service;

import org.example.fullstackrevconnect.modules.dto.ConnectionUserDto;
import org.example.fullstackrevconnect.modules.dto.PendingRequestDto;
import org.example.fullstackrevconnect.modules.entity.Connection;

import java.util.List;

public interface ConnectionService {
    public Connection sendRequest(Long senderId, Long receiverId);

    public Connection acceptRequest(Long requestId);

    public void rejectRequest(Long requestId);

    public List<PendingRequestDto> getPendingRequests(Long userId);

    public List<ConnectionUserDto> getConnections(Long userId);

    public List<Long> getSentRequests(Long userId);
}
