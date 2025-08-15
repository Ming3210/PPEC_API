package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.LectureRequest;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.dto.response.LectureResponse;
import com.ra.base_spring_boot.service.interfaces.ILectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/lectures")
public class LectureController {

    @Autowired
    private ILectureService lectureService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<APIResponse<List<LectureResponse>>> getAllTeachers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String status) {

        return ResponseEntity.ok(
                new APIResponse<>(true, "Danh sách giảng viên",
                        lectureService.getAllTeachers(keyword, specialization, status),
                        null, null)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<APIResponse<LectureResponse>> createLecture(@Valid @RequestBody LectureRequest request) {
        return ResponseEntity.ok(
                new APIResponse<>(true, "Lecture created successfully",
                        lectureService.createTeacher(request),
                        null, null)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','LECTURE')")
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<LectureResponse>> updateLecture(
            @PathVariable Long id,
            @Valid @RequestBody LectureRequest request) {
        return ResponseEntity.ok(
                new APIResponse<>(true, "Lecture updated successfully",
                        lectureService.updateTeacher(id, request),
                        null, null)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteTeacher(@PathVariable Long id) {
        lectureService.deleteTeacher(id);
        return ResponseEntity.ok(new APIResponse<>(true, "Teacher deleted successfully", null, null, null));
    }

    @PreAuthorize("hasAnyRole('ADMIN','LECTURE')")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<LectureResponse>> getTeacher(@PathVariable Long id) {
        return ResponseEntity.ok(
                new APIResponse<>(true, "Teacher retrieved successfully",
                        lectureService.getTeacher(id),
                        null, null)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<APIResponse<LectureResponse>> updateTeacherStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(
                new APIResponse<>(true, "Teacher status updated successfully",
                        lectureService.updateStatus(id, status),
                        null, null)
        );
    }
}

