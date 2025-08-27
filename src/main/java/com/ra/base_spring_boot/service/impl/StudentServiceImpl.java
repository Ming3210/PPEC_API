package com.ra.base_spring_boot.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ra.base_spring_boot.dto.request.StudentRequest;
import com.ra.base_spring_boot.dto.request.StudentUpdateDTO;
import com.ra.base_spring_boot.dto.request.UpdatePasswordRequest;
import com.ra.base_spring_boot.dto.request.UpdateStudentProfileRequest;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.dto.response.StudentProfileResponse;
import com.ra.base_spring_boot.dto.response.StudentResponse;
import com.ra.base_spring_boot.exception.HttpBadRequest;
import com.ra.base_spring_boot.exception.HttpConflict;
import com.ra.base_spring_boot.exception.HttpForbiden;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.Departments;
import com.ra.base_spring_boot.model.Industry;
import com.ra.base_spring_boot.model.Student;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.model.constants.AccountStatus;
import com.ra.base_spring_boot.model.constants.RoleName;
import com.ra.base_spring_boot.repository.DepartmentRepository;
import com.ra.base_spring_boot.repository.IndustryRepository;
import com.ra.base_spring_boot.repository.StudentRepository;
import com.ra.base_spring_boot.repository.UserRepository;
import com.ra.base_spring_boot.service.interfaces.IStudentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private IndustryRepository industryRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new HttpConflict("Ten tai khoan da ton tai");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new HttpConflict("Email da ton tai");
        }
        if(userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new HttpConflict("So dien thoai da ton tai");
        }
        if(studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new HttpConflict("Ma sinh vien da ton tai");
        }
        if (request.getAvatar() == null || request.getAvatar().isEmpty()) {
            throw new HttpBadRequest("Ảnh đại diện không được để trống");
        }


        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .role(RoleName.STUDENT)
                .status(AccountStatus.PENDING)
                .build();

        userRepository.save(user);

        Departments department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy trường"));

        Industry industry = industryRepository.findById(request.getIndustryId())
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy chuyên ngành"));

        String imageUrl = null;
        MultipartFile avatar = request.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(
                        avatar.getBytes(),
                        ObjectUtils.emptyMap()
                );
                imageUrl = uploadResult.get("secure_url").toString();
            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi tải hình anh", e);
            }
        }

        Student student = Student.builder()
                .user(user)
                .studentCode(request.getStudentCode())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .department(department)
                .academicYear(request.getAcademicYear())
                .address(request.getAddress())
                .industry(industry)
                .avatarUrl(imageUrl)
                .build();

        Student saved = studentRepository.save(student);

        return toStudentResponse(saved);
    }

    @Override
    public PaginationResponse<StudentResponse> getAllStudents(Integer page, Integer itemPage, String sortBy, Boolean orderBy) {
        Pageable pageable;
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort sort = orderBy
                    ? Sort.by(Sort.Direction.ASC, sortBy)
                    : Sort.by(Sort.Direction.DESC, sortBy);
            pageable = PageRequest.of(page, itemPage, sort);
        } else {
            pageable = PageRequest.of(page, itemPage);
        }

        Page<Student> students = studentRepository.findAll(pageable);
        Page<StudentResponse> studentResponses = students.map(this::toStudentResponse);

        return PaginationResponse.of(studentResponses);
    }


    @Override
    public StudentResponse getStudentById(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy sinh viên"));
        return toStudentResponse(student);
    }

    @Override
    public StudentResponse updateStudent(Long studentId, StudentUpdateDTO dto) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy sinh viên"));

        User user = student.getUser();

        if (dto.getFullName() != null && !dto.getFullName().isBlank()) {
            user.setFullName(dto.getFullName());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isBlank()) {
            user.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getRole() != null) {
            try {
                user.setRole(RoleName.valueOf(dto.getRole().name()));
            } catch (IllegalArgumentException e) {
                throw new HttpBadRequest("Role không hợp lệ");
            }
        }

        if (dto.getDateOfBirth() != null) {
            student.setDateOfBirth(dto.getDateOfBirth());
        }
        if (dto.getAddress() != null && !dto.getAddress().isBlank()) {
            student.setAddress(dto.getAddress());
        }
        if (dto.getDepartmentId() != null) {
            Departments department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new HttpNotFound("Không tìm thấy trường"));
            student.setDepartment(department);
        }
        if (dto.getIndustryId() != null) {
            Industry industry = industryRepository.findById(dto.getIndustryId())
                    .orElseThrow(() -> new HttpNotFound("Không tìm thấy chuyên ngành"));
            student.setIndustry(industry);
        }

        MultipartFile avatar = dto.getAvatar();

        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(
                        avatar.getBytes(),
                        ObjectUtils.emptyMap()
                );
                student.setAvatarUrl(uploadResult.get("secure_url").toString());
            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi tải ảnh", e);
            }
        }

        userRepository.save(user);
        studentRepository.save(student);

        return toStudentResponse(student);
    }




    @Override
    public void deleteStudent(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy sinh viên"));

        User user = student.getUser();

        userRepository.findById(user.getId()).orElseThrow(() -> new HttpNotFound("Không tìm thấy người dung"));
        if (user.getStatus() == AccountStatus.ACTIVE) {
            throw new IllegalStateException("Tài khoản ko đang đc ACTIVE");
        }

        user.setStatus(AccountStatus.INACTIVE);
        userRepository.save(user);

    }

    @Override
    public StudentProfileResponse toProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userLogin = userRepository.findByUsername(username)
                .orElseThrow(() -> new HttpNotFound("User not found with username: " + username));
        Student student = studentRepository.findByUser_Username(username);


        return StudentProfileResponse.builder()
                .userId(userLogin.getId())
                .studentId(student.getId())
                .username(userLogin.getUsername())
                .fullName(userLogin.getFullName())
                .email(userLogin.getEmail())
                .phoneNumber(userLogin.getPhoneNumber())
                .studentCode(student.getStudentCode())
                .dateOfBirth(student.getDateOfBirth())
                .gender(student.getGender())
                .academicYear(student.getAcademicYear())
                .address(student.getAddress())
                .departmentId(student.getDepartment() != null ? student.getDepartment().getId() : null)
                .departmentName(student.getDepartment() != null ? student.getDepartment().getName() : null)
                .industryId(student.getIndustry().getId())
                .industryName(student.getIndustry().getName())
                .avatarUrl(student.getAvatarUrl())
                .toSchoolPercentage("Chưa làm thống kê")
                .homeworkPercentage("Chưa làm thống kê")
                .taskPreparationPercentage("Chưa làm thống kê")
                .build();

    }

    @Override
    public StudentResponse updateProfile(Long studentId, UpdateStudentProfileRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy sinh viên"));
        User user = student.getUser();

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            user.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null && !request.getAddress().isBlank()) {
            student.setAddress(request.getAddress());
        }

        MultipartFile avatar = request.getAvatar();

        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(
                        avatar.getBytes(),
                        ObjectUtils.emptyMap()
                );
                student.setAvatarUrl(uploadResult.get("secure_url").toString());
            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi tải ảnh", e);
            }
        }


        if (request.getDateOfBirth() != null) {
            student.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            student.setGender(request.getGender());
        }
        if (request.getAcademicYear() != null && !request.getAcademicYear().isBlank()) {
            student.setAcademicYear(request.getAcademicYear());
        }
        if (request.getDepartmentId() != null) {
            Departments department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new HttpNotFound("Không tìm thấy khoa"));
            student.setDepartment(department);
        }
        if (request.getIndustryId() != null) {
            Industry industry = industryRepository.findById(request.getIndustryId())
                    .orElseThrow(() -> new HttpNotFound("Không tìm thấy ngành"));
            student.setIndustry(industry);
        }

        userRepository.save(user);
        studentRepository.save(student);

        return toStudentResponse(student);
    }

    @Override
    public Boolean updatePassword( UpdatePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (username == null) {
            throw new HttpForbiden("Truy cập bị từ chối");
        }
        User userLogin = userRepository.findByUsername(username)
                .orElseThrow(() -> new HttpNotFound("User not found with username: " + username));


        if (!passwordEncoder.matches(request.getOldPassword(), userLogin.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu cũ không chính xác");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Xác nhận mật khẩu không khớp");
        }

        userLogin.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(userLogin);

        return true;
    }



    private StudentResponse toStudentResponse(Student student) {
        return StudentResponse.builder()
                .studentId(student.getId())
                .userId(student.getUser().getId())
                .username(student.getUser().getUsername())
                .fullName(student.getUser().getFullName())
                .email(student.getUser().getEmail())
                .phoneNumber(student.getUser().getPhoneNumber())
                .studentCode(student.getStudentCode())
                .dateOfBirth(student.getDateOfBirth())
                .gender(student.getGender())
                .academicYear(student.getAcademicYear())
                .address(student.getAddress())
                .departmentId(student.getDepartment() != null ? student.getDepartment().getId() : null)
                .departmentName(student.getDepartment() != null ? student.getDepartment().getName() : null)
                .industryId(student.getIndustry().getId())
                .industryName(student.getIndustry().getName())
                .avatarUrl(student.getAvatarUrl())
                .build();
    }
}
