package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.StudentCourseOffRequest;
import com.ra.base_spring_boot.dto.response.StudentCourseOffResponse;

import java.util.List;

public interface IStudentCourseOffService {

    StudentCourseOffResponse addStudentToCourse(StudentCourseOffRequest request);

    List<StudentCourseOffResponse> getStudentsOfCourse(Long courseOffId);

    List<StudentCourseOffResponse> getCoursesOfStudent(Long studentId);
}
