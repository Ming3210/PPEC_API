package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.CourseOffRequest;
import com.ra.base_spring_boot.dto.request.CourseOffRequestDTO;
import com.ra.base_spring_boot.dto.request.CourseOffSearchFilterDTO;
import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.dto.response.CourseOffResponseDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.service.interfaces.ICourseOffService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/courses-off")
@RequiredArgsConstructor
public class CourseOffController {

    private final ICourseOffService courseOffService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<PaginationResponse<CourseOffResponseDTO>>> getAllCoursesOff(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        PaginationResponse<CourseOffResponseDTO> result = courseOffService.getAllCoursesOff(page, size, sortBy, sortDirection);

        ResponseWrapper<PaginationResponse<CourseOffResponseDTO>> response =
                ResponseWrapper.<PaginationResponse<CourseOffResponseDTO>>builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(result)
                        .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<CourseOffResponseDTO>> getCourseOffById(@PathVariable Long id) {
        CourseOffResponseDTO courseOff = courseOffService.getCourseOffById(id);

        ResponseWrapper<CourseOffResponseDTO> response = ResponseWrapper.<CourseOffResponseDTO>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(courseOff)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseWrapper<PaginationResponse<CourseOffResponseDTO>>> searchCoursesOff(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String targetAudience,
            @RequestParam(required = false) Long centerId,
            @RequestParam(required = false) BigDecimal priceFrom,
            @RequestParam(required = false) BigDecimal priceTo,
            @RequestParam(required = false) Integer estimatedHoursFrom,
            @RequestParam(required = false) Integer estimatedHoursTo,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        CourseOffSearchFilterDTO filterDTO = new CourseOffSearchFilterDTO();
        filterDTO.setName(name);

        if (targetAudience != null && !targetAudience.trim().isEmpty()) {
            try {
                filterDTO.setTargetAudience(com.ra.base_spring_boot.model.constants.TargetAudience.valueOf(targetAudience.toUpperCase()));
            } catch (IllegalArgumentException e) {
            }
        }

        filterDTO.setCenterId(centerId);
        filterDTO.setPriceFrom(priceFrom);
        filterDTO.setPriceTo(priceTo);
        filterDTO.setEstimatedHoursFrom(estimatedHoursFrom);
        filterDTO.setEstimatedHoursTo(estimatedHoursTo);
        filterDTO.setSortBy(sortBy);
        filterDTO.setSortDirection(sortDirection);
        filterDTO.setPage(page);
        filterDTO.setSize(size);

        PaginationResponse<CourseOffResponseDTO> result = courseOffService.searchAndFilterCoursesOff(filterDTO);

        ResponseWrapper<PaginationResponse<CourseOffResponseDTO>> response =
                ResponseWrapper.<PaginationResponse<CourseOffResponseDTO>>builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(result)
                        .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<APIResponse<CourseOffResponseDTO>> addCourseOff(@Valid @RequestBody CourseOffRequestDTO courseOffRequest){
        return new ResponseEntity<>(new APIResponse<>(
                true,
                "Thao tác thêm thành công!",
                courseOffService.createCourseOff(courseOffRequest),
                HttpStatus.CREATED,
                LocalDateTime.now().toString()
        ), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<CourseOffResponseDTO>> updateCourseOff(@PathVariable Long id, @Valid @RequestBody CourseOffRequestDTO courseOffRequest){
        return new ResponseEntity<>(new APIResponse<>(
                true,
                "Thao tác cập nhật thành công!",
                courseOffService.updateCourseOff(id, courseOffRequest),
                HttpStatus.OK,
                LocalDateTime.now().toString()
        ), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourseOff(@PathVariable Long id){
        courseOffService.deleteCourseOff(id);
        return new ResponseEntity<>(new APIResponse<>(
                true,
                "Thao tác xóa thành công!",
                null,
                HttpStatus.OK,
                LocalDateTime.now().toString()
        ), HttpStatus.OK);
    }
}
