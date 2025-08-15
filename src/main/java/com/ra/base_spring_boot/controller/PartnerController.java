package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.PartnerDTO;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.model.Partner;
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
    public ResponseEntity<APIResponse<Partner>> createPartner(@Valid @ModelAttribute PartnerDTO partnerDTO) {
        Partner createdPartner = partnerService.createPartner(partnerDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(true, "Thêm đối tác thành công !!!", createdPartner, HttpStatus.CREATED, LocalDateTime.now().toString()));
    }
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<Partner>> updatePartner(@PathVariable Long id, @Valid @ModelAttribute PartnerDTO partnerDTO) {
        Partner updatedPartner = partnerService.updatePartner(id, partnerDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new APIResponse<>(true, "Cập nhật đối tác thành công !!!", updatedPartner, HttpStatus.OK, LocalDateTime.now().toString()));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> deletePartner(@PathVariable Long id) {
        partnerService.deletePartner(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new APIResponse<>(true, "Xóa đối tác thành công !!!", "Đối tác đã được xóa", HttpStatus.OK, LocalDateTime.now().toString()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Partner>> getPartnerById(@PathVariable Long id) {
        Partner partner = partnerService.getPartnerById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new APIResponse<>(true, "Lấy thông tin đối tác thành công !!!", partner, HttpStatus.OK, LocalDateTime.now().toString()));
    }
    @GetMapping
    public ResponseEntity<Page<Partner>> getPartners(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Partner> response = partnerService.searchPartners(keyword, page, size);
        return ResponseEntity.ok(response);
    }
}
