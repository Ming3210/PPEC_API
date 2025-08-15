package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.LectureRequest;
import com.ra.base_spring_boot.dto.response.LectureResponse;

import java.util.List;

public interface ILectureService {
    List<LectureResponse> getAllTeachers(String keyword, String specialization, String status);
    LectureResponse createTeacher(LectureRequest request);
    LectureResponse  updateTeacher(Long id, LectureRequest request);
    void deleteTeacher(Long id);
    LectureResponse  getTeacher(Long id);
    LectureResponse  updateStatus(Long id, String status);
}
