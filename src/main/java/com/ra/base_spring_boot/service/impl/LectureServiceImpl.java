package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.LectureRequest;
import com.ra.base_spring_boot.dto.response.LectureResponse;
import com.ra.base_spring_boot.model.*;
import com.ra.base_spring_boot.model.constants.RoleName;
import com.ra.base_spring_boot.repository.*;
import com.ra.base_spring_boot.service.interfaces.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureServiceImpl implements LectureService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private IndustryRepository industryRepository;

    @Override
    public List<Lecturer> getAllTeachers(String keyword, String specialization, String status) {
        return List.of();
    }

    @Override
    public LectureResponse createTeacher(LectureRequest request) {
        User user = userRepository.findByIdAndRole(request.getUserId(), RoleName.LECTURER)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user có role LECTURER"));

        Lecturer lecturer = new Lecturer();
        lecturer.setUser(user);
        lecturer.setLecturerCode(request.getLecturerCode());
        lecturer.setDateOfBirth(request.getDateOfBirth());
        lecturer.setHometown(request.getHometown());
        lecturer.setWorkYear(request.getWorkYear());

        if (request.getDepartmentId() != null) {
            Departments departments = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy trường với ID: " + request.getDepartmentId()));
            lecturer.setDepartment(departments);
        }

        if (request.getIndustryId() != null) {
            Industry industry = industryRepository.findById(request.getIndustryId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên ngành với ID: " + request.getIndustryId()));
            lecturer.setIndustry(industry);
        }

        Lecturer savedLecturer = lectureRepository.save(lecturer);
        return toResponse(savedLecturer);
    }


    @Override
    public Lecturer updateTeacher(Long id, LectureRequest request) {
        return null;
    }

    @Override
    public void deleteTeacher(Long id) {

    }

    @Override
    public Lecturer getTeacher(Long id) {
        return null;
    }

    @Override
    public Lecturer updateStatus(Long id, String status) {
        return null;
    }


    public static LectureResponse toResponse(Lecturer lecturer) {
        return LectureResponse.builder()
                .id(lecturer.getId())
                .fullName(lecturer.getUser() != null ? lecturer.getUser().getFullName() : null)
                .email(lecturer.getUser() != null ? lecturer.getUser().getEmail() : null)
                .dateOfBirth(lecturer.getDateOfBirth())
                .lecturerCode(lecturer.getLecturerCode())
                .hometown(lecturer.getHometown())
                .schoolId(lecturer.getDepartment() != null ? lecturer.getDepartment().getId() : null)
                .industryId(lecturer.getIndustry() != null ? lecturer.getIndustry().getId() : null)
                .workYear(lecturer.getWorkYear())
                .build();
    }
}
