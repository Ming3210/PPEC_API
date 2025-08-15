package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.StudentRequest;
import com.ra.base_spring_boot.dto.request.StudentUpdateDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.dto.response.StudentResponse;
import org.springframework.data.domain.Page;


public interface IStudentService {
    StudentResponse createStudent(StudentRequest studentRequest);

    PaginationResponse<StudentResponse> getAllStudents(Integer page, Integer itemPage, String sortBy, Boolean orderBy);

    StudentResponse getStudentById(Long studentId);

    StudentResponse updateStudent(Long studentId, StudentUpdateDTO studentUpdateDTO);

    void deleteStudent(Long studentId);
}
