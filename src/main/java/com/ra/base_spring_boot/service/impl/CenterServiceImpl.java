package com.ra.base_spring_boot.service.impl;


import com.ra.base_spring_boot.dto.request.CenterRequestDTO;
import com.ra.base_spring_boot.dto.response.CenterResponseDTO;
import com.ra.base_spring_boot.exception.NotFoundException;
import com.ra.base_spring_boot.exception.ConflictException;
import com.ra.base_spring_boot.exception.BadRequestException;
import com.ra.base_spring_boot.model.Center;
import com.ra.base_spring_boot.repository.CenterRepository;
import com.ra.base_spring_boot.repository.CourseOffRepository;
import com.ra.base_spring_boot.repository.CourseRepository;
import com.ra.base_spring_boot.service.interfaces.ICenterService;
import com.ra.base_spring_boot.service.interfaces.ICloudinaryService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CenterServiceImpl implements ICenterService {

    private final CenterRepository centerRepository;
    private final CourseRepository courseRepository;
    private final ICloudinaryService cloudinaryService;

    @Override
    public CenterResponseDTO createCenter(CenterRequestDTO centerRequestDTO) {
        // Check unique name
        if (centerRepository.existsByName(centerRequestDTO.getName())) {
            throw new ConflictException("Tên trung tâm đã tồn tại: " + centerRequestDTO.getName());
        }

        // Upload logo if provided
        String logoUrl = null;
        MultipartFile logoFile = centerRequestDTO.getLogoFile();
        if (logoFile != null && !logoFile.isEmpty()) {
            logoUrl = cloudinaryService.uploadImage(logoFile, "centers");
        }

        Center center = new Center();
        center.setName(centerRequestDTO.getName());
        center.setAddress(centerRequestDTO.getAddress());
        center.setLogoUrl(logoUrl);

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

        // Check unique name (exclude current)
        if (centerRepository.existsByNameAndIdNot(centerRequestDTO.getName(), id)) {
            throw new ConflictException("Tên trung tâm đã tồn tại: " + centerRequestDTO.getName());
        }

        // Upload new logo if provided
        String logoUrl = existingCenter.getLogoUrl(); // Keep existing if no new logo
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

        existingCenter.setName(centerRequestDTO.getName());
        existingCenter.setAddress(centerRequestDTO.getAddress());
        existingCenter.setLogoUrl(logoUrl);

        Center updatedCenter = centerRepository.save(existingCenter);
        return convertToCenterResponseDTO(updatedCenter);
    }

    @Override
    public void deleteCenter(Long id) {
        Center existingCenter = centerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy trung tâm với ID: " + id));

        // Check if center can be deleted
        if (!canDeleteCenter(id)) {
            throw new BadRequestException("Không thể xóa trung tâm này vì có khóa học đang sử dụng");
        }

        // Delete logo from Cloudinary if exists
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
            centerPage = centerRepository.findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(
                    keyword, keyword, pageable);
        }

        if (centerPage.isEmpty()) {
            throw new NotFoundException("Không tìm thấy trung tâm nào");
        }

        return centerPage.map(this::convertToCenterResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canDeleteCenter(Long id) {
        long courseCount = courseRepository.countByCenterId(id);
        return courseCount == 0;
    }

    private CenterResponseDTO convertToCenterResponseDTO(Center center) {
        Long totalCourses = courseRepository.countByCenterId(center.getId());

        return new CenterResponseDTO(
                center.getId(),
                center.getName(),
                center.getAddress(),
                center.getLogoUrl(),
                center.getCreatedAt(),
                totalCourses
        );
    }
}

