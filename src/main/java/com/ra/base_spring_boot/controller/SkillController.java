package com.ra.base_spring_boot.controller;


import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.request.SkillRequestDTO;
import com.ra.base_spring_boot.dto.request.SkillSearchFilterDTO;
import com.ra.base_spring_boot.dto.response.SkillResponseDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.service.interfaces.ISkillService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/skills")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class SkillController {

    private final ISkillService skillService;

    @PostMapping
    public ResponseEntity<ResponseWrapper<SkillResponseDTO>> createSkill(
            @Valid @RequestBody SkillRequestDTO skillRequestDTO) {

        SkillResponseDTO createdSkill = skillService.createSkill(skillRequestDTO);

        ResponseWrapper<SkillResponseDTO> response = ResponseWrapper.<SkillResponseDTO>builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .data(createdSkill)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<SkillResponseDTO>> getSkillById(@PathVariable Long id) {
        SkillResponseDTO skill = skillService.getSkillById(id);

        ResponseWrapper<SkillResponseDTO> response = ResponseWrapper.<SkillResponseDTO>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(skill)
                .build();

        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<SkillResponseDTO>> updateSkill(
            @PathVariable Long id,
            @Valid @RequestBody SkillRequestDTO skillRequestDTO) {

        SkillResponseDTO updatedSkill = skillService.updateSkill(id, skillRequestDTO);

        ResponseWrapper<SkillResponseDTO> response = ResponseWrapper.<SkillResponseDTO>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(updatedSkill)
                .build();

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<String>> deleteSkill(@PathVariable Long id) {
        skillService.deleteSkill(id);

        ResponseWrapper<String> response = ResponseWrapper.<String>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data("Xóa kỹ năng thành công")
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}/can-delete")
    public ResponseEntity<ResponseWrapper<Map<String, Object>>> checkCanDelete(@PathVariable Long id) {
        boolean canDelete = skillService.canDeleteSkill(id);

        Map<String, Object> result = new HashMap<>();
        result.put("canDelete", canDelete);
        result.put("message", canDelete ?
                "Có thể xóa kỹ năng" :
                "Không thể xóa kỹ năng vì có khóa học đang sử dụng");

        ResponseWrapper<Map<String, Object>> response = ResponseWrapper.<Map<String, Object>>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(result)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<PaginationResponse<SkillResponseDTO>>> getAllSkills(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        PaginationResponse<SkillResponseDTO> result = skillService.getAllSkills(page, size, sortBy, sortDirection);

        ResponseWrapper<PaginationResponse<SkillResponseDTO>> response =
                ResponseWrapper.<PaginationResponse<SkillResponseDTO>>builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(result)
                        .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/search")
    public ResponseEntity<ResponseWrapper<PaginationResponse<SkillResponseDTO>>> searchAndFilterSkills(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        SkillSearchFilterDTO filterDTO = new SkillSearchFilterDTO();
        filterDTO.setName(name);
        filterDTO.setSortBy(sortBy);
        filterDTO.setSortDirection(sortDirection);
        filterDTO.setPage(page);
        filterDTO.setSize(size);

        PaginationResponse<SkillResponseDTO> result = skillService.searchAndFilterSkills(filterDTO);

        ResponseWrapper<PaginationResponse<SkillResponseDTO>> response =
                ResponseWrapper.<PaginationResponse<SkillResponseDTO>>builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(result)
                        .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/select")
    public ResponseEntity<ResponseWrapper<List<SkillResponseDTO>>> getAllSkillsForSelect() {
        List<SkillResponseDTO> skills = skillService.getAllSkillsForSelect();

        ResponseWrapper<List<SkillResponseDTO>> response = ResponseWrapper.<List<SkillResponseDTO>>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(skills)
                .build();

        return ResponseEntity.ok(response);
    }
}
