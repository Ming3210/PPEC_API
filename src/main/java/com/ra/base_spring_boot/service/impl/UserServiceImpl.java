package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.response.UserDetailResponseDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.dto.response.UserResponseDTO;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.*;
import com.ra.base_spring_boot.model.constants.RoleName;
import com.ra.base_spring_boot.repository.*;
import com.ra.base_spring_boot.service.interfaces.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LectureRepository lecturerRepository;
    @Autowired
    private ServiceStaffRepository serviceStaffRepository;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private UserDetailRepository userDetailRepository;
    @Autowired
    private StudentCourseOffRepository studentCourseOffRepository;

    @Autowired
    private EnrollmentOnlineRepository enrollmentOnlineRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private ExamStudentRepository examStudentRepository;


    @Override
    public PaginationResponse<UserResponseDTO> getAllUser(int page, int size, String sortBy, boolean sortDirection, String keyword) {
        Pageable pageable;
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort sort = sortDirection
                    ? Sort.by(Sort.Direction.ASC, sortBy)
                    : Sort.by(Sort.Direction.DESC, sortBy);
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size);
        }

        Page<User> users = userRepository.findAllExceptAdmin(RoleName.ADMIN, keyword, pageable);
        Page<UserResponseDTO> userResponses = users.map(this::toUserResponse);
        return PaginationResponse.of(userResponses);
    }

    @Override
    public UserDetailResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new HttpNotFound("Không tìm thấy người dùng"));

        switch (user.getRole()) {
            case STUDENT -> {
                return getStudentDetail(user);
            }
            case LECTURER -> {
                return getLecturerDetail(user);
            }
            case SERVICE_STAFF -> {
                return getServiceStaffDetail(user);
            }
            case STAFF -> {
                return getStaffDetail(user);
            }
            default -> {
                throw new HttpNotFound("Không tìm thấy người dùng");
            }
        }
    }


    private UserResponseDTO toUserResponse(User user) {
        LocalDate dateOfBirth = null;
        String address = null;

        try {
            switch (user.getRole()) {
                case STUDENT -> {
                    Student student = studentRepository.findByUser(user);
                    if (student != null) {
                        dateOfBirth = student.getDateOfBirth();
                        address = student.getAddress();
                    }
                }
                case LECTURER -> {
                    Lecturer lecturer = lecturerRepository.findByUser(user);
                    if (lecturer != null) {
                        dateOfBirth = lecturer.getDateOfBirth();
                        address = lecturer.getHometown();
                    }
                }
                case SERVICE_STAFF -> {
                    ServiceStaff serviceStaff = serviceStaffRepository.findByUser(user);
                    if (serviceStaff != null) {
                        dateOfBirth = serviceStaff.getDateOfBirth();
                        address = serviceStaff.getHometown();
                    }
                }
                case STAFF -> {
                    Staff staff = staffRepository.findByUser(user);
                    if (staff != null) {
                        dateOfBirth = staff.getDateOfBirth();
                        UserDetail userDetail = userDetailRepository.findByUserId(user.getId()).orElse(null);
                        if (userDetail != null) {
                            address = userDetail.getHometown();
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error getting user details: " + e.getMessage());
        }

        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(address)
                .dateOfBirth(dateOfBirth)
                .role(user.getRole())
                .build();
    }


    private UserDetailResponseDTO getStudentDetail(User user) {
        Student student = studentRepository.findByUser(user);
        if (student == null) {
            throw new HttpNotFound("Không tìm thấy thông tin sinh viên");
        }

        List<CourseOff> courseOffs = studentCourseOffRepository.findByStudent(student)
                .stream()
                .map(StudentCourseOff::getCourseOff)
                .toList();


        List<Course> assignedCourses = enrollmentOnlineRepository.findByStudent(student)
                .stream()
                .map(EnrollmentOnline::getCourse)
                .toList();

        List<Exam> exams = examStudentRepository.findByStudent(student).stream().map(ExamStudent::getExam).toList();

        return UserDetailResponseDTO.builder()
                .studentId(student.getId())
                .userId(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .dateOfBirth(student.getDateOfBirth())
                .email(user.getEmail())
                .role(user.getRole())
                .phoneNumber(user.getPhoneNumber())
                .address(student.getAddress())
                .avatarUrl(student.getAvatarUrl())
                .studentCode(student.getStudentCode())
                .academicYear(student.getAcademicYear())
                .departmentId(student.getDepartment() != null ? student.getDepartment().getId() : null)
                .departmentName(student.getDepartment() != null ? student.getDepartment().getName() : null)
                .industryId(student.getIndustry() != null ? student.getIndustry().getId() : null)
                .industryName(student.getIndustry() != null ? student.getIndustry().getName() : null)
                .courseOffs(courseOffs)
                .assignedCourses(assignedCourses)
                .exams(exams)
                .build();
    }

    private UserDetailResponseDTO getLecturerDetail(User user) {
        Lecturer lecturer = lecturerRepository.findByUser(user);
        if (lecturer == null) {
            throw new HttpNotFound("Không tìm thấy thông tin giảng viên");
        }

        return UserDetailResponseDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .dateOfBirth(lecturer.getDateOfBirth())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .address(lecturer.getHometown())
                .avatarUrl(lecturer.getImageUrl())
                .build();
    }
    private UserDetailResponseDTO getServiceStaffDetail(User user) {
        ServiceStaff serviceStaff = serviceStaffRepository.findByUser(user);
        if (serviceStaff == null) {
            throw new HttpNotFound("Không tìm thấy thông tin nhân viên dịch vụ");
        }

        return UserDetailResponseDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .dateOfBirth(serviceStaff.getDateOfBirth())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(serviceStaff.getHometown())
                .role(user.getRole())
                .avatarUrl(serviceStaff.getAvatarUrl())
                .build();
    }

    private UserDetailResponseDTO getStaffDetail(User user) {
        Staff staff = staffRepository.findByUser(user);
        if (staff == null) {
            throw new HttpNotFound("Không tìm thấy thông tin nhân viên");
        }

        UserDetail userDetail = userDetailRepository.findByUserId(user.getId())
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy thông tin chi tiết người dùng"));

        return UserDetailResponseDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .dateOfBirth(staff.getDateOfBirth())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .address(userDetail.getHometown())
                .avatarUrl(userDetail.getAvatarUrl())
                .build();
    }

}
