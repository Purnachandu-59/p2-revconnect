package org.example.fullstackrevconnect.service;

import org.example.fullstackrevconnect.modules.dto.ConnectionUserDto;
import org.example.fullstackrevconnect.modules.dto.PendingRequestDto;
import org.example.fullstackrevconnect.modules.entity.Connection;
import org.example.fullstackrevconnect.modules.entity.User;
import org.example.fullstackrevconnect.modules.entity.enums.NotificationType;
import org.example.fullstackrevconnect.modules.repository.ConnectionRepository;
import org.example.fullstackrevconnect.modules.repository.UserRepository;
import org.example.fullstackrevconnect.modules.service.NotificationService;
import org.example.fullstackrevconnect.modules.service.impl.ConnectionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConnectionServiceImplTest {

    @Mock
    private ConnectionRepository connectionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ConnectionServiceImpl service;



    @Test
    void testSendRequest_success() {

        Long senderId = 1L;
        Long receiverId = 2L;

        when(connectionRepository.findBySenderIdAndReceiverId(senderId, receiverId))
                .thenReturn(Optional.empty());

        when(connectionRepository.findBySenderIdAndReceiverId(receiverId, senderId))
                .thenReturn(Optional.empty());

        Connection savedConnection = new Connection();
        savedConnection.setId(100L);
        savedConnection.setSenderId(senderId);
        savedConnection.setReceiverId(receiverId);
        savedConnection.setStatus(Connection.Status.PENDING);

        when(connectionRepository.save(any(Connection.class)))
                .thenReturn(savedConnection);

        User sender = new User();
        sender.setId(senderId);
        sender.setUsername("chandu");

        when(userRepository.findById(senderId))
                .thenReturn(Optional.of(sender));

        Connection result = service.sendRequest(senderId, receiverId);

        assertEquals(Connection.Status.PENDING, result.getStatus());
        verify(notificationService).createNotification(
                eq(receiverId),
                eq(senderId),
                eq(NotificationType.CONNECTION_REQUEST),
                contains("chandu"),
                eq("100")
        );
    }

    @Test
    void testSendRequest_selfConnection() {

        assertThrows(RuntimeException.class,
                () -> service.sendRequest(1L, 1L));
    }

    @Test
    void testSendRequest_alreadyExists() {

        when(connectionRepository.findBySenderIdAndReceiverId(1L, 2L))
                .thenReturn(Optional.of(new Connection()));

        assertThrows(RuntimeException.class,
                () -> service.sendRequest(1L, 2L));
    }



    @Test
    void testAcceptRequest_success() {

        Connection connection = new Connection();
        connection.setId(10L);
        connection.setSenderId(1L);
        connection.setReceiverId(2L);
        connection.setStatus(Connection.Status.PENDING);

        when(connectionRepository.findById(10L))
                .thenReturn(Optional.of(connection));

        when(connectionRepository.save(any(Connection.class)))
                .thenReturn(connection);

        User receiver = new User();
        receiver.setId(2L);
        receiver.setUsername("receiver");

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(receiver));

        Connection result = service.acceptRequest(10L);

        assertEquals(Connection.Status.ACCEPTED, result.getStatus());

        verify(notificationService).createNotification(
                eq(1L),
                eq(2L),
                eq(NotificationType.CONNECTION_ACCEPTED),
                contains("receiver"),
                eq("10")
        );
    }

    @Test
    void testAcceptRequest_notFound() {

        when(connectionRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.acceptRequest(10L));
    }



    @Test
    void testRejectRequest_success() {

        Connection connection = new Connection();
        connection.setId(10L);

        when(connectionRepository.findById(10L))
                .thenReturn(Optional.of(connection));

        service.rejectRequest(10L);

        assertEquals(Connection.Status.REJECTED, connection.getStatus());
        verify(connectionRepository).save(connection);
    }



    @Test
    void testGetPendingRequests_success() {

        Connection connection = new Connection();
        connection.setId(1L);
        connection.setSenderId(5L);
        connection.setReceiverId(2L);

        when(connectionRepository.findByReceiverIdAndStatus(2L, Connection.Status.PENDING))
                .thenReturn(List.of(connection));

        User sender = new User();
        sender.setId(5L);
        sender.setUsername("chandu");
        sender.setProfileImage("img.jpg");

        when(userRepository.findById(5L))
                .thenReturn(Optional.of(sender));

        List<PendingRequestDto> result = service.getPendingRequests(2L);

        assertEquals(1, result.size());
        assertEquals("chandu", result.get(0).getSenderUsername());
    }



    @Test
    void testGetConnections_success() {

        Connection connection = new Connection();
        connection.setSenderId(1L);
        connection.setReceiverId(2L);
        connection.setStatus(Connection.Status.ACCEPTED);

        when(connectionRepository
                .findByStatusAndSenderIdOrStatusAndReceiverId(
                        Connection.Status.ACCEPTED, 1L,
                        Connection.Status.ACCEPTED, 1L))
                .thenReturn(List.of(connection));

        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("friend");
        otherUser.setBio("bio");
        otherUser.setProfileImage("img.jpg");

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(otherUser));

        List<ConnectionUserDto> result = service.getConnections(1L);

        assertEquals(1, result.size());
        assertEquals("friend", result.get(0).getUsername());
    }


    @Test
    void testGetSentRequests_success() {

        Connection connection = new Connection();
        connection.setReceiverId(5L);

        when(connectionRepository.findBySenderIdAndStatus(1L, Connection.Status.PENDING))
                .thenReturn(List.of(connection));

        List<Long> result = service.getSentRequests(1L);

        assertEquals(1, result.size());
        assertEquals(5L, result.get(0));
    }
}