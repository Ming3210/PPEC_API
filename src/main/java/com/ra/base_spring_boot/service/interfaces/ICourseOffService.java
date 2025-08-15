package com.ra.base_spring_boot.service.interfaces;


import com.ra.base_spring_boot.dto.request.CourseOffRequestDTO;
import com.ra.base_spring_boot.dto.request.CourseOffSearchFilterDTO;
import com.ra.base_spring_boot.dto.response.CourseOffResponseDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;

import java.util.List;

public interface ICourseOffService {
    CourseOffResponseDTO createCourseOff(CourseOffRequestDTO courseOffRequestDTO);
    CourseOffResponseDTO getCourseOffById(Long id);
    CourseOffResponseDTO updateCourseOff(Long id, CourseOffRequestDTO courseOffRequestDTO);
    void deleteCourseOff(Long id);
    PaginationResponse<CourseOffResponseDTO> searchAndFilterCoursesOff(CourseOffSearchFilterDTO filterDTO);
    PaginationResponse<CourseOffResponseDTO> getAllCoursesOff(int page, int size, String sortBy, String sortDirection);
}

