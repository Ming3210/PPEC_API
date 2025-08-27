package com.ra.base_spring_boot.service.interfaces;

import com.corundumstudio.socketio.SocketIOClient;
import com.ra.base_spring_boot.dto.request.PrivateMessageRequest;
import com.ra.base_spring_boot.model.PrivateMessage;

import java.util.List;

public interface IPrivateMessageService {
    void sendPrivateMessage(SocketIOClient client, PrivateMessageRequest messageRequest);
    List<PrivateMessage> getConversation(Long userId1, Long userId2);
    List<PrivateMessage> getUnreadMessages(Long userId);
    void markAsRead(Long messageId);
    void markConversationAsRead(Long senderId, Long receiverId);
}
