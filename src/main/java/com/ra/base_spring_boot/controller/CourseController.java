package com.ra.base_spring_boot.controller;


import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.request.CourseRequestDTO;
import com.ra.base_spring_boot.dto.response.CourseResponseDTO;
import com.ra.base_spring_boot.service.interfaces.ICourseService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/courses")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class CourseController {

    private final ICourseService courseService;

    @PostMapping
    public ResponseEntity<ResponseWrapper<CourseResponseDTO>> createCourse(
            @Valid @ModelAttribute CourseRequestDTO courseRequestDTO) {

        CourseResponseDTO createdCourse = courseService.createCourse(courseRequestDTO);

        ResponseWrapper<CourseResponseDTO> response = ResponseWrapper.<CourseResponseDTO>builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .data(createdCourse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<CourseResponseDTO>> getCourseById(@PathVariable Long id) {
        CourseResponseDTO course = courseService.getCourseById(id);

        ResponseWrapper<CourseResponseDTO> response = ResponseWrapper.<CourseResponseDTO>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(course)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<CourseResponseDTO>> updateCourse(
            @PathVariable Long id,
            @Valid @ModelAttribute CourseRequestDTO courseRequestDTO) {

        CourseResponseDTO updatedCourse = courseService.updateCourse(id, courseRequestDTO);

        ResponseWrapper<CourseResponseDTO> response = ResponseWrapper.<CourseResponseDTO>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(updatedCourse)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<String>> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);

        ResponseWrapper<String> response = ResponseWrapper.<String>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data("Xóa khóa học thành công")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<CourseResponseDTO>>> getAllCourses() {
        List<CourseResponseDTO> courses = courseService.getAllCourses();

        ResponseWrapper<List<CourseResponseDTO>> response = ResponseWrapper.<List<CourseResponseDTO>>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(courses)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseWrapper<Page<CourseResponseDTO>>> searchCourses(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<CourseResponseDTO> courses = courseService.searchCourses(keyword, page, size);

        ResponseWrapper<Page<CourseResponseDTO>> response = ResponseWrapper.<Page<CourseResponseDTO>>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(courses)
                .build();

        return ResponseEntity.ok(response);
    }
}

