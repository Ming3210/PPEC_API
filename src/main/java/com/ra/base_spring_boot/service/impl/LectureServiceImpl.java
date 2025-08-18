package com.ra.base_spring_boot.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ra.base_spring_boot.dto.request.LectureRequest;
import com.ra.base_spring_boot.dto.request.UpdateLectureRequest;
import com.ra.base_spring_boot.dto.response.LectureResponse;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.model.*;
import com.ra.base_spring_boot.model.constants.RoleName;
import com.ra.base_spring_boot.repository.*;
import com.ra.base_spring_boot.service.interfaces.ILectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class LectureServiceImpl implements ILectureService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private IndustryRepository industryRepository;
    @Autowired
    private Cloudinary cloudinary;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user hiện tại"));
    }

    private void checkOwnerOrAdmin(Long lectureId) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == RoleName.LECTURER) {
            Lecturer myLecturer = lectureRepository.findByUserId(currentUser.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên của user hiện tại"));
            if (!myLecturer.getId().equals(lectureId)) {
                throw new AccessDeniedException("Không có quyền truy cập");
            }
        }
    }


    @Override
    public PaginationResponse<LectureResponse> getAllTeachers(
            String keyword, String specialization, String status,
            int page, int size
    ) {
        Boolean deletedStatus = null;
        if (status != null && !status.trim().isEmpty()) {
            if (status.equalsIgnoreCase("deleted")) {
                deletedStatus = true;
            } else if (status.equalsIgnoreCase("active")) {
                deletedStatus = false;
            }
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Lecturer> lecturerPage = lectureRepository.searchLecturers(
                (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null,
                (specialization != null && !specialization.trim().isEmpty()) ? specialization.trim() : null,
                deletedStatus,
                pageable
        );

        List<LectureResponse> responses = lecturerPage.getContent()
                .stream()
                .map(LectureServiceImpl::toResponse)
                .toList();

        return PaginationResponse.of(
                responses,
                lecturerPage.getNumber(),
                lecturerPage.getSize(),
                lecturerPage.getTotalElements()
        );
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

        MultipartFile image = request.getImage();
        if (image != null && !image.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(
                        image.getBytes(),
                        ObjectUtils.emptyMap()
                );
                lecturer.setImageUrl(uploadResult.get("secure_url").toString());
            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi tải ảnh", e);
            }
        }

        Lecturer savedLecturer = lectureRepository.save(lecturer);
        return toResponse(savedLecturer);
    }

    @Override
    public LectureResponse updateTeacher(Long id, UpdateLectureRequest request) {
        checkOwnerOrAdmin(id);
        Lecturer lecturer = lectureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên với ID: " + id));

        lecturer.setLecturerCode(request.getLecturerCode());
        lecturer.setDateOfBirth(request.getDateOfBirth());
        lecturer.setHometown(request.getHometown());
        lecturer.setWorkYear(request.getWorkYear());

        if (request.getDepartmentId() != null) {
            Departments department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khoa với ID: " + request.getDepartmentId()));
            lecturer.setDepartment(department);
        }

        if (request.getIndustryId() != null) {
            Industry industry = industryRepository.findById(request.getIndustryId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên ngành với ID: " + request.getIndustryId()));
            lecturer.setIndustry(industry);
        }

        MultipartFile image = request.getImage();
        if (image != null && !image.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(
                        image.getBytes(),
                        ObjectUtils.emptyMap()
                );
                lecturer.setImageUrl(uploadResult.get("secure_url").toString());
            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi tải ảnh", e);
            }
        }

        Lecturer updatedLecturer = lectureRepository.save(lecturer);
        return toResponse(updatedLecturer);
    }


    @Override
    public void deleteTeacher(Long id) {
        checkOwnerOrAdmin(id);
        Lecturer lecturer = lectureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên với ID: " + id));
        lecturer.setDeleted(true);
        lectureRepository.save(lecturer);
    }

    @Override
    public LectureResponse getTeacher(Long id) {
        checkOwnerOrAdmin(id);
        Lecturer lecturer = lectureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên với ID: " + id));
        return toResponse(lecturer);
    }

    @Override
    public LectureResponse updateStatus(Long id, String status) {
        checkOwnerOrAdmin(id);
        Lecturer lecturer = lectureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên với ID: " + id));
        if ("DELETED".equalsIgnoreCase(status)) {
            lecturer.setDeleted(true);
        } else if ("ACTIVE".equalsIgnoreCase(status)) {
            lecturer.setDeleted(false);
        } else {
            throw new RuntimeException("Trạng thái không hợp lệ: " + status);
        }
        Lecturer savedLecturer = lectureRepository.save(lecturer);
        return toResponse(savedLecturer);
    }

    public static LectureResponse toResponse(Lecturer lecturer) {
        return LectureResponse.builder()
                .id(lecturer.getId())
                .fullName(lecturer.getUser() != null ? lecturer.getUser().getFullName() : null)
                .email(lecturer.getUser() != null ? lecturer.getUser().getEmail() : null)
                .dateOfBirth(lecturer.getDateOfBirth())
                .lecturerCode(lecturer.getLecturerCode())
                .hometown(lecturer.getHometown())
                .departmentId(lecturer.getDepartment() != null ? lecturer.getDepartment().getId() : null)
                .industryId(lecturer.getIndustry() != null ? lecturer.getIndustry().getId() : null)
                .workYear(lecturer.getWorkYear())
                .imageUrl(lecturer.getImageUrl())
                .build();
    }
}
