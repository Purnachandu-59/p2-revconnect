package org.example.fullstackrevconnect.modules.controller;

import lombok.RequiredArgsConstructor;
import org.example.fullstackrevconnect.modules.dto.ConnectionUserDto;
import org.example.fullstackrevconnect.modules.dto.PendingRequestDto;
import org.example.fullstackrevconnect.modules.entity.Connection;
import org.example.fullstackrevconnect.modules.entity.User;
import org.example.fullstackrevconnect.modules.service.ConnectionService;
import org.example.fullstackrevconnect.modules.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connections")
@RequiredArgsConstructor
@CrossOrigin
public class ConnectionController {

    private final ConnectionService connectionService;
    private final UserService userService;


    @PostMapping("/request/{receiverId}")
    public ResponseEntity<Connection> sendRequest(
            @PathVariable Long receiverId,
            Authentication authentication) {

        User sender = userService.getUserByUsername(authentication.getName());

        Connection connection =
                connectionService.sendRequest(sender.getId(), receiverId);

        return ResponseEntity.ok(connection);
    }


    @PutMapping("/accept/{requestId}")
    public ResponseEntity<Connection> acceptRequest(
            @PathVariable Long requestId) {

        return ResponseEntity.ok(
                connectionService.acceptRequest(requestId)
        );
    }


    @PutMapping("/reject/{requestId}")
    public ResponseEntity<Void> rejectRequest(
            @PathVariable Long requestId) {

        connectionService.rejectRequest(requestId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/pending")
    public ResponseEntity<List<PendingRequestDto>> getPending(
            Authentication authentication) {

        User user = userService.getUserByUsername(authentication.getName());

        return ResponseEntity.ok(
                connectionService.getPendingRequests(user.getId())
        );
    }


    @GetMapping("/my")
    public List<ConnectionUserDto> getMyConnections(
            Authentication authentication) {

        User user = userService.getUserByUsername(authentication.getName());

        return connectionService.getConnections(user.getId());
    }

    @GetMapping("/sent")
    public ResponseEntity<List<Long>> getSentRequests(Authentication authentication) {

        User user = userService.getUserByUsername(authentication.getName());

        return ResponseEntity.ok(
                connectionService.getSentRequests(user.getId())
        );
    }
}