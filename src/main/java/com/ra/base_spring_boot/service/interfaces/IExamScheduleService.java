package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.ExamScheduleRequest;
import com.ra.base_spring_boot.dto.response.ExamScheduleDTO;
import com.ra.base_spring_boot.model.Exam;

public interface IExamScheduleService {
    ExamScheduleDTO addExamSchedule(Long courseId, ExamScheduleRequest examScheduleRequest);

    Exam getExamById(Long id);

    ExamScheduleDTO updateExamSchedule(Long id, Long courseId, ExamScheduleRequest examScheduleRequest);

    void deleteExamById(Long courseId, Long examScheduleId);
}
