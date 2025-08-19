package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.StudentCourseOffRequest;
import com.ra.base_spring_boot.dto.response.StudentCourseOffResponse;
import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.service.interfaces.IStudentCourseOffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-course-off")
@RequiredArgsConstructor
public class StudentCourseOffController {

    private final IStudentCourseOffService studentCourseOffService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<StudentCourseOffResponse>> addStudentToCourse(
            @RequestBody StudentCourseOffRequest request) {

        StudentCourseOffResponse response = studentCourseOffService.addStudentToCourse(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.<StudentCourseOffResponse>builder()
                        .status(HttpStatus.CREATED)
                        .code(HttpStatus.CREATED.value())
                        .data(response)
                        .build());
    }
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<List<StudentCourseOffResponse>>> getStudentsOfCourse(
            @PathVariable Long courseId) {

        List<StudentCourseOffResponse> responses = studentCourseOffService.getStudentsOfCourse(courseId);

        return ResponseEntity.ok(ResponseWrapper.<List<StudentCourseOffResponse>>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(responses)
                .build());
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<List<StudentCourseOffResponse>>> getCoursesOfStudent(
            @PathVariable Long studentId) {

        List<StudentCourseOffResponse> responses = studentCourseOffService.getCoursesOfStudent(studentId);

        return ResponseEntity.ok(ResponseWrapper.<List<StudentCourseOffResponse>>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(responses)
                .build());
    }
    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ResponseWrapper<List<StudentCourseOffResponse>>> getMyCourses() {

        List<StudentCourseOffResponse> responses = studentCourseOffService.getMyCourses();

        return ResponseEntity.ok(ResponseWrapper.<List<StudentCourseOffResponse>>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(responses)
                .build());
    }
}
