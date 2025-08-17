package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.StaffDTO;
import com.ra.base_spring_boot.dto.response.StaffResponseDTO;
import com.ra.base_spring_boot.exception.HttpConflict;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.School;
import com.ra.base_spring_boot.model.Staff;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.model.UserDetail;
import com.ra.base_spring_boot.model.constants.AccountStatus;
import com.ra.base_spring_boot.model.constants.RoleName;
import com.ra.base_spring_boot.repository.*;
import com.ra.base_spring_boot.service.interfaces.IAccountSchoolService;
import com.ra.base_spring_boot.service.interfaces.ICloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountSchoolServiceImpl implements IAccountSchoolService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final StaffRepository staffRepository;
    private final SchoolRepository schoolRepository;
    private final PasswordEncoder passwordEncoder;
    private final ICloudinaryService cloudinaryService;

    @Override
    public StaffResponseDTO createAccountSchool(StaffDTO staffDTO) {
        if (userRepository.existsByUsername(staffDTO.getUsername())) {
            throw new HttpConflict("Tên đăng nhập đã tồn tại");
        }
        if (userRepository.existsByEmail(staffDTO.getEmail())) {
            throw new HttpConflict("Email đã tồn tại");
        }

        User user = new User();
        user.setUsername(staffDTO.getUsername());
        user.setPassword(passwordEncoder.encode(staffDTO.getPassword()));
        user.setFullName(staffDTO.getFullName());
        user.setEmail(staffDTO.getEmail());
        user.setPhoneNumber(staffDTO.getPhoneNumber());
        user.setRole(RoleName.STAFF);
        user.setStatus(AccountStatus.ACTIVE);
        userRepository.save(user);

        UserDetail userDetail = userDetailRepository.findByUserId(user.getId()).orElse(new UserDetail());
        userDetail.setUser(user);
        userDetail.setDob(staffDTO.getDateOfBirth());
        userDetail.setHometown(staffDTO.getHometown());

        if (staffDTO.getAvatarUrl() != null && !staffDTO.getAvatarUrl().isEmpty()) {
            String uploadedUrl = cloudinaryService.uploadImage(staffDTO.getAvatarUrl(), "staffs");
            userDetail.setAvatarUrl(uploadedUrl);
        }else if (staffDTO.getAvatar() != null) {
            userDetail.setAvatarUrl(staffDTO.getAvatar());
        }

        userDetailRepository.save(userDetail);

        School school = schoolRepository.findById(staffDTO.getSchoolId())
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy trường"));

        Staff staff = new Staff();
        staff.setUser(user);
        staff.setSchool(school);
        staff.setEmployeeCode(generateUniqueEmployeeCode());
        staff.setStartYear(staffDTO.getStartYear());
        staff.setPosition(staffDTO.getPosition());
        staffRepository.save(staff);

        return mapToResponse(user, userDetail, staff);
    }

    @Override
    public StaffResponseDTO updateAccountSchool(Long staffId, StaffDTO staffDTO) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy nhân viên"));

        User user = staff.getUser();
        user.setFullName(staffDTO.getFullName());
        user.setEmail(staffDTO.getEmail());
        user.setPhoneNumber(staffDTO.getPhoneNumber());
        userRepository.save(user);

        UserDetail userDetail = userDetailRepository.findByUserId(user.getId())
                .orElse(new UserDetail());
        userDetail.setUser(user);
        userDetail.setDob(staffDTO.getDateOfBirth());
        userDetail.setHometown(staffDTO.getHometown());

        if (staffDTO.getAvatarUrl() != null && !staffDTO.getAvatarUrl().isEmpty()) {
            String uploadedUrl = cloudinaryService.uploadImage(staffDTO.getAvatarUrl(), "staffs");
            userDetail.setAvatarUrl(uploadedUrl);
        }
        userDetailRepository.save(userDetail);

        School school = schoolRepository.findById(staffDTO.getSchoolId())
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy trường"));
        staff.setSchool(school);
        staff.setStartYear(staffDTO.getStartYear());
        staff.setPosition(staffDTO.getPosition());
        staffRepository.save(staff);

        return mapToResponse(user, userDetail, staff);
    }

    @Override
    public StaffResponseDTO getAccountSchoolById(Long id) {
        return null;
    }

    @Override
    public void deleteAccountSchool(Long staffId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy nhân viên"));
        User user = staff.getUser();
        user.setStatus(AccountStatus.INACTIVE);
        userRepository.save(user);
    }

    @Override
    public Page<StaffResponseDTO> getAllAccountSchools(String keyword, int page, int size) {
        return null;
    }

    private StaffResponseDTO mapToResponse(User user, UserDetail userDetail, Staff staff) {
        return StaffResponseDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .dateOfBirth(userDetail != null ? userDetail.getDob() : null)
                .hometown(userDetail != null ? userDetail.getHometown() : null)
                .avatarUrl(userDetail != null ? userDetail.getAvatarUrl() : null)
                .employeeCode(staff.getEmployeeCode())
                .startYear(staff.getStartYear())
                .position(staff.getPosition())
                .schoolId(staff.getSchool().getId())
                .schoolName(staff.getSchool().getSchoolName())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .build();
    }

    private String generateUniqueEmployeeCode() {
        String code;
        do {
            code = "EMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (staffRepository.existsByEmployeeCode(code));
        return code;
    }
}
