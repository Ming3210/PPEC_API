package com.ra.base_spring_boot.dto.response;

import com.ra.base_spring_boot.model.Course;
import com.ra.base_spring_boot.model.CourseOff;
import com.ra.base_spring_boot.model.Exam;
import com.ra.base_spring_boot.model.constants.Gender;
import com.ra.base_spring_boot.model.constants.RoleName;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailResponseDTO {
    private Long studentId;

    private Long userId;
    private String username;
    private String fullName;
    private LocalDate dateOfBirth;
    private String email;
    private String phoneNumber;
    private String address;
    private String avatarUrl;

    private String studentCode;
    private String academicYear;

    private Long departmentId;
    private String departmentName;
    private Long industryId;
    private String industryName;
    private RoleName role;
    private List<CourseOff> courseOffs;
    private List<Course> assignedCourses;
    private List<Exam> exams;
    private List<Course> coursesManaged;
}
