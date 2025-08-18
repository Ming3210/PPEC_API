package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.request.ExamRequestDTO;
import com.ra.base_spring_boot.dto.request.ExamSearchFilterDTO;
import com.ra.base_spring_boot.dto.response.ExamResponseDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.model.constants.ExamStatus;
import com.ra.base_spring_boot.service.interfaces.IExamService;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/exams")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ExamController {

    private final IExamService examService;


    @PostMapping
    public ResponseEntity<ResponseWrapper<ExamResponseDTO>> createExam(
            @Valid @RequestBody ExamRequestDTO examRequestDTO) {

        ExamResponseDTO createdExam = examService.createExam(examRequestDTO);

        ResponseWrapper<ExamResponseDTO> response = ResponseWrapper.<ExamResponseDTO>builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .data(createdExam)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<ExamResponseDTO>> getExamById(@PathVariable Long id) {
        ExamResponseDTO exam = examService.getExamById(id);

        ResponseWrapper<ExamResponseDTO> response = ResponseWrapper.<ExamResponseDTO>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(exam)
                .build();

        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<ExamResponseDTO>> updateExam(
            @PathVariable Long id,
            @Valid @RequestBody ExamRequestDTO examRequestDTO) {

        ExamResponseDTO updatedExam = examService.updateExam(id, examRequestDTO);

        ResponseWrapper<ExamResponseDTO> response = ResponseWrapper.<ExamResponseDTO>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data(updatedExam)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<String>> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);

        ResponseWrapper<String> response = ResponseWrapper.<String>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .data("Xóa bài thi thành công")
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<ResponseWrapper<PaginationResponse<ExamResponseDTO>>> getAllExams(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "examDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        PaginationResponse<ExamResponseDTO> result = examService.getAllExams(page, size, sortBy, sortDirection);

        ResponseWrapper<PaginationResponse<ExamResponseDTO>> response =
                ResponseWrapper.<PaginationResponse<ExamResponseDTO>>builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(result)
                        .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/search")
    public ResponseEntity<ResponseWrapper<PaginationResponse<ExamResponseDTO>>> searchAndFilterExams(
            @RequestParam(required = false) String examCode,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long partnerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate examDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate examDateTo,
            @RequestParam(defaultValue = "examDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ExamSearchFilterDTO filterDTO = new ExamSearchFilterDTO();
        filterDTO.setExamCode(examCode);
        filterDTO.setTitle(title);
        if (status != null && !status.trim().isEmpty()) {
            try {
                filterDTO.setStatus(ExamStatus.valueOf(status.toUpperCase()));
            } catch (IllegalArgumentException e) {
            }
        }
        filterDTO.setCourseId(courseId);
        filterDTO.setPartnerId(partnerId);
        filterDTO.setExamDateFrom(examDateFrom);
        filterDTO.setExamDateTo(examDateTo);
        filterDTO.setSortBy(sortBy);
        filterDTO.setSortDirection(sortDirection);
        filterDTO.setPage(page);
        filterDTO.setSize(size);

        PaginationResponse<ExamResponseDTO> result = examService.searchAndFilterExams(filterDTO);

        ResponseWrapper<PaginationResponse<ExamResponseDTO>> response =
                ResponseWrapper.<PaginationResponse<ExamResponseDTO>>builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .data(result)
                        .build();

        return ResponseEntity.ok(response);
    }
}
