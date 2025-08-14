package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.LectureRequest;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.dto.response.LectureResponse;
import com.ra.base_spring_boot.service.interfaces.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/lectures")
public class LectureController {

    @Autowired
    private LectureService lectureService;

    @PostMapping
    public ResponseEntity<APIResponse<LectureResponse>> createLecture(@Valid @RequestBody LectureRequest request) {
        LectureResponse lectureResponse = lectureService.createTeacher(request);
        return ResponseEntity.ok(
                new APIResponse<>(true, "Lecture created successfully", lectureResponse, null, null)
        );
    }
}
