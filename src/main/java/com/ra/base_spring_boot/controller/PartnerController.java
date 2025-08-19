package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.PartnerDTO;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.dto.response.GetDetailPartnerResponse;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.dto.response.PartnerResponseDTO;
import com.ra.base_spring_boot.service.interfaces.IPartnerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/partners")
public class PartnerController {
    @Autowired
    private IPartnerService partnerService;
    @PostMapping
    public ResponseEntity<APIResponse<PartnerResponseDTO>> createPartner(@Valid @ModelAttribute PartnerDTO partnerDTO) {
        PartnerResponseDTO createdPartner = partnerService.createPartner(partnerDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(true, "Thêm đối tác thành công !!!", createdPartner, HttpStatus.CREATED, LocalDateTime.now()));
    }
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<PartnerResponseDTO>> updatePartner(@PathVariable Long id, @Valid @ModelAttribute PartnerDTO partnerDTO) {
        PartnerResponseDTO updatedPartner = partnerService.updatePartner(id, partnerDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new APIResponse<>(true, "Cập nhật đối tác thành công !!!", updatedPartner, HttpStatus.OK, LocalDateTime.now()));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> deletePartner(@PathVariable Long id) {
        partnerService.deletePartner(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new APIResponse<>(true, "Xóa đối tác thành công !!!", "Đối tác đã được xóa", HttpStatus.OK, LocalDateTime.now()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<PartnerResponseDTO>> getPartnerById(@PathVariable Long id) {
        PartnerResponseDTO partner = partnerService.getPartnerById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new APIResponse<>(true, "Lấy thông tin đối tác thành công !!!", partner, HttpStatus.OK, LocalDateTime.now()));
    }
    @GetMapping
    public ResponseEntity<APIResponse<PaginationResponse<PartnerResponseDTO>>> getPartners(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PaginationResponse<PartnerResponseDTO> result = partnerService.searchPartners(keyword, page, size);
        APIResponse<PaginationResponse<PartnerResponseDTO>> apiResponse = APIResponse.<PaginationResponse<PartnerResponseDTO>>builder()
                .status(true)
                .message("Lấy danh sách đối tác thành công")
                .data(result)
                .httpStatus(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/{id}/students")
    public ResponseEntity<APIResponse<GetDetailPartnerResponse>> getStudentsByPartnerId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Lấy chi tiết đối tác thành công !!!",
                        partnerService.getDetailPartner(Math.toIntExact(id)),
                        HttpStatus.OK,
                        LocalDateTime.now()
                )
        );
    }


}
