package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.LectureRequest;
import com.ra.base_spring_boot.dto.response.LectureResponse;
import com.ra.base_spring_boot.model.Lecturer;

import java.util.List;

public interface LectureService {
    List<Lecturer> getAllTeachers(String keyword, String specialization, String status);
    LectureResponse createTeacher(LectureRequest request);
    Lecturer updateTeacher(Long id, LectureRequest request);
    void deleteTeacher(Long id);
    Lecturer getTeacher(Long id);
    Lecturer updateStatus(Long id, String status);
}
