package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.StudentRequest;
import com.ra.base_spring_boot.dto.request.StudentUpdateDTO;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.service.interfaces.IStudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("api/students")
public class StudentController {

    @Autowired
    private IStudentService IStudentService;

    @PostMapping  (consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<?>> createStudent(@Valid @ModelAttribute StudentRequest studentRequest) {
        return ResponseEntity.ok(new APIResponse<>(true, "Create student successfully!",
                IStudentService.createStudent(studentRequest), HttpStatus.OK, LocalDateTime.now()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER', 'STUDENT')")
    public ResponseEntity<APIResponse<?>> getAllStudents(@RequestParam(defaultValue = "0") Integer page,
                                                         @RequestParam(defaultValue = "10") Integer itemPage,
                                                         @RequestParam(defaultValue = "id") String sortBy,
                                                         @RequestParam(defaultValue = "true") Boolean orderBy) {
        return ResponseEntity.ok(new APIResponse<>(
                true,
                "Get student successfully!",
                IStudentService.getAllStudents(page, itemPage, sortBy, orderBy),
                HttpStatus.OK,
                LocalDateTime.now()
        ));
    }

    @GetMapping("/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER', 'STUDENT')")
    public ResponseEntity<APIResponse<?>> getStudentById(@PathVariable Long studentId) {
        return ResponseEntity.ok(new APIResponse<>(true, "Get student successfully!",
                IStudentService.getStudentById(studentId), HttpStatus.OK, LocalDateTime.now()));
    }

    @PutMapping("/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')") // chỉ ADMIN và LECTURER được sửa
    public ResponseEntity<APIResponse<?>> updateStudent(@PathVariable Long studentId,
                                                        @Valid @ModelAttribute StudentUpdateDTO studentRequest) {
        return ResponseEntity.ok(new APIResponse<>(true, "Update student successfully!",
                IStudentService.updateStudent(studentId, studentRequest), HttpStatus.OK, LocalDateTime.now()));
    }

    @DeleteMapping("/{studentId}")
    @PreAuthorize("hasRole('ADMIN')") // chỉ ADMIN được xóa
    public ResponseEntity<APIResponse<?>> deleteStudent(@PathVariable Long studentId) {
        IStudentService.deleteStudent(studentId);
        return ResponseEntity.ok(new APIResponse<>(true, "Delete student successfully!",
                null, HttpStatus.OK, LocalDateTime.now()));
    }
}
