package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.CourseScheduleRequest;
import com.ra.base_spring_boot.dto.response.CourseScheduleResponse;

import java.util.List;

public interface ICourseScheduleService {
    CourseScheduleResponse addCourseSchedule(CourseScheduleRequest requestDTO);
    List<CourseScheduleResponse> getSchedulesByCourse(Long courseOffId);
}
