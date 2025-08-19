package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.LectureRequest;
import com.ra.base_spring_boot.dto.request.UpdateLectureRequest;
import com.ra.base_spring_boot.dto.response.LectureResponse;
import com.ra.base_spring_boot.dto.response.PaginationResponse;

import java.util.List;

public interface ILectureService {
    PaginationResponse<LectureResponse> getAllTeachers(
            String keyword,
            String specialization,
            String status,
            int page,
            int size
    );
    LectureResponse createTeacher(LectureRequest request);
    LectureResponse  updateTeacher(Long id, UpdateLectureRequest request);
    void deleteTeacher(Long id);
    LectureResponse  getTeacher(Long id);
    LectureResponse  updateStatus(Long id, String status);
    LectureResponse getMyProfile();

}
