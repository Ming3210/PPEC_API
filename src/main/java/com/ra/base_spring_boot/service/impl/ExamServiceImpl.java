package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.ExamRequestDTO;
import com.ra.base_spring_boot.dto.response.ExamResponseDTO;
import com.ra.base_spring_boot.model.Exam;
import com.ra.base_spring_boot.model.Course;
import com.ra.base_spring_boot.model.Partner;

import com.ra.base_spring_boot.repository.ExamRepository;

import com.ra.base_spring_boot.service.interfaces.IExamService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExamServiceImpl implements IExamService {

    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;
    private final PartnerRepository partnerRepository;

    @Override
    public ExamResponseDTO createExam(ExamRequestDTO examRequestDTO) {
        // Validate unique exam code
        if (examRepository.existsByExamCode(examRequestDTO.getExamCode())) {
            throw new IllegalArgumentException("Mã bài thi đã tồn tại: " + examRequestDTO.getExamCode());
        }

        // Validate course exists
        Course course = courseRepository.findById(examRequestDTO.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found with ID: " + examRequestDTO.getCourseId()));

        // Validate partner exists
        Partner partner = partnerRepository.findById(examRequestDTO.getPartnerId())
                .orElseThrow(() -> new NotFoundException("Partner not found with ID: " + examRequestDTO.getPartnerId()));

        // Validate exam date is not in the past
        if (examRequestDTO.getExamDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày thi không thể trong quá khứ");
        }

        Exam exam = Exam.builder()
                .examCode(examRequestDTO.getExamCode())
                .title(examRequestDTO.getTitle())
                .examDate(examRequestDTO.getExamDate())
                .status(examRequestDTO.getStatus())
                .course(course)
                .partner(partner)
                .build();

        Exam savedExam = examRepository.save(exam);
        return convertToResponseDTO(savedExam);
    }

    @Override
    @Transactional(readOnly = true)
    public ExamResponseDTO getExamById(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Exam not found with ID: " + id));
        return convertToResponseDTO(exam);
    }

    @Override
    public ExamResponseDTO updateExam(Long id, ExamRequestDTO examRequestDTO) {
        Exam existingExam = examRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Exam not found with ID: " + id));

        // Validate unique exam code (exclude current exam)
        if (examRepository.existsByExamCodeAndExamIdNot(examRequestDTO.getExamCode(), id)) {
            throw new IllegalArgumentException("Mã bài thi đã tồn tại: " + examRequestDTO.getExamCode());
        }

        Course course = courseRepository.findById(examRequestDTO.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found with ID: " + examRequestDTO.getCourseId()));

        Partner partner = partnerRepository.findById(examRequestDTO.getPartnerId())
                .orElseThrow(() -> new NotFoundException("Partner not found with ID: " + examRequestDTO.getPartnerId()));

        existingExam.setExamCode(examRequestDTO.getExamCode());
        existingExam.setTitle(examRequestDTO.getTitle());
        existingExam.setExamDate(examRequestDTO.getExamDate());
        existingExam.setStatus(examRequestDTO.getStatus());
        existingExam.setCourse(course);
        existingExam.setPartner(partner);

        Exam updatedExam = examRepository.save(existingExam);
        return convertToResponseDTO(updatedExam);
    }

    @Override
    public void deleteExam(Long id) {
        if (!examRepository.existsById(id)) {
            throw new NotFoundException("Exam not found with ID: " + id);
        }
        examRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<ExamResponseDTO> searchAndFilterExams(ExamSearchFilterDTO filterDTO) {
        Specification<Exam> spec = createSpecification(filterDTO);

        Sort sort = createSort(filterDTO.getSortBy(), filterDTO.getSortDirection());
        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getSize(), sort);

        Page<Exam> examPage = examRepository.findAll(spec, pageable);

        List<ExamResponseDTO> examDTOs = examPage.getContent().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        PaginationDTO paginationDTO = new PaginationDTO(
                examPage.getNumber(),
                examPage.getSize(),
                examPage.getTotalPages(),
                examPage.getTotalElements()
        );

        return new PaginationResponse<>(examDTOs, paginationDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<ExamResponseDTO> getAllExams(int page, int size, String sortBy, String sortDirection) {
        Sort sort = createSort(sortBy, sortDirection);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Exam> examPage = examRepository.findAll(pageable);

        List<ExamResponseDTO> examDTOs = examPage.getContent().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        PaginationDTO paginationDTO = new PaginationDTO(
                examPage.getNumber(),
                examPage.getSize(),
                examPage.getTotalPages(),
                examPage.getTotalElements()
        );

        return new PaginationResponse<>(examDTOs, paginationDTO);
    }

    private Specification<Exam> createSpecification(ExamSearchFilterDTO filterDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by exam code
            if (filterDTO.getExamCode() != null && !filterDTO.getExamCode().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("examCode")),
                        "%" + filterDTO.getExamCode().toLowerCase() + "%"
                ));
            }

            // Filter by title
            if (filterDTO.getTitle() != null && !filterDTO.getTitle().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + filterDTO.getTitle().toLowerCase() + "%"
                ));
            }

            // Filter by status
            if (filterDTO.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filterDTO.getStatus()));
            }

            // Filter by course ID
            if (filterDTO.getCourseId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("course").get("id"), filterDTO.getCourseId()));
            }

            // Filter by partner ID
            if (filterDTO.getPartnerId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("partner").get("id"), filterDTO.getPartnerId()));
            }

            // Filter by exam date range
            if (filterDTO.getExamDateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("examDate"), filterDTO.getExamDateFrom()));
            }
            if (filterDTO.getExamDateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("examDate"), filterDTO.getExamDateTo()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Sort createSort(String sortBy, String sortDirection) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        List<String> validSortFields = List.of("examId", "examCode", "title", "examDate", "status");
        if (!validSortFields.contains(sortBy)) {
            sortBy = "examDate";
        }

        return Sort.by(direction, sortBy);
    }

    private ExamResponseDTO convertToResponseDTO(Exam exam) {
        return new ExamResponseDTO(
                exam.getExamId(),
                exam.getExamCode(),
                exam.getTitle(),
                exam.getExamDate(),
                exam.getStatus(),
                exam.getCourse().getId(),
                exam.getCourse().getTitle(),
                exam.getPartner().getId(),
                exam.getPartner().getName()
        );
    }
}
