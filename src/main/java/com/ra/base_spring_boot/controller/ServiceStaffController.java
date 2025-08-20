package com.ra.base_spring_boot.controller;
import com.ra.base_spring_boot.dto.request.ServiceStaffRequestDTO;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.dto.response.ServiceStaffResponseDTO;
import com.ra.base_spring_boot.service.interfaces.IServiceStaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/service-staffs")
public class ServiceStaffController {

    @Autowired
    private IServiceStaffService staffService;
    @PostMapping
    public ResponseEntity<APIResponse<ServiceStaffResponseDTO>> create(
            @ModelAttribute @Valid ServiceStaffRequestDTO requestDTO) {
        ServiceStaffResponseDTO staff = staffService.create(requestDTO);
        return ResponseEntity.ok(
                APIResponse.<ServiceStaffResponseDTO>builder()
                        .status(true)
                        .message("Tạo nhân viên dịch vụ thành công")
                        .data(staff)
                        .httpStatus(HttpStatus.CREATED)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<ServiceStaffResponseDTO>> update(
            @PathVariable Long id,
            @ModelAttribute @Valid ServiceStaffRequestDTO requestDTO) {
        ServiceStaffResponseDTO staff = staffService.update(id, requestDTO);
        return ResponseEntity.ok(
                APIResponse.<ServiceStaffResponseDTO>builder()
                        .status(true)
                        .message("Cập nhật nhân viên dịch vụ thành công")
                        .data(staff)
                        .httpStatus(HttpStatus.OK)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> delete(@PathVariable Long id) {
        staffService.delete(id);
        return ResponseEntity.ok(
                APIResponse.<Void>builder()
                        .status(true)
                        .message("Xóa nhân viên dịch vụ thành công")
                        .data(null)
                        .httpStatus(HttpStatus.OK)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<ServiceStaffResponseDTO>> getById(@PathVariable Long id) {
        ServiceStaffResponseDTO staff = staffService.getById(id);
        return ResponseEntity.ok(
                APIResponse.<ServiceStaffResponseDTO>builder()
                        .status(true)
                        .message("Lấy chi tiết nhân viên dịch vụ thành công")
                        .data(staff)
                        .httpStatus(HttpStatus.OK)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<APIResponse<PaginationResponse<ServiceStaffResponseDTO>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(page, size);

        PaginationResponse<ServiceStaffResponseDTO> result = staffService.getAll(keyword, pageable);

        return ResponseEntity.ok(
                APIResponse.<PaginationResponse<ServiceStaffResponseDTO>>builder()
                        .status(true)
                        .message("Lấy danh sách nhân viên dịch vụ thành công")
                        .data(result)
                        .httpStatus(HttpStatus.OK)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

}
