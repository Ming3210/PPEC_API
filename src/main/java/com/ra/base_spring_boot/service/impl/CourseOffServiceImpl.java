package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.CourseOffRequest;
import com.ra.base_spring_boot.dto.response.CourseOffDTO;
import com.ra.base_spring_boot.exception.HttpBadRequest;
import com.ra.base_spring_boot.model.Center;
import com.ra.base_spring_boot.model.CourseOff;
import com.ra.base_spring_boot.model.Skill;
import com.ra.base_spring_boot.repository.CenterRepository;
import com.ra.base_spring_boot.repository.CourseOffRepository;
import com.ra.base_spring_boot.repository.SkillRepository;
import com.ra.base_spring_boot.service.interfaces.CourseOffService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseOffServiceImpl implements CourseOffService {
    @Autowired
    private CourseOffRepository courseOffRepository;

    @Autowired
    private CenterRepository centerRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Override
    public Page<CourseOffDTO> getAllCourseOffs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CourseOff> courseOffDTOPage = courseOffRepository.findAll(pageable);
        return courseOffDTOPage.map(this::convertToDTO);
    }

    @Override
    @Transactional
    public CourseOffDTO addCourseOff(CourseOffRequest courseOffRequest) {
        if (courseOffRepository.existsByName(courseOffRequest.getName())) {
            throw new HttpBadRequest("Tên khóa học đã tồn tại!");
        }

        Center center = centerRepository.findById(courseOffRequest.getCenterId())
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy trung tâm!"));

        Set<Skill> skills = new HashSet<>(skillRepository.findAllById(courseOffRequest.getSkillsId()));

        if (skills.isEmpty()) {
            throw new NoSuchElementException("Không tìm thấy kỹ năng nào!");
        }

        CourseOff courseOff = CourseOff.builder()
                .name(courseOffRequest.getName())
                .bannerUrl(courseOffRequest.getBannerUrl())
                .targetAudience(courseOffRequest.getTargetAudience())
                .description(courseOffRequest.getDescription())
                .estimatedHours(courseOffRequest.getEstimatedHours())
                .center(center)
                .price(courseOffRequest.getPrice())
                .skills(skills)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        CourseOff courseOff1 = courseOffRepository.save(courseOff);
        return convertToDTO(courseOff1);
    }

    @Override
    public CourseOff getCourseOffById(Long id) {
        return courseOffRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy khóa học!"));
    }

    @Override
    public CourseOffDTO update(Long id, CourseOffRequest courseOffRequest) {
        Center center = centerRepository.findById(courseOffRequest.getCenterId())
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy trung tâm!"));

        Set<Skill> skills = new HashSet<>(skillRepository.findAllById(courseOffRequest.getSkillsId()));

        CourseOff courseOff = getCourseOffById(id);
        courseOff.setName(courseOffRequest.getName());
        courseOff.setBannerUrl(courseOffRequest.getBannerUrl());
        courseOff.setTargetAudience(courseOffRequest.getTargetAudience());
        courseOff.setEstimatedHours(courseOffRequest.getEstimatedHours());
        courseOff.setDescription(courseOffRequest.getDescription());
        courseOff.setCenter(center);
        courseOff.setPrice(courseOffRequest.getPrice());
        courseOff.setSkills(skills);
        courseOff.setUpdatedAt(LocalDateTime.now());

        CourseOff updateCourseOff = courseOffRepository.save(courseOff);
        return convertToDTO(updateCourseOff);
    }

    @Override
    public void delete(Long id) {
        CourseOff courseOff = getCourseOffById(id);

        if (courseOff.getSkills() != null && !courseOff.getSkills().isEmpty()) {
            throw new HttpBadRequest("Khóa học này có kỹ năng liên kết, không thể xóa!");
        }

        courseOffRepository.delete(courseOff);
    }

    @Override
    public CourseOffDTO getCourseById(Long id) {
        CourseOff courseOff = getCourseOffById(id);
        return convertToDTO(courseOff);
    }

    public CourseOffDTO convertToDTO(CourseOff courseOff) {
        return CourseOffDTO.builder()
                .name(courseOff.getName())
                .bannerUrl(courseOff.getBannerUrl())
                .targetAudience(courseOff.getTargetAudience())
                .description(courseOff.getDescription())
                .estimatedHours(courseOff.getEstimatedHours())
                .price(courseOff.getPrice())
                .centerName(courseOff.getCenter().getName())
                .skills(courseOff.getSkills().stream()
                        .map(Skill::getName)
                        .collect(Collectors.toSet()))
                .build();
    }
}
