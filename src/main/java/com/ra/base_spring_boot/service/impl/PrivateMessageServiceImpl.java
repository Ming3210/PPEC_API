package com.ra.base_spring_boot.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.ra.base_spring_boot.dto.request.PrivateMessageRequest;
import com.ra.base_spring_boot.model.PrivateMessage;
import com.ra.base_spring_boot.repository.PrivateMessageRepository;
import com.ra.base_spring_boot.service.interfaces.IPrivateMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrivateMessageServiceImpl implements IPrivateMessageService {

    private final PrivateMessageRepository privateMessageRepository;

    @Override
    public void sendPrivateMessage(SocketIOClient client, PrivateMessageRequest messageRequest) {
        PrivateMessage message = PrivateMessage.builder()
                .content(messageRequest.getContent())
                .senderId(messageRequest.getSenderId())
                .receiverId(messageRequest.getReceiverId())
                .senderUsername(messageRequest.getSenderUsername())
                .receiverUsername(messageRequest.getReceiverUsername())
                .isRead(false)
                .timestamp(LocalDateTime.now())
                .build();

        PrivateMessage savedMessage = privateMessageRepository.save(message);

        // Gửi tin nhắn đến người nhận (nếu online)
        client.getNamespace().getBroadcastOperations()
                .sendEvent("private_message_" + messageRequest.getReceiverId(), savedMessage);

        // Confirm gửi thành công cho người gửi
        client.sendEvent("message_sent", savedMessage);

        log.info("Private message sent from {} to {}: {}",
                messageRequest.getSenderUsername(),
                messageRequest.getReceiverUsername(),
                messageRequest.getContent());
    }

    @Override
    public List<PrivateMessage> getConversation(Long userId1, Long userId2) {
        return privateMessageRepository.findConversationBetweenUsers(userId1, userId2);
    }

    @Override
    public List<PrivateMessage> getUnreadMessages(Long userId) {
        return privateMessageRepository.findByReceiverIdAndIsReadFalse(userId);
    }

    @Override
    public void markAsRead(Long messageId) {
        privateMessageRepository.findById(messageId).ifPresent(message -> {
            message.setIsRead(true);
            privateMessageRepository.save(message);
        });
    }

    @Override
    public void markConversationAsRead(Long senderId, Long receiverId) {
        List<PrivateMessage> unreadMessages = privateMessageRepository
                .findConversationBetweenUsers(senderId, receiverId)
                .stream()
                .filter(msg -> !msg.getIsRead() && msg.getReceiverId().equals(receiverId))
                .toList();

        unreadMessages.forEach(msg -> msg.setIsRead(true));
        privateMessageRepository.saveAll(unreadMessages);
    }
}
