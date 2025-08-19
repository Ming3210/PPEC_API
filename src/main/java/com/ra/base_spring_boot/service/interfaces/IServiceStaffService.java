package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.ServiceStaffRequestDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.dto.response.ServiceStaffResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IServiceStaffService {
    ServiceStaffResponseDTO create(ServiceStaffRequestDTO requestDTO);
    ServiceStaffResponseDTO update(Long id, ServiceStaffRequestDTO requestDTO);
    void delete(Long id);
    PaginationResponse<ServiceStaffResponseDTO> getAll(String keyword, Pageable pageable);
    ServiceStaffResponseDTO getById(Long id);
}
