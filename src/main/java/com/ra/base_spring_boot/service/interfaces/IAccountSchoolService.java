package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.StaffDTO;
import com.ra.base_spring_boot.dto.response.StaffResponseDTO;
import org.springframework.data.domain.Page;

public interface IAccountSchoolService {
    StaffResponseDTO createAccountSchool(StaffDTO staff);
    StaffResponseDTO updateAccountSchool(Long id, StaffDTO staff);
    StaffResponseDTO getAccountSchoolById(Long id);
    void deleteAccountSchool(Long id);
    Page<StaffResponseDTO> getAllAccountSchools(String keyword, int page, int size);
}
