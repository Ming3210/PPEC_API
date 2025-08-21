package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.PaginationDTO;
import com.ra.base_spring_boot.dto.request.ServiceStaffRequestDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.dto.response.ServiceStaffResponseDTO;
import com.ra.base_spring_boot.model.Center;
import com.ra.base_spring_boot.model.ServiceStaff;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.model.constants.AccountStatus;
import com.ra.base_spring_boot.model.constants.RoleName;
import com.ra.base_spring_boot.repository.*;
import com.ra.base_spring_boot.service.interfaces.ICloudinaryService;
import com.ra.base_spring_boot.service.interfaces.IServiceStaffService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ServiceStaffServiceImpl implements IServiceStaffService {
    @Autowired
    private ServiceStaffRepository staffRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ICloudinaryService cloudinaryService;
    @Autowired
    private CenterRepository centerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ServiceStaffRepository serviceStaffRepository;
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private TeachingAssistantRepository teachingAssistantRepository;
    @Override
    @Transactional
    public ServiceStaffResponseDTO create(ServiceStaffRequestDTO requestDTO) {
        User isCheck = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (isCheck.getRole() == RoleName.STUDENT) {
            throw new IllegalArgumentException("Bạn không có quyền truy cập");
        }

        if (userRepository.findByUsername(requestDTO.getUsername()).isPresent()) {
        throw new IllegalArgumentException("Tên tài khoản đã tồn tại");
        }
                if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
        throw new IllegalArgumentException("Email đã tồn tại");
        }
                if (userRepository.findByPhoneNumber(requestDTO.getPhoneNumber()).isPresent()) {
        throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }
        Center center = centerRepository.findById(requestDTO.getPartnerId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy trung tâm với id: " + requestDTO.getPartnerId()));

        String uniqueCode = generateUniqueEmployeeCode();

        User user = new User();
        user.setUsername(requestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setFullName(requestDTO.getFullName());
        user.setEmail(requestDTO.getEmail());
        user.setPhoneNumber(requestDTO.getPhoneNumber());
        user.setRole(requestDTO.getRole());
        user.setStatus(AccountStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        ServiceStaff serviceStaff = new ServiceStaff();
        serviceStaff.setUser(user);
        serviceStaff.setStaffCode(uniqueCode);
        serviceStaff.setDateOfBirth(requestDTO.getDateOfBirth());
        serviceStaff.setHometown(requestDTO.getHometown());
        serviceStaff.setCenter(center);
        serviceStaff.setPosition(requestDTO.getPosition());

        if (requestDTO.getAvatar() != null && !requestDTO.getAvatar().isEmpty()) {
            String uploadedUrl = cloudinaryService.uploadImage(requestDTO.getAvatar(), "assistants");
            serviceStaff.setAvatarUrl(uploadedUrl);
        }


        staffRepository.save(serviceStaff);

        return toResponseDTO(user, serviceStaff, center);
    }


    @Override
    public ServiceStaffResponseDTO update(Long id, ServiceStaffRequestDTO requestDTO) {
        User isCheck = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (isCheck.getRole() == RoleName.STUDENT) {
            throw new IllegalArgumentException("Bạn không có quyền truy cập");
        }
        ServiceStaff serviceStaff = staffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên với id: " + id));

        User user = serviceStaff.getUser();

        userRepository.findByEmail(requestDTO.getEmail())
                .filter(u -> !u.getId().equals(user.getId()))
                .ifPresent(u -> { throw new IllegalArgumentException("Email đã tồn tại"); });

        userRepository.findByPhoneNumber(requestDTO.getPhoneNumber())
                .filter(u -> !u.getId().equals(user.getId()))
                .ifPresent(u -> { throw new IllegalArgumentException("Số điện thoại đã tồn tại"); });

        user.setFullName(requestDTO.getFullName());
        user.setEmail(requestDTO.getEmail());
        user.setPhoneNumber(requestDTO.getPhoneNumber());
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        }
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        serviceStaff.setDateOfBirth(requestDTO.getDateOfBirth());
        serviceStaff.setHometown(requestDTO.getHometown());
        serviceStaff.setPosition(requestDTO.getPosition());

        Center center = centerRepository.findById(requestDTO.getPartnerId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy trung tâm với id: " + requestDTO.getPartnerId()));
        serviceStaff.setCenter(center);

        if (requestDTO.getAvatar() != null && !requestDTO.getAvatar().isEmpty()) {
            String uploadedUrl = cloudinaryService.uploadImage(requestDTO.getAvatar(), "assistants");
            serviceStaff.setAvatarUrl(uploadedUrl);
        }
        staffRepository.save(serviceStaff);

        return toResponseDTO(user, serviceStaff, center);
    }


    @Override
    public void delete(Long id) {
        User isCheck = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (isCheck.getRole() == RoleName.STUDENT) {
            throw new IllegalArgumentException("Bạn không có quyền truy cập");
        }
        ServiceStaff serviceStaff = staffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên với id: " + id));

        User user = serviceStaff.getUser();

        if (user.getStatus() == AccountStatus.ACTIVE) {
            throw new IllegalStateException("Không thể xoá nhân viên đang ACTIVE. Vui lòng chuyển trạng thái trước.");
        }
        staffRepository.delete(serviceStaff);
        userRepository.delete(user);
    }


    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<ServiceStaffResponseDTO> getAll(String keyword, Pageable pageable) {
        Page<ServiceStaff> serviceStaffs = staffRepository.search(keyword, pageable);

        List<ServiceStaffResponseDTO> listDTO = serviceStaffs.getContent().stream()
                .map(s -> toResponseDTO(s.getUser(), s, s.getCenter()))
                .collect(Collectors.toList());

        PaginationDTO paginationDTO = new PaginationDTO(
                serviceStaffs.getNumber(),
                serviceStaffs.getSize(),
                serviceStaffs.getTotalPages(),
                serviceStaffs.getTotalElements()
        );
        return new PaginationResponse<>(listDTO, paginationDTO);
    }



    @Override
    public ServiceStaffResponseDTO getById(Long id) {
        return null;
    }
    private String generateUniqueEmployeeCode() {
        String code;
        do {
            code = "SS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (staffRepository.isCheckStaffCode(code));
        return code;
    }
    private ServiceStaffResponseDTO toResponseDTO(User user, ServiceStaff staff, Center center) {
        return ServiceStaffResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .dateOfBirth(staff.getDateOfBirth())
                .hometown(staff.getHometown())
                .avatarUrl(staff.getAvatarUrl())
                .position(staff.getPosition())
                .centerId(center.getId())
                .centerName(center.getName())
                .staffServiceCode(staff.getStaffCode())
                .build();
    }
}
