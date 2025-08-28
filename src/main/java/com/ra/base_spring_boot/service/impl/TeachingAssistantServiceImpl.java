package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.PaginationDTO;
import com.ra.base_spring_boot.dto.request.TeachingAssistantRequestDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.dto.response.TeachingAssistantResponseDTO;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.Departments;
import com.ra.base_spring_boot.model.Lecturer;
import com.ra.base_spring_boot.model.TeachingAssistant;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.model.constants.AccountStatus;
import com.ra.base_spring_boot.model.constants.RoleName;
import com.ra.base_spring_boot.repository.DepartmentRepository;
import com.ra.base_spring_boot.repository.LectureRepository;
import com.ra.base_spring_boot.repository.TeachingAssistantRepository;
import com.ra.base_spring_boot.repository.UserRepository;
import com.ra.base_spring_boot.service.interfaces.ICloudinaryService;
import com.ra.base_spring_boot.service.interfaces.ITeachingAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeachingAssistantServiceImpl implements ITeachingAssistantService {

    @Autowired
    private TeachingAssistantRepository teachingAssistantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LectureRepository lecturerRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ICloudinaryService cloudinaryService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public TeachingAssistantResponseDTO createTeachingAssistant(TeachingAssistantRequestDTO requestDTO) {
        if (userRepository.existsByUsername(requestDTO.getUsername())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại: " + requestDTO.getUsername());
        }

        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại: " + requestDTO.getEmail());
        }

        if (userRepository.existsByPhoneNumber(requestDTO.getPhoneNumber())) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại: " + requestDTO.getPhoneNumber());
        }
        Lecturer lecturer = lecturerRepository.findById(requestDTO.getAssignedLecturerId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giáo viên với id: " + requestDTO.getAssignedLecturerId()));

        Departments department = departmentRepository.findById(requestDTO.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khoa với id: " + requestDTO.getDepartmentId()));
        User user = new User();
        user.setUsername(requestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setFullName(requestDTO.getFullName());
        user.setEmail(requestDTO.getEmail());
        user.setPhoneNumber(requestDTO.getPhoneNumber());
        user.setRole(RoleName.ASSISTANT);
        user.setStatus(AccountStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);


        TeachingAssistant assistant = new TeachingAssistant();
        assistant.setUser(user);
        assistant.setTaCode(generateUniqueEmployeeCode());
        assistant.setAssignedLecturer(lecturer);
        assistant.setDepartment(department);

        if (requestDTO.getAvatarUrl() != null && !requestDTO.getAvatarUrl().isEmpty()) {
            String uploadedUrl = cloudinaryService.uploadImage(requestDTO.getAvatarUrl(), "assistants");
            assistant.setAvatar(uploadedUrl);
        }

        teachingAssistantRepository.save(assistant);

        return mapToResponseDTO(assistant);
    }

    @Override
    public TeachingAssistantResponseDTO updateTeachingAssistant(Long id, TeachingAssistantRequestDTO requestDTO) {
        TeachingAssistant assistant = teachingAssistantRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy trợ giảng với id: " + id));

        if (requestDTO.getFullName() != null) {
            assistant.getUser().setFullName(requestDTO.getFullName());
        }
        if (requestDTO.getEmail() != null) {
            assistant.getUser().setEmail(requestDTO.getEmail());
        }
        if (requestDTO.getPhoneNumber() != null) {
            assistant.getUser().setPhoneNumber(requestDTO.getPhoneNumber());
        }

        if (requestDTO.getAssignedLecturerId() != null) {
            Lecturer lecturer = lecturerRepository.findById(requestDTO.getAssignedLecturerId())
                    .orElseThrow(() -> new HttpNotFound("Không tìm thấy giáo viên với id: " + requestDTO.getAssignedLecturerId()));
            assistant.setAssignedLecturer(lecturer);
        }

        if (requestDTO.getDepartmentId() != null) {
            Departments department = departmentRepository.findById(requestDTO.getDepartmentId())
                    .orElseThrow(() -> new HttpNotFound("Không tìm thấy khoa với id: " + requestDTO.getDepartmentId()));
            assistant.setDepartment(department);
        }

        if (requestDTO.getAvatarUrl() != null && !requestDTO.getAvatarUrl().isEmpty()) {
            String uploadedUrl = cloudinaryService.uploadImage(requestDTO.getAvatarUrl(), "assistants");
            assistant.setAvatar(uploadedUrl);
        }

        teachingAssistantRepository.save(assistant);
        return mapToResponseDTO(assistant);
    }

    @Override
    public void deleteTeachingAssistant(Long id) {
        TeachingAssistant assistant = teachingAssistantRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy trợ giảng với id: " + id));
        teachingAssistantRepository.delete(assistant);
    }

    @Override
    public TeachingAssistantResponseDTO getTeachingAssistantById(Long id) {
        TeachingAssistant assistant = teachingAssistantRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy trợ giảng với id: " + id));
        return mapToResponseDTO(assistant);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<TeachingAssistantResponseDTO> getAllTeachingAssistants(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<TeachingAssistant> assistants;
        if (keyword != null && !keyword.trim().isEmpty()) {
            assistants = teachingAssistantRepository.searchByKeyword(keyword, pageable);
        } else {
            assistants = teachingAssistantRepository.findAll(pageable);
        }

        List<TeachingAssistantResponseDTO> assistantDTOs = assistants.getContent().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        PaginationDTO paginationDTO = new PaginationDTO(
                assistants.getNumber(),
                assistants.getSize(),
                assistants.getTotalPages(),
                assistants.getTotalElements()
        );
        return new PaginationResponse<>(assistantDTOs, paginationDTO);
    }



    private String generateUniqueEmployeeCode() {
        String code;
        do {
            code = "TA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (teachingAssistantRepository.existsByEmployeeCode(code));
        return code;
    }

    private TeachingAssistantResponseDTO mapToResponseDTO(TeachingAssistant assistant) {
        return TeachingAssistantResponseDTO.builder()
                .userId(assistant.getUser().getId())
                .username(assistant.getUser().getUsername())
                .fullName(assistant.getUser().getFullName())
                .email(assistant.getUser().getEmail())
                .phoneNumber(assistant.getUser().getPhoneNumber())
                .role(assistant.getUser().getRole())
                .status(assistant.getUser().getStatus())
                .createdAt(assistant.getUser().getCreatedAt())
                .updatedAt(assistant.getUser().getUpdatedAt())
                .taId(assistant.getId())
                .taCode(assistant.getTaCode())
                .assignedLecturerName(
                        assistant.getAssignedLecturer() != null
                                ? assistant.getAssignedLecturer().getUser().getFullName()
                                : null
                )
                .departmentName(
                        assistant.getDepartment() != null
                                ? assistant.getDepartment().getName()
                                : null
                )
                .build();
    }


}
