package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.model.PrivateMessage;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.repository.UserRepository;
import com.ra.base_spring_boot.service.interfaces.IPrivateMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/private-messages")
@RequiredArgsConstructor
public class PrivateMessageController {

    private final IPrivateMessageService privateMessageService;
    private final UserRepository UserRepository;
    @GetMapping("/my-conversations/{otherUserId}")
    public ResponseEntity<ResponseWrapper<List<PrivateMessage>>> getMyConversation(
            @PathVariable Long otherUserId,
            @AuthenticationPrincipal UserDetails currentUser) {

        // Lấy userId từ currentUser (cần implement)
        Long currentUserId = getCurrentUserIdFromUserDetails(currentUser);

        List<PrivateMessage> conversation = privateMessageService
                .getConversation(currentUserId, otherUserId);

        ResponseWrapper<List<PrivateMessage>> response = ResponseWrapper
                .<List<PrivateMessage>>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(conversation)
                .build();

        return ResponseEntity.ok(response);
    }

    private Long getCurrentUserIdFromUserDetails(UserDetails userDetails) {

        return UserRepository.findByUsername(userDetails.getUsername())
                .map(User::getId)
                .orElse(null);
    }
}
