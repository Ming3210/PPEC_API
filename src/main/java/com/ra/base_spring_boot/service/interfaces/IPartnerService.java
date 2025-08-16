package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.PartnerDTO;
import com.ra.base_spring_boot.dto.response.PartnerResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPartnerService {
    PartnerResponseDTO createPartner(PartnerDTO dto);
    PartnerResponseDTO updatePartner(Long id, PartnerDTO dto);
    PartnerResponseDTO getPartnerById(Long id);
    List<PartnerResponseDTO> getAllPartners();
    void deletePartner(Long id);
    Page<PartnerResponseDTO> searchPartners(String keyword, int page, int size);
}
