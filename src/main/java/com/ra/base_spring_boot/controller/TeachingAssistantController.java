package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.TeachingAssistantRequestDTO;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.dto.response.TeachingAssistantResponseDTO;
import com.ra.base_spring_boot.service.interfaces.ITeachingAssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/teaching-assistants")
@RequiredArgsConstructor
public class TeachingAssistantController {

    private final ITeachingAssistantService teachingAssistantService;

    @PostMapping
    public ResponseEntity<APIResponse<TeachingAssistantResponseDTO>> createTeachingAssistant(
            @Valid @ModelAttribute TeachingAssistantRequestDTO requestDTO) {
        TeachingAssistantResponseDTO responseDTO = teachingAssistantService.createTeachingAssistant(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                APIResponse.<TeachingAssistantResponseDTO>builder()
                        .status(true)
                        .message("Tạo trợ giảng thành công")
                        .data(responseDTO)
                        .httpStatus(HttpStatus.CREATED)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<TeachingAssistantResponseDTO>> updateTeachingAssistant(
            @PathVariable Long id,
            @Valid @ModelAttribute TeachingAssistantRequestDTO requestDTO) {
        TeachingAssistantResponseDTO responseDTO = teachingAssistantService.updateTeachingAssistant(id, requestDTO);
        return ResponseEntity.ok(
                APIResponse.<TeachingAssistantResponseDTO>builder()
                        .status(true)
                        .message("Cập nhật trợ giảng thành công")
                        .data(responseDTO)
                        .httpStatus(HttpStatus.OK)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteTeachingAssistant(@PathVariable Long id) {
        teachingAssistantService.deleteTeachingAssistant(id);
        return ResponseEntity.ok(
                APIResponse.<Void>builder()
                        .status(true)
                        .message("Xóa trợ giảng thành công")
                        .data(null)
                        .httpStatus(HttpStatus.OK)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<TeachingAssistantResponseDTO>> getTeachingAssistantById(@PathVariable Long id) {
        TeachingAssistantResponseDTO responseDTO = teachingAssistantService.getTeachingAssistantById(id);
        return ResponseEntity.ok(
                APIResponse.<TeachingAssistantResponseDTO>builder()
                        .status(true)
                        .message("Lấy thông tin trợ giảng thành công")
                        .data(responseDTO)
                        .httpStatus(HttpStatus.OK)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<APIResponse<Page<TeachingAssistantResponseDTO>>> getAllTeachingAssistants(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<TeachingAssistantResponseDTO> assistants = teachingAssistantService.getAllTeachingAssistants(keyword, page, size);
        return ResponseEntity.ok(
                APIResponse.<Page<TeachingAssistantResponseDTO>>builder()
                        .status(true)
                        .message("Lấy danh sách trợ giảng thành công")
                        .data(assistants)
                        .httpStatus(HttpStatus.OK)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
