package com.ra.base_spring_boot.controller;
import com.ra.base_spring_boot.dto.request.StaffDTO;
import com.ra.base_spring_boot.dto.response.StaffResponseDTO;
import com.ra.base_spring_boot.service.interfaces.IAccountSchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/staffs")
@RequiredArgsConstructor
public class SchoolController {

    private final IAccountSchoolService accountSchoolService;

    // ✅ Thêm nhân viên
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<StaffResponseDTO> createStaff(@ModelAttribute StaffDTO staffDTO) {
        StaffResponseDTO response = accountSchoolService.createAccountSchool(staffDTO);
        return ResponseEntity.ok(response);
    }

    // ✅ Sửa nhân viên
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<StaffResponseDTO> updateStaff(
            @PathVariable Long id,
            @ModelAttribute StaffDTO staffDTO
    ) {
        StaffResponseDTO response = accountSchoolService.updateAccountSchool(id, staffDTO);
        return ResponseEntity.ok(response);
    }

    // ✅ Xoá nhân viên
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStaff(@PathVariable Long id) {
        accountSchoolService.deleteAccountSchool(id);
        return ResponseEntity.ok("Xóa nhân viên thành công");
    }

    // ✅ Lấy chi tiết 1 nhân viên
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<StaffResponseDTO> getStaffById(@PathVariable Long id) {
        StaffResponseDTO response = accountSchoolService.getAccountSchoolById(id);
        return ResponseEntity.ok(response);
    }

    // ✅ Danh sách + phân trang + tìm kiếm theo keyword
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<StaffResponseDTO>> getAllStaffs(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<StaffResponseDTO> response = accountSchoolService.getAllAccountSchools(keyword, page, size);
        return ResponseEntity.ok(response);
    }
}

