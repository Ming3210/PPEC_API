package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.StudentCourseOffRequest;
import com.ra.base_spring_boot.dto.request.StudentRegisterCourseOffRequest;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.dto.response.StudentCourseOfResponse;
import com.ra.base_spring_boot.dto.response.StudentCourseOffResponse;
import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.service.interfaces.IStudentCourseOffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/api/student-course-off")
@RequiredArgsConstructor
public class StudentCourseOffController {

    private final IStudentCourseOffService studentCourseOffService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<StudentCourseOffResponse>> addStudentToCourse(
            @RequestBody StudentCourseOffRequest request) {
        return ResponseEntity.ok(new APIResponse<>(true, "Thêm sinh viên vào khóa học thành công",studentCourseOffService.addStudentToCourse(request),
                 HttpStatus.CREATED, LocalDateTime.now()));
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<StudentCourseOffResponse>>> getStudentsOfCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(
                new APIResponse<>(true, "Danh sách sinh viên của khóa học",
                        studentCourseOffService.getStudentsOfCourse(courseId),
                        HttpStatus.OK, LocalDateTime.now())
        );
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<StudentCourseOfResponse>>> getCoursesOfStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(
                new APIResponse<>(true, "Danh sách khóa học của sinh viên",
                        studentCourseOffService.getCoursesOfStudent(studentId),
                        HttpStatus.OK, LocalDateTime.now())
        );
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<APIResponse<List<StudentCourseOfResponse>>> getMyCourses() {
        return ResponseEntity.ok(
                new APIResponse<>(true, "Danh sách khóa học của tôi",
                        studentCourseOffService.getMyCourses(),
                        HttpStatus.OK, LocalDateTime.now())
        );
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<APIResponse<StudentCourseOffResponse>> registerCourse(
            @RequestBody @Valid StudentRegisterCourseOffRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new APIResponse<>(true, "Đăng ký khóa học thành công",
                        studentCourseOffService.registerCourseOff(request),
                        HttpStatus.CREATED, LocalDateTime.now())
        );
    }
}
