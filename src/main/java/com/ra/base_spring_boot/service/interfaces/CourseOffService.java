package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.CourseOffRequest;
import com.ra.base_spring_boot.dto.response.CourseOffDTO;
import com.ra.base_spring_boot.model.CourseOff;
import org.springframework.data.domain.Page;

public interface CourseOffService {
    Page<CourseOffDTO> getAllCourseOffs(int page, int size);

    CourseOffDTO addCourseOff(CourseOffRequest courseOffRequest);

    CourseOff getCourseOffById(Long id);

    CourseOffDTO update(Long id, CourseOffRequest courseOffRequest);

    void delete(Long id);
}
