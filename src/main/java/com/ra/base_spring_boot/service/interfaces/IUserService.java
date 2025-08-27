package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.dto.response.UserResponseDTO;

public interface IUserService {
    PaginationResponse<UserResponseDTO> getAllUser(int page, int size, String sortBy, boolean sortDirection, String keyword);

    UserResponseDTO getUserById(Long id);
}
