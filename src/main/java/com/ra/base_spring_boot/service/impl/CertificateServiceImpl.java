package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.CertificateRequestDTO;
import com.ra.base_spring_boot.dto.request.CertificateSearchFilterDTO;
import com.ra.base_spring_boot.dto.request.PaginationDTO;
import com.ra.base_spring_boot.dto.response.CertificateResponseDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.exception.NotFoundException;
import com.ra.base_spring_boot.exception.ConflictException;
import com.ra.base_spring_boot.exception.BadRequestException;
import com.ra.base_spring_boot.model.Certificate;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.repository.CertificateRepository;
import com.ra.base_spring_boot.repository.UserCertificateRepository;
import com.ra.base_spring_boot.repository.UserRepository;
import com.ra.base_spring_boot.service.interfaces.ICertificateService;

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
public class CertificateServiceImpl implements ICertificateService {

    private final CertificateRepository certificateRepository;
    private final UserCertificateRepository userCertificateRepository;
    private final UserRepository userRepository;

    @Override
    public CertificateResponseDTO createCertificate(CertificateRequestDTO certificateRequestDTO) {
        // Validate unique code
        if (certificateRepository.existsByCode(certificateRequestDTO.getCode())) {
            throw new ConflictException("Mã chứng chỉ đã tồn tại: " + certificateRequestDTO.getCode());
        }

        Certificate certificate = new Certificate();
        certificate.setCode(certificateRequestDTO.getCode());
        certificate.setName(certificateRequestDTO.getName());
        certificate.setExamDate(certificateRequestDTO.getExamDate());
        certificate.setDescription(certificateRequestDTO.getDescription());
        certificate.setStatus(certificateRequestDTO.getStatus());
        certificate.setCreatedAt(LocalDateTime.now());
        certificate.setUpdatedAt(LocalDateTime.now());
        // TODO: createBy from security
        certificate.setCreatedBy(1L);

        Certificate savedCertificate = certificateRepository.save(certificate);
        return convertToResponseDTO(savedCertificate);
    }

    @Override
    @Transactional(readOnly = true)
    public CertificateResponseDTO getCertificateById(Long id) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy chứng chỉ với ID: " + id));
        return convertToResponseDTO(certificate);
    }

    @Override
    public CertificateResponseDTO updateCertificate(Long id, CertificateRequestDTO certificateRequestDTO) {
        Certificate existingCertificate = certificateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy chứng chỉ với ID: " + id));

        if (certificateRepository.existsByCodeAndIdNot(certificateRequestDTO.getCode(), id)) {
            throw new ConflictException("Mã chứng chỉ đã tồn tại: " + certificateRequestDTO.getCode());
        }

        existingCertificate.setCode(certificateRequestDTO.getCode());
        existingCertificate.setName(certificateRequestDTO.getName());
        existingCertificate.setExamDate(certificateRequestDTO.getExamDate());
        existingCertificate.setDescription(certificateRequestDTO.getDescription());
        existingCertificate.setStatus(certificateRequestDTO.getStatus());
        existingCertificate.setUpdatedAt(LocalDateTime.now());
        // TODO: updatedBy from security
        existingCertificate.setUpdatedBy(1L); // Temporary hardcode

        Certificate updatedCertificate = certificateRepository.save(existingCertificate);
        return convertToResponseDTO(updatedCertificate);
    }

    @Override
    public void deleteCertificate(Long id) {
        if (!certificateRepository.existsById(id)) {
            throw new NotFoundException("Không tìm thấy chứng chỉ với ID: " + id);
        }

        if (!canDeleteCertificate(id)) {
            throw new BadRequestException("Không thể xóa chứng chỉ này vì có học viên đang sử dụng");
        }

        certificateRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<CertificateResponseDTO> searchAndFilterCertificates(CertificateSearchFilterDTO filterDTO) {
        Specification<Certificate> spec = createSpecification(filterDTO);

        Sort sort = createSort(filterDTO.getSortBy(), filterDTO.getSortDirection());
        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getSize(), sort);

        Page<Certificate> certificatePage = certificateRepository.findAll(spec, pageable);

        List<CertificateResponseDTO> certificateDTOs = certificatePage.getContent().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        PaginationDTO paginationDTO = new PaginationDTO(
                certificatePage.getNumber(),
                certificatePage.getSize(),
                certificatePage.getTotalPages(),
                certificatePage.getTotalElements()
        );

        return new PaginationResponse<>(certificateDTOs, paginationDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<CertificateResponseDTO> getAllCertificates(int page, int size, String sortBy, String sortDirection) {
        Sort sort = createSort(sortBy, sortDirection);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Certificate> certificatePage = certificateRepository.findAll(pageable);

        List<CertificateResponseDTO> certificateDTOs = certificatePage.getContent().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        PaginationDTO paginationDTO = new PaginationDTO(
                certificatePage.getNumber(),
                certificatePage.getSize(),
                certificatePage.getTotalPages(),
                certificatePage.getTotalElements()
        );

        return new PaginationResponse<>(certificateDTOs, paginationDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canDeleteCertificate(Long id) {
        long userCertificateCount = userCertificateRepository.countByCertificateId(id);
        return userCertificateCount == 0;
    }

    // Private methods giữ nguyên...
    private Specification<Certificate> createSpecification(CertificateSearchFilterDTO filterDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDTO.getCode() != null && !filterDTO.getCode().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("code")),
                        "%" + filterDTO.getCode().toLowerCase() + "%"
                ));
            }

            if (filterDTO.getName() != null && !filterDTO.getName().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + filterDTO.getName().toLowerCase() + "%"
                ));
            }

            if (filterDTO.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filterDTO.getStatus()));
            }

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

        List<String> validSortFields = List.of("id", "code", "name", "examDate", "status", "createdAt", "updatedAt");
        if (!validSortFields.contains(sortBy)) {
            sortBy = "createdAt";
        }

        return Sort.by(direction, sortBy);
    }

    private CertificateResponseDTO convertToResponseDTO(Certificate certificate) {
        Long totalUserCertificates = userCertificateRepository.countByCertificateId(certificate.getId());

        String creatorName = null;
        String updaterName = null;

        if (certificate.getCreator() != null) {
            creatorName = certificate.getCreator().getFullName();
        }
        if (certificate.getUpdater() != null) {
            updaterName = certificate.getUpdater().getFullName();
        }

        return new CertificateResponseDTO(
                certificate.getId(),
                certificate.getCode(),
                certificate.getName(),
                certificate.getExamDate(),
                certificate.getDescription(),
                certificate.getStatus(),
                certificate.getCreatedAt(),
                certificate.getUpdatedAt(),
                creatorName,
                updaterName,
                totalUserCertificates
        );
    }
}
