package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.LectureRequest;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.dto.response.LectureResponse;
import com.ra.base_spring_boot.service.interfaces.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/lectures")
public class LectureController {

    @Autowired
    private LectureService lectureService;

    @GetMapping
    public ResponseEntity<APIResponse<List<LectureResponse>>> getAllTeachers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String status) {

        List<LectureResponse> responses = lectureService.getAllTeachers(keyword, specialization, status);
        return ResponseEntity.ok(new APIResponse<>(true, "Danh sách giảng viên", responses, null, null));
    }

    @PostMapping
    public ResponseEntity<APIResponse<LectureResponse>> createLecture(@Valid @RequestBody LectureRequest request) {
        LectureResponse lectureResponse = lectureService.createTeacher(request);
        return ResponseEntity.ok(
                new APIResponse<>(true, "Lecture created successfully", lectureResponse, null, null)
        );
    }
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<LectureResponse>> updateLecture(
            @PathVariable Long id,
            @Valid @RequestBody LectureRequest request) {

        LectureResponse updatedLecture = lectureService.updateTeacher(id, request);
        return ResponseEntity.ok(
                new APIResponse<>(true, "Lecture updated successfully", updatedLecture, null, null)
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteTeacher(@PathVariable Long id) {
        lectureService.deleteTeacher(id);
        return ResponseEntity.ok(new APIResponse<>(true, "Teacher deleted successfully", null, null, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<LectureResponse>> getTeacher(@PathVariable Long id) {
        LectureResponse lectureResponse = lectureService.getTeacher(id);
        return ResponseEntity.ok(new APIResponse<>(true, "Teacher retrieved successfully", lectureResponse, null, null));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<APIResponse<LectureResponse>> updateTeacherStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        LectureResponse updated = lectureService.updateStatus(id, status);
        return ResponseEntity.ok(new APIResponse<>(true, "Teacher status updated successfully", updated, null, null));
    }


}
