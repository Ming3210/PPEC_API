package com.ra.base_spring_boot.service.impl;


import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.exception.HttpConflict;
import com.ra.base_spring_boot.exception.HttpBadRequest;
import com.ra.base_spring_boot.model.Skill;
import com.ra.base_spring_boot.dto.request.SkillRequestDTO;
import com.ra.base_spring_boot.dto.request.SkillSearchFilterDTO;
import com.ra.base_spring_boot.dto.request.PaginationDTO;
import com.ra.base_spring_boot.dto.response.SkillResponseDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.repository.SkillRepository;
import com.ra.base_spring_boot.service.interfaces.ISkillService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SkillServiceImpl implements ISkillService {

    private final SkillRepository skillRepository;

    @Override
    public SkillResponseDTO createSkill(SkillRequestDTO skillRequestDTO) {
        // Check unique name
        if (skillRepository.existsByName(skillRequestDTO.getName())) {
            throw new HttpConflict("Tên kỹ năng đã tồn tại: " + skillRequestDTO.getName());
        }

        Skill skill = new Skill(skillRequestDTO.getName(), skillRequestDTO.getDescription());

        Skill savedSkill = skillRepository.save(skill);
        return convertToSkillResponseDTO(savedSkill);
    }

    @Override
    @Transactional(readOnly = true)
    public SkillResponseDTO getSkillById(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy kỹ năng với ID: " + id));
        return convertToSkillResponseDTO(skill);
    }

    @Override
    public SkillResponseDTO updateSkill(Long id, SkillRequestDTO skillRequestDTO) {
        Skill existingSkill = skillRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy kỹ năng với ID: " + id));

        // Check unique name (exclude current)
        if (skillRepository.existsByNameAndIdNot(skillRequestDTO.getName(), id)) {
            throw new HttpConflict("Tên kỹ năng đã tồn tại: " + skillRequestDTO.getName());
        }

        existingSkill.setName(skillRequestDTO.getName());
        existingSkill.setDescription(skillRequestDTO.getDescription());

        Skill updatedSkill = skillRepository.save(existingSkill);
        return convertToSkillResponseDTO(updatedSkill);
    }

    @Override
    public void deleteSkill(Long id) {
        Skill existingSkill = skillRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Không tìm thấy kỹ năng với ID: " + id));

        // Check if skill can be deleted
        if (!canDeleteSkill(id)) {
            throw new HttpBadRequest("Không thể xóa kỹ năng này vì có khóa học đang sử dụng");
        }

        skillRepository.delete(existingSkill);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<SkillResponseDTO> searchAndFilterSkills(SkillSearchFilterDTO filterDTO) {
        Specification<Skill> spec = createSpecification(filterDTO);

        Sort sort = createSort(filterDTO.getSortBy(), filterDTO.getSortDirection());
        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getSize(), sort);

        Page<Skill> skillPage = skillRepository.findAll(spec, pageable);

        List<SkillResponseDTO> skillDTOs = skillPage.getContent().stream()
                .map(this::convertToSkillResponseDTO)
                .collect(Collectors.toList());

        PaginationDTO paginationDTO = new PaginationDTO(
                skillPage.getNumber(),
                skillPage.getSize(),
                skillPage.getTotalPages(),
                skillPage.getTotalElements()
        );

        return new PaginationResponse<>(skillDTOs, paginationDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<SkillResponseDTO> getAllSkills(int page, int size, String sortBy, String sortDirection) {
        Sort sort = createSort(sortBy, sortDirection);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Skill> skillPage = skillRepository.findAll(pageable);

        List<SkillResponseDTO> skillDTOs = skillPage.getContent().stream()
                .map(this::convertToSkillResponseDTO)
                .collect(Collectors.toList());

        PaginationDTO paginationDTO = new PaginationDTO(
                skillPage.getNumber(),
                skillPage.getSize(),
                skillPage.getTotalPages(),
                skillPage.getTotalElements()
        );

        return new PaginationResponse<>(skillDTOs, paginationDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canDeleteSkill(Long id) {
        // Check if there are any courses using this skill
        long courseCount = skillRepository.countCoursesUsingSkill(id);
        return courseCount == 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillResponseDTO> getAllSkillsForSelect() {
        return skillRepository.findAllByOrderByNameAsc().stream()
                .map(skill -> new SkillResponseDTO(
                        skill.getId(),
                        skill.getName(),
                        skill.getDescription(),
                        skill.getCreatedAt(),
                        null
                ))
                .collect(Collectors.toList());
    }

    private Specification<Skill> createSpecification(SkillSearchFilterDTO filterDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by name
            if (filterDTO.getName() != null && !filterDTO.getName().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + filterDTO.getName().toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Sort createSort(String sortBy, String sortDirection) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        List<String> validSortFields = List.of("id", "name", "createdAt");
        if (!validSortFields.contains(sortBy)) {
            sortBy = "createdAt";
        }

        return Sort.by(direction, sortBy);
    }

    private SkillResponseDTO convertToSkillResponseDTO(Skill skill) {
        Long totalCourses = skillRepository.countCoursesUsingSkill(skill.getId());

        return new SkillResponseDTO(
                skill.getId(),
                skill.getName(),
                skill.getDescription(),
                skill.getCreatedAt(),
                totalCourses
        );
    }
}
