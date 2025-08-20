package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.advice.PartnerAlreadyExistsException;
import com.ra.base_spring_boot.dto.request.PartnerDTO;
import com.ra.base_spring_boot.model.Industry;
import com.ra.base_spring_boot.model.Partner;
import com.ra.base_spring_boot.repository.IndustryRepository;
import com.ra.base_spring_boot.repository.PartnerRepository;
import com.ra.base_spring_boot.dto.response.PartnerResponseDTO;
import com.ra.base_spring_boot.service.interfaces.ICloudinaryService;
import com.ra.base_spring_boot.service.interfaces.IPartnerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements IPartnerService {

    private final PartnerRepository partnerRepository;
    private final IndustryRepository industryRepository;
    private final ICloudinaryService cloudinaryService;

    @Override
    public PartnerResponseDTO createPartner(PartnerDTO dto) {
        if (partnerRepository.existsByPartnerCode(dto.getPartnerCode())) {
            throw new PartnerAlreadyExistsException("Mã đối tác đã tồn tại");
        }
        if (partnerRepository.existsByName(dto.getName())) {
            throw new PartnerAlreadyExistsException("Tên đối tác đã tồn tại");
        }

        Partner partner = new Partner();
        mapDtoToEntity(dto, partner);
        partner = partnerRepository.save(partner);
        return mapEntityToResponse(partner);
    }

    @Override
    @Transactional
    public PartnerResponseDTO updatePartner(Long id, PartnerDTO dto) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy đối tác"));

        if (!partner.getPartnerCode().equals(dto.getPartnerCode()) &&
                partnerRepository.existsByPartnerCode(dto.getPartnerCode())) {
            throw new PartnerAlreadyExistsException("Mã đối tác đã tồn tại");
        }
        if (!partner.getName().equals(dto.getName()) &&
                partnerRepository.existsByName(dto.getName())) {
            throw new PartnerAlreadyExistsException("Tên đối tác đã tồn tại");
        }

        mapDtoToEntity(dto, partner);
        partner = partnerRepository.save(partner);
        partner.getIndustries().size();

        return mapEntityToResponse(partner);
    }


    @Override
    public PartnerResponseDTO getPartnerById(Long id) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy đối tác"));
        return mapEntityToResponse(partner);
    }

    @Override
    public List<PartnerResponseDTO> getAllPartners() {
        return partnerRepository.findAll()
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePartner(Long id) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy đối tác"));

        if (partner.getAvatarUrl() != null) {
            String publicId = cloudinaryService.extractPublicIdFromUrl(partner.getAvatarUrl());
            cloudinaryService.deleteImage(publicId);
        }

        partnerRepository.delete(partner);
    }

    @Override
    public Page<PartnerResponseDTO> searchPartners(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Partner> partnerPage = partnerRepository.searchWithIndustries(keyword, pageable);

        if (partnerPage.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy đối tác");
        }

        return partnerPage.map(this::mapEntityToResponse);
    }



    private void mapDtoToEntity(PartnerDTO dto, Partner partner) {
        partner.setPartnerCode(dto.getPartnerCode());
        partner.setName(dto.getName());
        partner.setDescription(dto.getDescription());
        partner.setNumberOfEmployees(dto.getNumberOfEmployees());
        partner.setNumberOfCourses(dto.getNumberOfCourses());
        partner.setAddress(dto.getAddress());
        partner.setStatus(dto.getStatus());

        if (dto.getAvatarUrl() != null && !dto.getAvatarUrl().isEmpty()) {
            String uploadedUrl = cloudinaryService.uploadImage(dto.getAvatarUrl(), "partners");
            partner.setAvatarUrl(uploadedUrl);
        } else if (dto.getAvatar() != null) {
            partner.setAvatarUrl(dto.getAvatar());
        }

        if (dto.getIndustryIds() != null && !dto.getIndustryIds().isEmpty()) {
            Set<Industry> industries = industryRepository.findAllById(dto.getIndustryIds())
                    .stream().collect(Collectors.toSet());
            partner.setIndustries(industries);
        }
    }

    private PartnerResponseDTO mapEntityToResponse(Partner partner) {
        return PartnerResponseDTO.builder()
                .id(partner.getId())
                .partnerCode(partner.getPartnerCode())
                .name(partner.getName())
                .description(partner.getDescription())
                .numberOfEmployees(partner.getNumberOfEmployees())
                .numberOfCourses(partner.getNumberOfCourses())
                .address(partner.getAddress())
                .avatarUrl(partner.getAvatarUrl())
                .status(partner.getStatus())
                .industries(
                        partner.getIndustries() != null
                                ? partner.getIndustries().stream()
                                .map(Industry::getName)
                                .collect(Collectors.toSet())
                                : null
                )
                .build();
    }
}
