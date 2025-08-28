package com.ra.base_spring_boot.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.ra.base_spring_boot.dto.request.PrivateMessageRequest;
import com.ra.base_spring_boot.security.jwt.JWTProvider;
import com.ra.base_spring_boot.service.interfaces.IPrivateMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class PrivateMessageSocketModule {

    private final SocketIOServer server;
    private final IPrivateMessageService privateMessageService;
    private final JWTProvider jwtProvider;
    @PostConstruct
    private void init() {
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_private_message", PrivateMessageRequest.class, onPrivateMessageReceived());
        server.addEventListener("join_private_chat", Long.class, onJoinPrivateChat());
        server.addEventListener("mark_as_read", Long.class, onMarkAsRead());
    }

    private DataListener<PrivateMessageRequest> onPrivateMessageReceived() {
        return (client, data, ackSender) -> {
            log.info("Received private message: {}", data);

            // Client phải gửi kèm auth token hoặc userId trong handshake
            String userIdStr = client.get("userId");
            if (userIdStr == null) {
                client.sendEvent("error", "User not authenticated");
                return;
            }

            // Set userId vào SecurityContext (nếu cần)
            // hoặc truyền trực tiếp vào service
            privateMessageService.sendPrivateMessage(client, data);
        };
    }


    private DataListener<Long> onJoinPrivateChat() {
        return (client, userId, ackSender) -> {
            // Join room riêng cho user để nhận tin nhắn
            client.joinRoom("user_" + userId);

            // Gửi tin nhắn chưa đọc
            var unreadMessages = privateMessageService.getUnreadMessages(userId);
            client.sendEvent("unread_messages", unreadMessages);

            log.info("User {} joined private chat", userId);
        };
    }

    private DataListener<Long> onMarkAsRead() {
        return (client, messageId, ackSender) -> {
            privateMessageService.markAsRead(messageId);
            log.info("Message {} marked as read", messageId);
        };
    }

    private ConnectListener onConnected() {
        return client -> {
            var params = client.getHandshakeData().getUrlParams();
            String token = params.get("token").stream().collect(Collectors.joining());


            if (token != null && jwtProvider.validateToken(token)) {
                String username = jwtProvider.getUsernameFromToken(token);
                // Có thể lấy thêm userId từ token nếu cần

                client.set("username", username);
                client.set("token", token);

                log.info("User {} connected via Socket.IO", username);
            } else {
                client.sendEvent("auth_error", "Invalid or missing token");
                client.disconnect();
            }
        };
    }



    private DisconnectListener onDisconnected() {
        return client -> {
            String userId = client.get("userId");
            String username = client.get("username");

            log.info("User disconnected - ID: {}, Username: {}", userId, username);
        };
    }
}

