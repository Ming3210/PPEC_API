package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.CourseScheduleRequest;
import com.ra.base_spring_boot.dto.response.CourseScheduleResponse;
import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.service.interfaces.ICourseScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/course-schedules")
@RequiredArgsConstructor
public class CourseScheduleController {

    private final ICourseScheduleService courseScheduleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<CourseScheduleResponse>> addCourseSchedule(
            @Valid @RequestBody CourseScheduleRequest requestDTO) {

        CourseScheduleResponse responseDTO = courseScheduleService.addCourseSchedule(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.<CourseScheduleResponse>builder()
                        .status(HttpStatus.CREATED)
                        .code(HttpStatus.CREATED.value())
                        .data(responseDTO)
                        .build());
    }

    @GetMapping("/course/{courseOffId}")
    public ResponseEntity<ResponseWrapper<List<CourseScheduleResponse>>> getSchedulesByCourse(
            @PathVariable Long courseOffId) {

        List<CourseScheduleResponse> schedules = courseScheduleService.getSchedulesByCourse(courseOffId);

        return ResponseEntity.ok(ResponseWrapper.<List<CourseScheduleResponse>>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(schedules)
                .build());
    }
}
