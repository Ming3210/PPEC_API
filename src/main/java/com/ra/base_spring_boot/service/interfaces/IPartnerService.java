package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.PartnerDTO;
import com.ra.base_spring_boot.model.Partner;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPartnerService {
    Partner createPartner(PartnerDTO partner);
    Partner updatePartner(Long id, PartnerDTO partner);
    Partner getPartnerById(Long id);
    void deletePartner(Long id);
    List<Partner> getAllPartners();
    Page<Partner> searchPartners(String keyword, int page, int size);
}
