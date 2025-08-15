package com.ra.base_spring_boot.service.interfaces;


import com.ra.base_spring_boot.dto.request.ExamRequestDTO;
import com.ra.base_spring_boot.dto.response.ExamResponseDTO;
import com.ra.base_spring_boot.dto.request.ExamSearchFilterDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;

public interface IExamService {


    ExamResponseDTO createExam(ExamRequestDTO examRequestDTO);

    ExamResponseDTO getExamById(Long id);

    ExamResponseDTO updateExam(Long id, ExamRequestDTO examRequestDTO);

    void deleteExam(Long id);

    PaginationResponse<ExamResponseDTO> searchAndFilterExams(ExamSearchFilterDTO filterDTO);

    PaginationResponse<ExamResponseDTO> getAllExams(int page, int size, String sortBy, String sortDirection);
}

