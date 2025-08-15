package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.CenterRequestDTO;
import com.ra.base_spring_boot.dto.response.CenterResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICenterService {
    CenterResponseDTO createCenter(CenterRequestDTO centerRequestDTO);
    CenterResponseDTO getCenterById(Long id);
    CenterResponseDTO updateCenter(Long id, CenterRequestDTO centerRequestDTO);
    void deleteCenter(Long id);
    List<CenterResponseDTO> getAllCenters();
    Page<CenterResponseDTO> searchCenters(String keyword, int page, int size);
    boolean canDeleteCenter(Long id);
}
