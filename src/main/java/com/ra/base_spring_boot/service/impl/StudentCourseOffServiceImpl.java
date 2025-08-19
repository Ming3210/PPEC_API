package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.StudentCourseOffRequest;
import com.ra.base_spring_boot.dto.response.StudentCourseOffResponse;
import com.ra.base_spring_boot.model.CourseOff;
import com.ra.base_spring_boot.model.Student;
import com.ra.base_spring_boot.model.StudentCourseOff;
import com.ra.base_spring_boot.repository.CourseOffRepository;
import com.ra.base_spring_boot.repository.StudentCourseOffRepository;
import com.ra.base_spring_boot.repository.StudentRepository;
import com.ra.base_spring_boot.security.principal.UserPrincipal;
import com.ra.base_spring_boot.service.interfaces.IStudentCourseOffService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentCourseOffServiceImpl implements IStudentCourseOffService {

    private final StudentCourseOffRepository studentCourseOffRepository;
    private final StudentRepository studentRepository;
    private final CourseOffRepository courseOffRepository;

    @Override
    public StudentCourseOffResponse addStudentToCourse(StudentCourseOffRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với ID: " + request.getStudentId()));

        CourseOff courseOff = courseOffRepository.findById(request.getCourseOffId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học với ID: " + request.getCourseOffId()));

        studentCourseOffRepository.findByStudentIdAndCourseOffId(student.getId(), courseOff.getId())
                .ifPresent(sc -> { throw new RuntimeException("Sinh viên đã đăng ký khóa học này"); });

        StudentCourseOff studentCourseOff = StudentCourseOff.builder()
                .student(student)
                .courseOff(courseOff)
                .status(request.getStatus() != null ? request.getStatus() : "REGISTERED")
                .registrationDate(LocalDateTime.now())
                .build();

        StudentCourseOff saved = studentCourseOffRepository.save(studentCourseOff);

        return toResponse(saved);
    }

    @Override
    public List<StudentCourseOffResponse> getStudentsOfCourse(Long courseOffId) {
        List<StudentCourseOff> list = studentCourseOffRepository.findByCourseOffId(courseOffId);
        return list.stream().map(StudentCourseOffServiceImpl::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<StudentCourseOffResponse> getCoursesOfStudent(Long studentId) {
        List<StudentCourseOff> list = studentCourseOffRepository.findByStudentId(studentId);
        return list.stream().map(StudentCourseOffServiceImpl::toResponse).collect(Collectors.toList());
    }
    @Override
    public List<StudentCourseOffResponse> getMyCourses() {
        Long studentId = getCurrentStudentId();
        List<StudentCourseOff> list = studentCourseOffRepository.findByStudentId(studentId);
        return list.stream().map(StudentCourseOffServiceImpl::toResponse).collect(Collectors.toList());
    }

    private Long getCurrentStudentId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.getId();
    }



    private static StudentCourseOffResponse toResponse(StudentCourseOff sco) {
        return StudentCourseOffResponse.builder()
                .id(sco.getId())
                .studentId(sco.getStudent().getId())
                .studentName(sco.getStudent().getUser().getFullName())
                .studentEmail(sco.getStudent().getUser().getEmail())
                .courseOffId(sco.getCourseOff().getId())
                .courseOffName(sco.getCourseOff().getName())
                .registrationDate(sco.getRegistrationDate())
                .status(sco.getStatus())
                .grade(sco.getGrade())
                .build();
    }
}
