package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.response.SkillResponseDTO;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.exception.HttpConflict;
import com.ra.base_spring_boot.model.CourseOff;
import com.ra.base_spring_boot.model.Partner;
import com.ra.base_spring_boot.model.Skill;
import com.ra.base_spring_boot.dto.request.CourseOffRequestDTO;
import com.ra.base_spring_boot.dto.request.CourseOffSearchFilterDTO;
import com.ra.base_spring_boot.dto.request.PaginationDTO;
import com.ra.base_spring_boot.dto.response.CourseOffResponseDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.repository.CourseOffRepository;
import com.ra.base_spring_boot.repository.PartnerRepository;
import com.ra.base_spring_boot.repository.SkillRepository;
import com.ra.base_spring_boot.service.interfaces.ICourseOffService;
import com.ra.base_spring_boot.service.interfaces.ICloudinaryService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseOffServiceImpl implements ICourseOffService {

    private final CourseOffRepository courseOffRepository;
    private final PartnerRepository partnerRepository;
    private final SkillRepository skillRepository;
    private final ICloudinaryService cloudinaryService;

    @Override
    public CourseOffResponseDTO createCourseOff(CourseOffRequestDTO courseOffRequestDTO) {
        if (courseOffRepository.existsByName(courseOffRequestDTO.getName())) {
            throw new HttpConflict("Tên khóa học đã tồn tại: " + courseOffRequestDTO.getName());
        }

        Partner partner = partnerRepository.findById(Math.toIntExact(courseOffRequestDTO.getPartnerId()))
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy trung tâm với ID: " + courseOffRequestDTO.getPartnerId()));

        Set<Skill> skills = new HashSet<>();
        if (courseOffRequestDTO.getSkillIds() != null && !courseOffRequestDTO.getSkillIds().isEmpty()) {
            for (Long skillId : courseOffRequestDTO.getSkillIds()) {
                Skill skill = skillRepository.findById(skillId)
                        .orElseThrow(() -> new HttpNotFound("Không tìm thấy kỹ năng với ID: " + skillId));
                skills.add(skill);
            }
        }

        String bannerUrl = null;
        MultipartFile bannerFile = courseOffRequestDTO.getBannerFile();
        if (bannerFile != null && !bannerFile.isEmpty()) {
            bannerUrl = cloudinaryService.uploadImage(bannerFile, "courses-off");
        }

        CourseOff courseOff = new CourseOff();
        courseOff.setName(courseOffRequestDTO.getName());
        courseOff.setDescription(courseOffRequestDTO.getDescription());
        courseOff.setTargetAudience(courseOffRequestDTO.getTargetAudience());
        courseOff.setEstimatedHours(courseOffRequestDTO.getEstimatedHours());
        courseOff.setPrice(courseOffRequestDTO.getPrice());
        courseOff.setBannerUrl(bannerUrl);
        courseOff.setPartner(partner);
        courseOff.setSkills(skills);
        courseOff.setCreatedAt(LocalDateTime.now());
        courseOff.setUpdatedAt(LocalDateTime.now());

        CourseOff savedCourseOff = courseOffRepository.save(courseOff);
        return convertToCourseOffResponseDTO(savedCourseOff);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseOffResponseDTO getCourseOffById(Long id) {
        CourseOff courseOff = courseOffRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy khóa học với ID: " + id));
        return convertToCourseOffResponseDTO(courseOff);
    }

    @Override
    public CourseOffResponseDTO updateCourseOff(Long id, CourseOffRequestDTO courseOffRequestDTO) {
        CourseOff existingCourseOff = courseOffRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy khóa học với ID: " + id));

        if (courseOffRepository.existsByNameAndIdNot(courseOffRequestDTO.getName(), id)) {
            throw new HttpConflict("Tên khóa học đã tồn tại: " + courseOffRequestDTO.getName());
        }

        Partner partner = partnerRepository.findById(Math.toIntExact(courseOffRequestDTO.getPartnerId()))
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy trung tâm với ID: " + courseOffRequestDTO.getPartnerId()));

        // Validate skills exist
        Set<Skill> skills = new HashSet<>();
        if (courseOffRequestDTO.getSkillIds() != null && !courseOffRequestDTO.getSkillIds().isEmpty()) {
            for (Long skillId : courseOffRequestDTO.getSkillIds()) {
                Skill skill = skillRepository.findById(skillId)
                        .orElseThrow(() -> new HttpNotFound("Không tìm thấy kỹ năng với ID: " + skillId));
                skills.add(skill);
            }
        }

        // Upload new banner if provided
        String bannerUrl = existingCourseOff.getBannerUrl(); // Keep existing if no new banner
        MultipartFile bannerFile = courseOffRequestDTO.getBannerFile();
        if (bannerFile != null && !bannerFile.isEmpty()) {
            // Delete old banner if exists
            if (bannerUrl != null) {
                String publicId = cloudinaryService.extractPublicIdFromUrl(bannerUrl);
                if (publicId != null) {
                    cloudinaryService.deleteImage(publicId);
                }
            }
            // Upload new banner
            bannerUrl = cloudinaryService.uploadImage(bannerFile, "courses-off");
        }

        existingCourseOff.setName(courseOffRequestDTO.getName());
        existingCourseOff.setDescription(courseOffRequestDTO.getDescription());
        existingCourseOff.setTargetAudience(courseOffRequestDTO.getTargetAudience());
        existingCourseOff.setEstimatedHours(courseOffRequestDTO.getEstimatedHours());
        existingCourseOff.setPrice(courseOffRequestDTO.getPrice());
        existingCourseOff.setBannerUrl(bannerUrl);
        existingCourseOff.setPartner(partner);
        existingCourseOff.setSkills(skills);
        existingCourseOff.setUpdatedAt(LocalDateTime.now());

        CourseOff updatedCourseOff = courseOffRepository.save(existingCourseOff);
        return convertToCourseOffResponseDTO(updatedCourseOff);
    }

    @Override
    public void deleteCourseOff(Long id) {
        CourseOff existingCourseOff = courseOffRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy khóa học với ID: " + id));

        // Delete banner from Cloudinary if exists
        if (existingCourseOff.getBannerUrl() != null) {
            String publicId = cloudinaryService.extractPublicIdFromUrl(existingCourseOff.getBannerUrl());
            if (publicId != null) {
                cloudinaryService.deleteImage(publicId);
            }
        }

        courseOffRepository.delete(existingCourseOff);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<CourseOffResponseDTO> searchAndFilterCoursesOff(CourseOffSearchFilterDTO filterDTO) {
        Specification<CourseOff> spec = createSpecification(filterDTO);

        Sort sort = createSort(filterDTO.getSortBy(), filterDTO.getSortDirection());
        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getSize(), sort);

        Page<CourseOff> courseOffPage = courseOffRepository.findAll(spec, pageable);

        List<CourseOffResponseDTO> courseOffDTOs = courseOffPage.getContent().stream()
                .map(this::convertToCourseOffResponseDTO)
                .collect(Collectors.toList());

        PaginationDTO paginationDTO = new PaginationDTO(
                courseOffPage.getNumber(),
                courseOffPage.getSize(),
                courseOffPage.getTotalPages(),
                courseOffPage.getTotalElements()
        );

        return new PaginationResponse<>(courseOffDTOs, paginationDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<CourseOffResponseDTO> getAllCoursesOff(int page, int size, String sortBy, String sortDirection) {
        Sort sort = createSort(sortBy, sortDirection);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CourseOff> courseOffPage = courseOffRepository.findAll(pageable);

        List<CourseOffResponseDTO> courseOffDTOs = courseOffPage.getContent().stream()
                .map(this::convertToCourseOffResponseDTO)
                .collect(Collectors.toList());

        PaginationDTO paginationDTO = new PaginationDTO(
                courseOffPage.getNumber(),
                courseOffPage.getSize(),
                courseOffPage.getTotalPages(),
                courseOffPage.getTotalElements()
        );

        return new PaginationResponse<>(courseOffDTOs, paginationDTO);
    }

    private Specification<CourseOff> createSpecification(CourseOffSearchFilterDTO filterDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by name
            if (filterDTO.getName() != null && !filterDTO.getName().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + filterDTO.getName().toLowerCase() + "%"
                ));
            }

            // Filter by target audience
            if (filterDTO.getTargetAudience() != null) {
                predicates.add(criteriaBuilder.equal(root.get("targetAudience"), filterDTO.getTargetAudience()));
            }

            // Filter by center ID
            if (filterDTO.getCenterId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("center").get("id"), filterDTO.getCenterId()));
            }

            // Filter by price range
            if (filterDTO.getPriceFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), filterDTO.getPriceFrom()));
            }
            if (filterDTO.getPriceTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), filterDTO.getPriceTo()));
            }

            // Filter by estimated hours range
            if (filterDTO.getEstimatedHoursFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("estimatedHours"), filterDTO.getEstimatedHoursFrom()));
            }
            if (filterDTO.getEstimatedHoursTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("estimatedHours"), filterDTO.getEstimatedHoursTo()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Sort createSort(String sortBy, String sortDirection) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        List<String> validSortFields = List.of("id", "name", "targetAudience", "estimatedHours",
                "price", "createdAt", "updatedAt");
        if (!validSortFields.contains(sortBy)) {
            sortBy = "createdAt";
        }

        return Sort.by(direction, sortBy);
    }

    private CourseOffResponseDTO convertToCourseOffResponseDTO(CourseOff courseOff) {
        List<SkillResponseDTO> skillDTOs = courseOff.getSkills().stream()
                .map(skill -> new SkillResponseDTO(
                        skill.getId(),
                        skill.getName(),
                        skill.getDescription(),
                        skill.getCreatedAt(),
                        0L
                ))
                .collect(Collectors.toList());

        return new CourseOffResponseDTO(
                courseOff.getId(),
                courseOff.getName(),
                courseOff.getBannerUrl(),
                courseOff.getTargetAudience(),
                courseOff.getDescription(),
                courseOff.getEstimatedHours(),
                courseOff.getPrice(),
                courseOff.getCreatedAt(),
                courseOff.getUpdatedAt(),
                courseOff.getPartner().getId(),
                courseOff.getPartner().getName(),
                skillDTOs
        );
    }
}
