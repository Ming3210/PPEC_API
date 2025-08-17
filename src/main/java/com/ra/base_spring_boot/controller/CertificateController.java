package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;

import com.ra.base_spring_boot.dto.request.CertificateRequestDTO;
import com.ra.base_spring_boot.dto.request.CertificateSearchFilterDTO;
import com.ra.base_spring_boot.dto.response.CertificateResponseDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.service.interfaces.ICertificateService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/certificates")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CertificateController {

    private final ICertificateService certificateService;

    @PostMapping
    public ResponseEntity<ResponseWrapper<CertificateResponseDTO>> createCertificate(
            @Valid @RequestBody CertificateRequestDTO certificateRequestDTO) {

        CertificateResponseDTO createdCertificate = certificateService.createCertificate(certificateRequestDTO);

        ResponseWrapper<CertificateResponseDTO> response = ResponseWrapper.<CertificateResponseDTO>builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .data(createdCertificate)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<CertificateResponseDTO>> getCertificateById(@PathVariable Long id) {
        CertificateResponseDTO certificate = certificateService.getCertificateById(id);

        ResponseWrapper<CertificateResponseDTO> response = ResponseWrapper.<CertificateResponseDTO>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(certificate)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<CertificateResponseDTO>> updateCertificate(
            @PathVariable Long id,
            @Valid @RequestBody CertificateRequestDTO certificateRequestDTO) {

        CertificateResponseDTO updatedCertificate = certificateService.updateCertificate(id, certificateRequestDTO);

        ResponseWrapper<CertificateResponseDTO> response = ResponseWrapper.<CertificateResponseDTO>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(updatedCertificate)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<String>> deleteCertificate(@PathVariable Long id) {
        certificateService.deleteCertificate(id);

        ResponseWrapper<String> response = ResponseWrapper.<String>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data("Xóa chứng chỉ thành công")
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}/can-delete")
    public ResponseEntity<ResponseWrapper<Map<String, Object>>> checkCanDelete(@PathVariable Long id) {
        boolean canDelete = certificateService.canDeleteCertificate(id);

        Map<String, Object> result = new HashMap<>();
        result.put("canDelete", canDelete);
        result.put("message", canDelete ? "Có thể xóa chứng chỉ" : "Không thể xóa chứng chỉ vì có dữ liệu liên quan");

        ResponseWrapper<Map<String, Object>> response = ResponseWrapper.<Map<String, Object>>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(result)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<PaginationResponse<CertificateResponseDTO>>> getAllCertificates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        PaginationResponse<CertificateResponseDTO> result = certificateService.getAllCertificates(page, size, sortBy, sortDirection);

        ResponseWrapper<PaginationResponse<CertificateResponseDTO>> response =
                ResponseWrapper.<PaginationResponse<CertificateResponseDTO>>builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(result)
                        .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/search")
    public ResponseEntity<ResponseWrapper<PaginationResponse<CertificateResponseDTO>>> searchAndFilterCertificates(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDateTime examDateFrom,
            @RequestParam(required = false) LocalDateTime examDateTo,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        CertificateSearchFilterDTO filterDTO = new CertificateSearchFilterDTO();
        filterDTO.setCode(code);
        filterDTO.setName(name);
        filterDTO.setExamDateFrom(examDateFrom);
        filterDTO.setExamDateTo(examDateTo);
        filterDTO.setSortBy(sortBy);
        filterDTO.setSortDirection(sortDirection);
        filterDTO.setPage(page);
        filterDTO.setSize(size);

        PaginationResponse<CertificateResponseDTO> result = certificateService.searchAndFilterCertificates(filterDTO);

        ResponseWrapper<PaginationResponse<CertificateResponseDTO>> response =
                ResponseWrapper.<PaginationResponse<CertificateResponseDTO>>builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(result)
                        .build();

        return ResponseEntity.ok(response);
    }
}
