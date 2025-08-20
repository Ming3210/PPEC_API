package com.ra.base_spring_boot.service.impl;


import com.ra.base_spring_boot.dto.request.CenterRequestDTO;
import com.ra.base_spring_boot.dto.response.CenterResponseDTO;
import com.ra.base_spring_boot.exception.NotFoundException;
import com.ra.base_spring_boot.model.Center;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.repository.CenterRepository;
import com.ra.base_spring_boot.repository.UserRepository;
import com.ra.base_spring_boot.service.interfaces.ICenterService;
import com.ra.base_spring_boot.service.interfaces.ICloudinaryService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CenterServiceImpl implements ICenterService {

    private final CenterRepository centerRepository;
    private final UserRepository userRepository;
    private final ICloudinaryService cloudinaryService;

    @Override
    public CenterResponseDTO createCenter(CenterRequestDTO centerRequestDTO) {

        String logoUrl = null;
        MultipartFile logoFile = centerRequestDTO.getLogoFile();
        if (logoFile != null && !logoFile.isEmpty()) {
            logoUrl = cloudinaryService.uploadImage(logoFile, "centers");
        }

        User user = userRepository.findById(centerRequestDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy user với ID: " + centerRequestDTO.getUserId()));

        Center center = new Center();
        center.setAddress(centerRequestDTO.getAddress());
        center.setLogoUrl(logoUrl);
        center.setUser(user);

        Center savedCenter = centerRepository.save(center);
        return convertToCenterResponseDTO(savedCenter);
    }

    @Override
    @Transactional(readOnly = true)
    public CenterResponseDTO getCenterById(Long id) {
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy trung tâm với ID: " + id));
        return convertToCenterResponseDTO(center);
    }

    @Override
    public CenterResponseDTO updateCenter(Long id, CenterRequestDTO centerRequestDTO) {
        Center existingCenter = centerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy trung tâm với ID: " + id));

        String logoUrl = existingCenter.getLogoUrl();
        MultipartFile logoFile = centerRequestDTO.getLogoFile();
        if (logoFile != null && !logoFile.isEmpty()) {
            // Delete old logo if exists
            if (logoUrl != null) {
                String publicId = cloudinaryService.extractPublicIdFromUrl(logoUrl);
                if (publicId != null) {
                    cloudinaryService.deleteImage(publicId);
                }
            }
            // Upload new logo
            logoUrl = cloudinaryService.uploadImage(logoFile, "centers");
        }

        existingCenter.setAddress(centerRequestDTO.getAddress());
        existingCenter.setLogoUrl(logoUrl);

        Center updatedCenter = centerRepository.save(existingCenter);
        return convertToCenterResponseDTO(updatedCenter);
    }

    @Override
    public void deleteCenter(Long id) {
        Center existingCenter = centerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy trung tâm với ID: " + id));

        if (existingCenter.getLogoUrl() != null) {
            String publicId = cloudinaryService.extractPublicIdFromUrl(existingCenter.getLogoUrl());
            if (publicId != null) {
                cloudinaryService.deleteImage(publicId);
            }
        }

        centerRepository.delete(existingCenter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CenterResponseDTO> getAllCenters() {
        return centerRepository.findAll().stream()
                .map(this::convertToCenterResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CenterResponseDTO> searchCenters(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Center> centerPage;

        if (keyword == null || keyword.isEmpty()) {
            centerPage = centerRepository.findAll(pageable);
        } else {
            centerPage = centerRepository
                    .findByUserFullNameContainingIgnoreCaseOrAddressContainingIgnoreCase(keyword, keyword, pageable);
        }

        if (centerPage.isEmpty()) {
            throw new NotFoundException("Không tìm thấy trung tâm nào");
        }

        return centerPage.map(this::convertToCenterResponseDTO);
    }

    private CenterResponseDTO convertToCenterResponseDTO(Center center) {
        return new CenterResponseDTO(
                center.getId(),
                center.getAddress(),
                center.getLogoUrl(),
                center.getUser().getId(),
                center.getUser().getFullName(),
                center.getCreatedAt(),
                center.getUpdatedAt()
        );
    }
}

