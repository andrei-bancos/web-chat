package com.andreibancos.webchatbackend.repository;

import com.andreibancos.webchatbackend.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface IMessageRepository extends JpaRepository<Message, UUID> {
    @Query(
        "SELECT m FROM Message m " +
        "WHERE (m.sender.id = :userSenderId " +
        "AND m.receiver.id = :userReceiverId) " +
        "OR (m.sender.id = :userReceiverId " +
        "AND m.receiver.id = :userSenderId) " +
        "ORDER BY m.createdAt ASC"
    )
    List<Message> findAllMessagesBetweenUsers(UUID userSenderId, UUID userReceiverId);

    @Query(
        "SELECT m FROM Message m WHERE m.createdAt = " +
        "(SELECT MAX(m2.createdAt) FROM Message m2 WHERE " +
        "(m2.sender.id = m.sender.id AND m2.receiver.id = m.receiver.id) OR " +
        "(m2.sender.id = m.receiver.id AND m2.receiver.id = m.sender.id)) " +
        "AND (m.sender.id = :userId OR m.receiver.id = :userId) ORDER BY m.createdAt DESC"
    )
    List<Message> findLastMessagesWithUsers(UUID userId, Pageable pageable);
}
