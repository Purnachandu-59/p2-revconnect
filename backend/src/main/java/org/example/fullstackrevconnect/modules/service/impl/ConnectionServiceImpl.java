package org.example.fullstackrevconnect.modules.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.fullstackrevconnect.modules.dto.ConnectionUserDto;
import org.example.fullstackrevconnect.modules.dto.PendingRequestDto;
import org.example.fullstackrevconnect.modules.entity.Connection;
import org.example.fullstackrevconnect.modules.entity.User;
import org.example.fullstackrevconnect.modules.repository.ConnectionRepository;
import org.example.fullstackrevconnect.modules.repository.UserRepository;
import org.example.fullstackrevconnect.modules.entity.enums.NotificationType;
import org.example.fullstackrevconnect.modules.service.ConnectionService;
import org.example.fullstackrevconnect.modules.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;


    public Connection sendRequest(Long senderId, Long receiverId) {

        if (senderId.equals(receiverId)) {
            throw new RuntimeException("You cannot connect with yourself");
        }

        if (connectionRepository
                .findBySenderIdAndReceiverId(senderId, receiverId)
                .isPresent()) {
            throw new RuntimeException("Request already exists");
        }

        if (connectionRepository
                .findBySenderIdAndReceiverId(receiverId, senderId)
                .isPresent()) {
            throw new RuntimeException("Connection already exists");
        }

        Connection connection = new Connection();
        connection.setSenderId(senderId);
        connection.setReceiverId(receiverId);
        connection.setStatus(Connection.Status.PENDING);

        Connection saved = connectionRepository.save(connection);

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        notificationService.createNotification(
                receiverId,
                senderId,
                NotificationType.CONNECTION_REQUEST,
                sender.getUsername() + " sent you a connection request",
                String.valueOf(saved.getId())
        );

        return saved;
    }


    public Connection acceptRequest(Long requestId) {

        Connection connection = connectionRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        connection.setStatus(Connection.Status.ACCEPTED);

        Connection saved = connectionRepository.save(connection);

        User receiver = userRepository.findById(connection.getReceiverId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        notificationService.createNotification(
                connection.getSenderId(),
                connection.getReceiverId(),
                NotificationType.CONNECTION_ACCEPTED,
                receiver.getUsername() + " accepted your connection request",
                String.valueOf(saved.getId())
        );

        return saved;
    }


    public void rejectRequest(Long requestId) {

        Connection connection = connectionRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        connection.setStatus(Connection.Status.REJECTED);
        connectionRepository.save(connection);
    }


    public List<PendingRequestDto> getPendingRequests(Long userId) {

        List<Connection> connections =
                connectionRepository.findByReceiverIdAndStatus(
                        userId,
                        Connection.Status.PENDING
                );

        return connections.stream()
                .map(c -> {

                    User sender = userRepository.findById(c.getSenderId())
                            .orElseThrow(() -> new RuntimeException("Sender not found"));

                    return new PendingRequestDto(
                            c.getId(),
                            sender.getId(),
                            sender.getUsername(),
                            sender.getProfileImage()
                    );
                })
                .toList();
    }


    public List<ConnectionUserDto> getConnections(Long userId) {

        List<Connection> connections =
                connectionRepository
                        .findByStatusAndSenderIdOrStatusAndReceiverId(
                                Connection.Status.ACCEPTED, userId,
                                Connection.Status.ACCEPTED, userId
                        );

        return connections.stream()
                .map(connection -> {

                    Long otherUserId =
                            connection.getSenderId().equals(userId)
                                    ? connection.getReceiverId()
                                    : connection.getSenderId();

                    User otherUser = userRepository.findById(otherUserId)
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    return new ConnectionUserDto(
                            otherUser.getId(),
                            otherUser.getUsername(),
                            otherUser.getBio(),
                            otherUser.getProfileImage()
                    );
                })
                .toList();
    }


    public List<Long> getSentRequests(Long userId) {

        return connectionRepository
                .findBySenderIdAndStatus(userId, Connection.Status.PENDING)
                .stream()
                .map(Connection::getReceiverId)
                .toList();
    }
}