package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.CourseOffRequestDTO;
import com.ra.base_spring_boot.dto.request.CourseOffSearchFilterDTO;
import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.request.StudyProgramRequest;
import com.ra.base_spring_boot.dto.response.CourseOffResponseDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.dto.response.StudyProgramResponseDTO;
import com.ra.base_spring_boot.service.interfaces.ICourseOffService;

import com.ra.base_spring_boot.service.interfaces.IStudyProgramService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/courses-off")
@RequiredArgsConstructor
public class CourseOffController {

    private final ICourseOffService courseOffService;

    private final IStudyProgramService studyProgramService;

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
    public ResponseEntity<ResponseWrapper<PaginationResponse<CourseOffResponseDTO>>> searchAndFilterCoursesOff(
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapper<CourseOffResponseDTO>> createCourseOff(
            @Valid @ModelAttribute CourseOffRequestDTO courseOffRequestDTO) {

        CourseOffResponseDTO createdCourseOff = courseOffService.createCourseOff(courseOffRequestDTO);

        ResponseWrapper<CourseOffResponseDTO> response = ResponseWrapper.<CourseOffResponseDTO>builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .data(createdCourseOff)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapper<CourseOffResponseDTO>> updateCourseOff(
            @PathVariable Long id,
            @Valid @ModelAttribute CourseOffRequestDTO courseOffRequestDTO) {

        CourseOffResponseDTO updatedCourseOff = courseOffService.updateCourseOff(id, courseOffRequestDTO);

        ResponseWrapper<CourseOffResponseDTO> response = ResponseWrapper.<CourseOffResponseDTO>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(updatedCourseOff)
                .build();

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapper<String>> deleteCourseOff(@PathVariable Long id) {
        courseOffService.deleteCourseOff(id);

        ResponseWrapper<String> response = ResponseWrapper.<String>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data("Xóa khóa học offline thành công")
                .build();

        return ResponseEntity.ok(response);
    }

    //need admin role to access these endpoints
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapper<PaginationResponse<CourseOffResponseDTO>>> getCoursesOffForAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(defaultValue = "false") boolean includeInactive) {

        PaginationResponse<CourseOffResponseDTO> result = courseOffService.getAllCoursesOff(page, size, sortBy, sortDirection);

        ResponseWrapper<PaginationResponse<CourseOffResponseDTO>> response =
                ResponseWrapper.<PaginationResponse<CourseOffResponseDTO>>builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(result)
                        .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/statistics")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapper<Map<String, Object>>> getCourseOffStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalCourses", "Placeholder - implement in service");
        statistics.put("totalStudents", "Placeholder - implement in service");
        statistics.put("totalRevenue", "Placeholder - implement in service");
        statistics.put("averagePrice", "Placeholder - implement in service");

        ResponseWrapper<Map<String, Object>> response = ResponseWrapper.<Map<String, Object>>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(statistics)
                .build();

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/admin/bulk")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapper<String>> bulkDeleteCoursesOff(@RequestBody java.util.List<Long> ids) {
        for (Long id : ids) {
            courseOffService.deleteCourseOff(id);
        }

        ResponseWrapper<String> response = ResponseWrapper.<String>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data("Xóa " + ids.size() + " khóa học thành công")
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/{courseId}/study-programs")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Thêm mới chương trình học theo khóa học")
    public ResponseEntity<ResponseWrapper<StudyProgramResponseDTO>> createStudyProgram(
            @PathVariable Long courseId,
            @Valid @RequestBody StudyProgramRequest studyProgramRequest
    ){
        StudyProgramResponseDTO createStudyProgram = studyProgramService.addStudyProgram(courseId, studyProgramRequest);

        ResponseWrapper<StudyProgramResponseDTO> response = ResponseWrapper.<StudyProgramResponseDTO>builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .data(createStudyProgram)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{courseId}/study-programs/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cập nhật chương trình học theo khóa học")
    public ResponseEntity<ResponseWrapper<StudyProgramResponseDTO>> updateStudyProgram(
            @PathVariable Long courseId,
            @PathVariable Long id,
            @Valid @RequestBody StudyProgramRequest studyProgramRequest
    ){
        StudyProgramResponseDTO updated = studyProgramService.updateStudyProgram(id, courseId, studyProgramRequest);

        ResponseWrapper<StudyProgramResponseDTO> response = ResponseWrapper.<StudyProgramResponseDTO>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(updated)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{courseId}/study-programs/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Xóa chương trình học theo khóa học")
    public ResponseEntity<ResponseWrapper<String>> deleteStudyProgram(
            @PathVariable Long courseId,
            @PathVariable Long id
    ){
        studyProgramService.deleteStudyProgram(courseId, id);

        ResponseWrapper<String> response = ResponseWrapper.<String>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data("Xóa chương trình học thành công!")
                .build();

        return ResponseEntity.ok(response);
    }

//    @PostMapping(value = "/{courseId}/input-requirements")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "Thêm mới yêu cầu đầu vào theo khóa học")
//    public ResponseEntity<ResponseWrapper<StudyProgramResponseDTO>> createInputRequirement(
//            @PathVariable Long courseId,
//            @Valid @RequestBody StudyProgramRequest studyProgramRequest
//    ){
//        StudyProgramResponseDTO createStudyProgram = studyProgramService.addStudyProgram(courseId, studyProgramRequest);
//
//        ResponseWrapper<StudyProgramResponseDTO> response = ResponseWrapper.<StudyProgramResponseDTO>builder()
//                .status(HttpStatus.CREATED)
//                .code(HttpStatus.CREATED.value())
//                .data(createStudyProgram)
//                .build();
//
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }
//
//    @PutMapping(value = "/{courseId}/input-requirements/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "Cập nhật yêu cầu đầu vào theo khóa học")
//    public ResponseEntity<ResponseWrapper<StudyProgramResponseDTO>> updateInputRequirement(
//            @PathVariable Long courseId,
//            @PathVariable Long id,
//            @Valid @RequestBody StudyProgramRequest studyProgramRequest
//    ){
//        StudyProgramResponseDTO updated = studyProgramService.updateStudyProgram(id, courseId, studyProgramRequest);
//
//        ResponseWrapper<StudyProgramResponseDTO> response = ResponseWrapper.<StudyProgramResponseDTO>builder()
//                .status(HttpStatus.OK)
//                .code(HttpStatus.OK.value())
//                .data(updated)
//                .build();
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @DeleteMapping(value = "/{courseId}/input-requirements/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "Xóa yêu cầu đầu vào theo khóa học")
//    public ResponseEntity<ResponseWrapper<String>> deleteInputRequirement(
//            @PathVariable Long courseId,
//            @PathVariable Long id
//    ){
//        studyProgramService.deleteStudyProgram(courseId, id);
//
//        ResponseWrapper<String> response = ResponseWrapper.<String>builder()
//                .status(HttpStatus.OK)
//                .code(HttpStatus.OK.value())
//                .data("Xóa chương trình học thành công!")
//                .build();
//
//        return ResponseEntity.ok(response);
//    }
}
