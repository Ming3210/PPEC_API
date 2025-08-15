package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.StudentRequest;
import com.ra.base_spring_boot.dto.request.StudentUpdateDTO;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.service.interfaces.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/student")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @PostMapping
    public ResponseEntity<APIResponse<?>> createStudent(@Valid @ModelAttribute StudentRequest studentRequest) {
        {
            return ResponseEntity.ok(new APIResponse<>(true, "Create student successfully!", studentService.createStudent(studentRequest), HttpStatus.OK, LocalDateTime.now().toString()));
        }
    }

    @GetMapping
    public ResponseEntity<APIResponse<?>> getAllStudents(@RequestParam(defaultValue = "0") Integer page,
                                                          @RequestParam(defaultValue = "10") Integer itemPage,
                                                          @RequestParam(defaultValue = "id") String sortBy,
                                                          @RequestParam(defaultValue = "true") Boolean orderBy) {
        return ResponseEntity.ok(new APIResponse<>(true, "Get student successfully!", studentService.getAllStudents(page, itemPage, sortBy, orderBy), HttpStatus.CREATED, LocalDateTime.now().toString()));
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<APIResponse<?>> getStudentById(@PathVariable Long studentId) {
        return ResponseEntity.ok(new APIResponse<>(true, "Get student successfully!", studentService.getStudentById(studentId), HttpStatus.OK, LocalDateTime.now().toString()));
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<APIResponse<?>> updateStudent(@PathVariable Long studentId, @Valid @ModelAttribute StudentUpdateDTO studentRequest) {
        return ResponseEntity.ok(new APIResponse<>(true, "Update student successfully!", studentService.updateStudent(studentId, studentRequest), HttpStatus.OK, LocalDateTime.now().toString()));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<APIResponse<?>> deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok(new APIResponse<>(true, "Delete student successfully!", null, HttpStatus.NO_CONTENT, LocalDateTime.now().toString()));
    }


}
