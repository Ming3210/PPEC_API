package com.ra.base_spring_boot.service.impl;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ra.base_spring_boot.dto.request.CourseRequestDTO;
import com.ra.base_spring_boot.dto.response.CourseResponseDTO;
import com.ra.base_spring_boot.exception.NotFoundException;
import com.ra.base_spring_boot.exception.ConflictException;
import com.ra.base_spring_boot.model.Course;
import com.ra.base_spring_boot.model.Center;
import com.ra.base_spring_boot.model.Partner;
import com.ra.base_spring_boot.repository.CourseRepository;
import com.ra.base_spring_boot.repository.PartnerRepository;
import com.ra.base_spring_boot.service.interfaces.ICourseService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements ICourseService {

    private final CourseRepository courseRepository;
    private final PartnerRepository partnerRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public CourseResponseDTO createCourse(CourseRequestDTO courseRequestDTO) {
        if (courseRepository.existsByCode(courseRequestDTO.getCode())) {
            throw new ConflictException("Mã khóa học đã tồn tại: " + courseRequestDTO.getCode());
        }

        Partner partner = partnerRepository.findById((courseRequestDTO.getPartnerId()))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy trung tâm với ID: " + courseRequestDTO.getPartnerId()));

        // Upload image if provided
        String imageUrl = null;
        try {
            MultipartFile imageFile = courseRequestDTO.getImageFile();
            if (imageFile != null && !imageFile.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
                imageUrl = uploadResult.get("url").toString();
            }
        } catch (IOException e) {
            throw new RuntimeException("Đăng ảnh lên không thành công", e);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi không xác định", e);
        }

        Course course = new Course();
        course.setCode(courseRequestDTO.getCode());
        course.setTitle(courseRequestDTO.getTitle());
        course.setSubtitle(courseRequestDTO.getSubtitle());
        course.setDescription(courseRequestDTO.getDescription());
        course.setProvider(courseRequestDTO.getProvider());
        course.setLevel(courseRequestDTO.getLevel());
        course.setPrice(courseRequestDTO.getPrice());
        course.setOriginalPrice(courseRequestDTO.getOriginalPrice());
        course.setStudentsCount(courseRequestDTO.getStudentsCount());
        course.setDuration(courseRequestDTO.getDuration());
        course.setLessonCount(courseRequestDTO.getLessonCount());
        course.setImageUrl(imageUrl);
        course.setIsActive(courseRequestDTO.getIsActive());
        course.setPartner(partner);
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());

        Course savedCourse = courseRepository.save(course);
        return convertToCourseResponseDTO(savedCourse);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khóa học với ID: " + id));
        return convertToCourseResponseDTO(course);
    }

    @Override
    public CourseResponseDTO updateCourse(Long id, CourseRequestDTO courseRequestDTO) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khóa học với ID: " + id));

        // Check unique code (exclude current)
        if (courseRepository.existsByCodeAndIdNot(courseRequestDTO.getCode(), id)) {
            throw new ConflictException("Mã khóa học đã tồn tại: " + courseRequestDTO.getCode());
        }

        Partner partner = partnerRepository.findById((courseRequestDTO.getPartnerId()))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy trung tâm với ID: " + courseRequestDTO.getPartnerId()));

        // Upload new image if provided
        String imageUrl = existingCourse.getImageUrl(); // Keep existing if no new image
        try {
            MultipartFile imageFile = courseRequestDTO.getImageFile();
            if (imageFile != null && !imageFile.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
                imageUrl = uploadResult.get("url").toString();
            }
        } catch (IOException e) {
            throw new RuntimeException("Đăng ảnh lên không thành công", e);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi không xác định", e);
        }

        existingCourse.setCode(courseRequestDTO.getCode());
        existingCourse.setTitle(courseRequestDTO.getTitle());
        existingCourse.setSubtitle(courseRequestDTO.getSubtitle());
        existingCourse.setDescription(courseRequestDTO.getDescription());
        existingCourse.setProvider(courseRequestDTO.getProvider());
        existingCourse.setLevel(courseRequestDTO.getLevel());
        existingCourse.setPrice(courseRequestDTO.getPrice());
        existingCourse.setOriginalPrice(courseRequestDTO.getOriginalPrice());
        existingCourse.setStudentsCount(courseRequestDTO.getStudentsCount());
        existingCourse.setDuration(courseRequestDTO.getDuration());
        existingCourse.setLessonCount(courseRequestDTO.getLessonCount());
        existingCourse.setImageUrl(imageUrl);
        existingCourse.setIsActive(courseRequestDTO.getIsActive());
        existingCourse.setPartner(partner);
        existingCourse.setUpdatedAt(LocalDateTime.now());

        Course updatedCourse = courseRepository.save(existingCourse);
        return convertToCourseResponseDTO(updatedCourse);
    }

    @Override
    public void deleteCourse(Long id) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khóa học với ID: " + id));

        courseRepository.delete(existingCourse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToCourseResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseResponseDTO> searchCourses(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Course> coursePage;

        if (keyword == null || keyword.isEmpty()) {
            coursePage = courseRepository.findAll(pageable);
        } else {
            coursePage = courseRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        }

        if (coursePage.isEmpty()) {
            throw new NotFoundException("Không tìm thấy khóa học nào");
        }

        return coursePage.map(this::convertToCourseResponseDTO);
    }

    private CourseResponseDTO convertToCourseResponseDTO(Course course) {
        return new CourseResponseDTO(
                course.getId(),
                course.getCode(),
                course.getTitle(),
                course.getSubtitle(),
                course.getDescription(),
                course.getProvider(),
                course.getLevel(),
                course.getPrice(),
                course.getOriginalPrice(),
                course.getRating(),
                course.getRatingCount(),
                course.getStudentsCount(),
                course.getDuration(),
                course.getLessonCount(),
                course.getImageUrl(),
                course.getIsActive(),
                course.getCreatedAt(),
                course.getUpdatedAt(),
                course.getPartner().getId(),
                course.getPartner().getName()
        );
    }
}
