package com.ra.base_spring_boot.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrivateMessageRequest {

    @NotNull(message = "Sender ID is required")
    private Long senderId;

    @NotNull(message = "Receiver ID is required")
    private Long receiverId;

    @NotBlank(message = "Content is required")
    private String content;

    @NotBlank(message = "Sender username is required")
    private String senderUsername;

    @NotBlank(message = "Receiver username is required")
    private String receiverUsername;
}

