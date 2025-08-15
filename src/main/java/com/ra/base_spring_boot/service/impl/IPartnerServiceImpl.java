package com.ra.base_spring_boot.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ra.base_spring_boot.advice.PartnerAlreadyExistsException;
import com.ra.base_spring_boot.dto.request.PartnerDTO;
import com.ra.base_spring_boot.model.Industry;
import com.ra.base_spring_boot.model.Partner;
import com.ra.base_spring_boot.repository.IndustryRepository;
import com.ra.base_spring_boot.repository.PartnerRepository;
import com.ra.base_spring_boot.service.interfaces.IPartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class IPartnerServiceImpl implements IPartnerService {

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private IndustryRepository industryRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Partner createPartner(PartnerDTO partnerDTO) {
        if (partnerRepository.existsByPartnerCode(partnerDTO.getPartnerCode())) {
            throw new PartnerAlreadyExistsException("Đối tác đã tồn tại");
        }

        if (partnerDTO.getIndustryIds() != null) {
            for (Long industryId : partnerDTO.getIndustryIds()) {
                if (!industryRepository.existsByIdCustom(industryId)) {
                    throw new IllegalArgumentException("Không tìm thấy ngành công nghiệp với ID: " + industryId);
                }
            }
        }
        String logoUrl = null;
        try {
            MultipartFile imageFile = partnerDTO.getAvatarUrl();
            if (imageFile != null && !imageFile.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
                logoUrl = uploadResult.get("url").toString();
            }
        } catch (IOException e) {
            throw new RuntimeException("Đăng ảnh lên không thành công", e);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi không xác định", e);
        }

        Set<Industry> industries = partnerDTO.getIndustryIds() != null
                ? new HashSet<>(industryRepository.findAllById(partnerDTO.getIndustryIds()))
                : new HashSet<>();

        Partner newPartner = Partner.builder()
                .partnerCode(partnerDTO.getPartnerCode())
                .name(partnerDTO.getName())
                .description(partnerDTO.getDescription())
                .numberOfEmployees(partnerDTO.getNumberOfEmployees())
                .numberOfCourses(partnerDTO.getNumberOfCourses())
                .address(partnerDTO.getAddress())
                .status(partnerDTO.getStatus())
                .industries(industries)
                .avatarUrl(logoUrl)
                .build();

        return partnerRepository.save(newPartner);
    }

    @Override
    public Partner updatePartner(Long id, PartnerDTO partnerDTO) {
        Partner existingPartner = partnerRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new IllegalArgumentException("Partner with id " + id + " not found"));

        if (partnerRepository.existsByPartnerCodeAndIdNot(partnerDTO.getPartnerCode(), id)) {
            throw new PartnerAlreadyExistsException("Đối tác đã tồn tại với mã đối tác: " + partnerDTO.getPartnerCode());
        }

        if (partnerDTO.getIndustryIds() != null) {
            for (Long industryId : partnerDTO.getIndustryIds()) {
                if (!industryRepository.existsByIdCustom(industryId)) {
                    throw new IllegalArgumentException("Không tìm thấy ngành công nghiệp với ID: " + industryId);
                }
            }
        }

        String logoUrl = null;
        try {
            MultipartFile imageFile = partnerDTO.getAvatarUrl();
            if (imageFile != null && !imageFile.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
                logoUrl = uploadResult.get("url").toString();
            }
        } catch (IOException e) {
            throw new RuntimeException("Đăng ảnh lên không thành công", e);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi không xác định", e);
        }

        Set<Industry> industries = partnerDTO.getIndustryIds() != null
                ? new HashSet<>(industryRepository.findAllById(partnerDTO.getIndustryIds()))
                : new HashSet<>();

        existingPartner.setPartnerCode(partnerDTO.getPartnerCode());
        existingPartner.setName(partnerDTO.getName());
        existingPartner.setDescription(partnerDTO.getDescription());
        existingPartner.setNumberOfEmployees(partnerDTO.getNumberOfEmployees());
        existingPartner.setNumberOfCourses(partnerDTO.getNumberOfCourses());
        existingPartner.setAddress(partnerDTO.getAddress());
        existingPartner.setStatus(partnerDTO.getStatus());
        existingPartner.setIndustries(industries);
        existingPartner.setAvatarUrl(logoUrl);

        return partnerRepository.save(existingPartner);
    }

    @Override
    public Partner getPartnerById(Long id) {
        return partnerRepository.findById((int) id.longValue())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đối tác với ID: " + id));
    }

    @Override
    public void deletePartner(Long id) {
        Partner existingPartner = partnerRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đối tác với ID: " + id));
        if (existingPartner.getIndustries() == null || existingPartner.getIndustries().isEmpty()) {
            throw new IllegalArgumentException("Không thể xóa đối tác đã có ngành nghiệp");
        }
        partnerRepository.delete(existingPartner);
    }

    @Override
    public List<Partner> getAllPartners() {
        return partnerRepository.findAll();
    }

    @Override
    public Page<Partner> searchPartners(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Partner> partnerPage;
        if (keyword == null || keyword.isEmpty()) {
            partnerPage = partnerRepository.findAll(pageable);
        } else {
            partnerPage = partnerRepository.findByNameContainingIgnoreCase(keyword, pageable);
        }
        if (partnerPage.isEmpty()) {
            throw new IllegalArgumentException("Không còn đối tác");
        }
        return partnerPage;
    }

    public Partner convertToPartner(PartnerDTO partnerDTO) {
        List<Industry> industryList = partnerDTO.getIndustryIds() != null
                ? industryRepository.findAllById(partnerDTO.getIndustryIds())
                : new ArrayList<>();
        Set<Industry> industries = new HashSet<>(industryList);

        return Partner.builder()
                .partnerCode(partnerDTO.getPartnerCode())
                .name(partnerDTO.getName())
                .description(partnerDTO.getDescription())
                .numberOfEmployees(partnerDTO.getNumberOfEmployees())
                .numberOfCourses(partnerDTO.getNumberOfCourses())
                .address(partnerDTO.getAddress())
                .avatarUrl(partnerDTO.getAvatar())
                .industries(industries)
                .status(partnerDTO.getStatus())
                .build();
    }
}