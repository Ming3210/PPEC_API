package com.ra.base_spring_boot.service.interfaces;


import com.ra.base_spring_boot.dto.request.CourseRequestDTO;
import com.ra.base_spring_boot.dto.response.CourseResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICourseService {
    CourseResponseDTO createCourse(CourseRequestDTO courseRequestDTO);
    CourseResponseDTO getCourseById(Long id);
    CourseResponseDTO updateCourse(Long id, CourseRequestDTO courseRequestDTO);
    void deleteCourse(Long id);
    List<CourseResponseDTO> getAllCourses();
    Page<CourseResponseDTO> searchCourses(String keyword, int page, int size);
}

