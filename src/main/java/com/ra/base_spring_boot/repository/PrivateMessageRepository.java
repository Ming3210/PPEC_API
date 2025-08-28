package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {

    @Query("SELECT pm FROM PrivateMessage pm WHERE " +
            "(pm.senderId = ?1 AND pm.receiverId = ?2) OR " +
            "(pm.senderId = ?2 AND pm.receiverId = ?1) " +
            "ORDER BY pm.timestamp ASC")
    List<PrivateMessage> findConversationBetweenUsers(Long userId1, Long userId2);

    @Query("SELECT pm FROM PrivateMessage pm WHERE " +
            "(pm.senderId = ?1 AND pm.receiverId = ?2) OR " +
            "(pm.senderId = ?2 AND pm.receiverId = ?1) " +
            "ORDER BY pm.timestamp DESC")
    List<PrivateMessage> findTop20ConversationBetweenUsers(Long userId1, Long userId2,
                                                           org.springframework.data.domain.Pageable pageable);

    List<PrivateMessage> findByReceiverIdAndIsReadFalse(Long receiverId);
}
