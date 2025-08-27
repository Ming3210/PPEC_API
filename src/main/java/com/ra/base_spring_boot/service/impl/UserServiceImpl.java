package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.dto.response.StudentResponse;
import com.ra.base_spring_boot.dto.response.UserResponseDTO;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.*;
import com.ra.base_spring_boot.repository.*;
import com.ra.base_spring_boot.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
        Page<User> users = userRepository.findAll(pageable);
        Page<UserResponseDTO> userResponses = users.map(this::toUserResponse);

        return PaginationResponse.of(userResponses);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new HttpNotFound("Không tìm thấy người dùng"));
        return toUserResponse(user);
    }

    private UserResponseDTO toUserResponse(User user) {
        LocalDate dateOfBirth = null;

        switch (user.getRole()) {
            case STUDENT -> {
                Student student = studentRepository.findByUser(user);
                dateOfBirth = student.getDateOfBirth();
            }
            case LECTURER -> {
                Lecturer lecturer = lecturerRepository.findByUser(user);
                dateOfBirth = lecturer.getDateOfBirth();
            }
            case SERVICE_STAFF -> {
                ServiceStaff serviceStaff = serviceStaffRepository.findByUser(user);
                dateOfBirth = serviceStaff.getDateOfBirth();
            }
            case STAFF -> {
                Staff staff = staffRepository.findByUser(user);
                dateOfBirth = staff.getDateOfBirth();
            }
            default -> {
            }
        }

        return UserResponseDTO.builder()
                .id((user.getId()))
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(null)
                .dateOfBirth(dateOfBirth)
                .role(user.getRole())
                .build();
    }


}
