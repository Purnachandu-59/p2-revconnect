package org.example.fullstackrevconnect.modules.repository;

import org.example.fullstackrevconnect.modules.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    Optional<Connection> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    List<Connection> findByReceiverIdAndStatus(Long receiverId, Connection.Status status);

    List<Connection> findBySenderIdAndStatus(Long senderId, Connection.Status status);

    List<Connection> findByStatusAndSenderIdOrStatusAndReceiverId(
            Connection.Status status1, Long senderId,
            Connection.Status status2, Long receiverId
    );
    long countByReceiverIdAndStatus(Long receiverId, Connection.Status status);

    long countBySenderIdAndStatus(Long senderId, Connection.Status status);

    long countBySenderIdAndStatusOrReceiverIdAndStatus(
            Long senderId,
            Connection.Status senderStatus,
            Long receiverId,
            Connection.Status receiverStatus
    );


}