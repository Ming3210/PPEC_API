package com.ra.base_spring_boot.controller;


import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.request.CenterRequestDTO;
import com.ra.base_spring_boot.dto.response.CenterResponseDTO;
import com.ra.base_spring_boot.service.interfaces.ICenterService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/centers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CenterController {

    private final ICenterService centerService;

    @PostMapping
    public ResponseEntity<ResponseWrapper<CenterResponseDTO>> createCenter(
            @Valid @ModelAttribute CenterRequestDTO centerRequestDTO) {

        CenterResponseDTO createdCenter = centerService.createCenter(centerRequestDTO);

        ResponseWrapper<CenterResponseDTO> response = ResponseWrapper.<CenterResponseDTO>builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .data(createdCenter)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<CenterResponseDTO>> getCenterById(@PathVariable Long id) {
        CenterResponseDTO center = centerService.getCenterById(id);

        ResponseWrapper<CenterResponseDTO> response = ResponseWrapper.<CenterResponseDTO>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(center)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<CenterResponseDTO>> updateCenter(
            @PathVariable Long id,
            @Valid @ModelAttribute CenterRequestDTO centerRequestDTO) {

        CenterResponseDTO updatedCenter = centerService.updateCenter(id, centerRequestDTO);

        ResponseWrapper<CenterResponseDTO> response = ResponseWrapper.<CenterResponseDTO>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(updatedCenter)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<String>> deleteCenter(@PathVariable Long id) {
        centerService.deleteCenter(id);

        ResponseWrapper<String> response = ResponseWrapper.<String>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data("Xóa trung tâm thành công")
                .build();

        return ResponseEntity.ok(response);
    }
    // chưa kiểm tra id có tồn tại hay
    @GetMapping("/{id}/can-delete")
    public ResponseEntity<ResponseWrapper<Map<String, Object>>> checkCanDelete(@PathVariable Long id) {
        boolean canDelete = centerService.canDeleteCenter(id);

        Map<String, Object> result = new HashMap<>();
        result.put("canDelete", canDelete);
        result.put("message", canDelete ?
                "Có thể xóa trung tâm" :
                "Không thể xóa trung tâm vì có khóa học đang sử dụng");

        ResponseWrapper<Map<String, Object>> response = ResponseWrapper.<Map<String, Object>>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(result)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<CenterResponseDTO>>> getAllCenters() {
        List<CenterResponseDTO> centers = centerService.getAllCenters();

        ResponseWrapper<List<CenterResponseDTO>> response = ResponseWrapper.<List<CenterResponseDTO>>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(centers)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseWrapper<Page<CenterResponseDTO>>> searchCenters(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<CenterResponseDTO> centers = centerService.searchCenters(keyword, page, size);

        ResponseWrapper<Page<CenterResponseDTO>> response = ResponseWrapper.<Page<CenterResponseDTO>>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(centers)
                .build();

        return ResponseEntity.ok(response);
    }
}
