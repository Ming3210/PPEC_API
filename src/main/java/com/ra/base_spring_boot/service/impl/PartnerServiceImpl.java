package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.advice.PartnerAlreadyExistsException;
import com.ra.base_spring_boot.dto.request.PaginationDTO;
import com.ra.base_spring_boot.dto.request.PartnerDTO;
import com.ra.base_spring_boot.dto.response.*;
import com.ra.base_spring_boot.model.*;
import com.ra.base_spring_boot.model.constants.PartnerStatus;
import com.ra.base_spring_boot.model.constants.RoleName;
import com.ra.base_spring_boot.repository.*;
import com.ra.base_spring_boot.service.interfaces.ICloudinaryService;
import com.ra.base_spring_boot.service.interfaces.IPartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements IPartnerService {

    private final PartnerRepository partnerRepository;
    private final IndustryRepository industryRepository;
    private final ICloudinaryService cloudinaryService;
    private final EnrollmentOnlineRepository enrollmentOnlineRepository;
    private final CourseRepository courseRepository;
    private final StudentCourseOffRepository studentCourseOffRepository;
    private final LessonRepository lessonRepository;
    private final CourseOffRepository courseOffRepository;
    private final StudentProgressRepository studentProgressRepository;
    private final ServiceStaffRepository serviceStaffRepository;
    @Override
    public PartnerResponseDTO createPartner(PartnerDTO dto) {
        if (partnerRepository.existsByName(dto.getName())) {
            throw new PartnerAlreadyExistsException("Tên đối tác đã tồn tại");
        }
        Partner partner = new Partner();
        partner.setPartnerCode(generateUniquePartnerCode());
        partner.setStatus(dto.getStatus() != null ? dto.getStatus() : PartnerStatus.ACTIVE);
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
    @Transactional(readOnly = true)
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

        Optional<ServiceStaff> isCheck = serviceStaffRepository.findAll().stream()
                .filter(s -> s.getPartner().getId().equals(id))
                .findFirst();

        if (isCheck.isPresent()) {
            throw new IllegalArgumentException("Đối tác đang có nhân viên dịch vụ, không thể xóa");
        }

        if (partner.getAvatarUrl() != null) {
            String publicId = cloudinaryService.extractPublicIdFromUrl(partner.getAvatarUrl());
            cloudinaryService.deleteImage(publicId);
        }

        partnerRepository.delete(partner);
    }


    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<PartnerResponseDTO> searchPartners(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Partner> partnerPage = partnerRepository.searchWithIndustries(keyword, pageable);

        if (partnerPage.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy đối tác");
        }

        List<PartnerResponseDTO> partnerDTOs = partnerPage.getContent().stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());

        PaginationDTO paginationDTO = new PaginationDTO(
                partnerPage.getNumber(),
                partnerPage.getSize(),
                partnerPage.getTotalPages(),
                partnerPage.getTotalElements()
        );
        return new PaginationResponse<>(partnerDTOs, paginationDTO);
    }

    @Override
    public Page<PartnerResponseDTO> getPartners(String keyword, int page, int size) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public GetDetailPartnerResponse getDetailPartner(Long partnerId) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy đối tác"));

        PartnerResponseDTO partnerDTO = mapEntityToResponse(partner);

        List<Course> courses = courseRepository.findByPartnerId(partnerId);
        List<CourseOff> courseOffs = courseOffRepository.findByPartnerId(partnerId);

        List<Long> courseIds = courses.stream().map(Course::getId).toList();
        List<Long> courseOffIds = courseOffs.stream().map(CourseOff::getId).toList();

        int totalStudentOnline = courseIds.isEmpty() ? 0 : enrollmentOnlineRepository.countByCourseIdIn(courseIds);
        int totalStudentOffline = courseOffIds.isEmpty() ? 0 : studentCourseOffRepository.countByCourseIdIn(courseOffIds);
        int totalStudent = totalStudentOnline + totalStudentOffline;

        int graduatedOffline = countGraduated(courseOffIds, true);
        int graduatedOnline = countGraduated(courseIds, false);

        int totalGraduated = graduatedOnline + graduatedOffline;
        double graduationRate = (totalStudent == 0) ? 0.0 : (double) totalGraduated / totalStudent;

        List<CourseOnlineDTO> onlineCourses = courses.stream()
                .map(c -> new CourseOnlineDTO(
                        c.getId(),
                        c.getCode(),
                        c.getTitle(),
                        c.getPrice(),
                        c.getImageUrl()
                ))
                .toList();

        List<CourseOffResponse> offlineCourses = courseOffs.stream()
                .map(c -> new CourseOffResponse(
                        c.getId(),
                        c.getName(),
                        c.getBannerUrl(),
                        c.getTargetAudience(),
                        c.getDescription(),
                        c.getEstimatedHours(),
                        c.getPrice()
                ))
                .toList();

        GetDetailPartnerResponse response = new GetDetailPartnerResponse();
        response.setClassOpened(courses.size() + courseOffs.size());
        response.setGraduationRate(graduationRate);
        response.setTotalStudent(totalStudent);
        response.setPartner(partnerDTO);
        response.setOnlineCourses(onlineCourses);
        response.setOfflineCourses(offlineCourses);

        return response;
    }


    private void mapDtoToEntity(PartnerDTO dto, Partner partner) {

        partner.setName(dto.getName());
        partner.setDescription(dto.getDescription());
        partner.setNumberOfEmployees(dto.getNumberOfEmployees());
        partner.setNumberOfCourses(dto.getNumberOfCourses());
        partner.setAddress(dto.getAddress());
        if (dto.getStatus() != null) {
            partner.setStatus(dto.getStatus());
        }
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
    private int countGraduated(List<Long> courseIds, boolean isOffline) {
        int graduated = 0;

        for (Long courseId : courseIds) {
            List<Lesson> lessons = lessonRepository.findByCourseId(courseId);
            if (lessons.isEmpty()) continue;
            List<Long> studentIds = isOffline
                    ? studentCourseOffRepository.findStudentIdsByCourseId(courseId)
                    : enrollmentOnlineRepository.findStudentIdsByCourseId(courseId);
            for (Long studentId : studentIds) {
                boolean completedAll = true;
                for (Lesson lesson : lessons) {
                    List<StudentProgress> progresses =
                            studentProgressRepository.findByLessonIdAndStudentId(lesson.getId(), studentId);

                    boolean finished = progresses.stream()
                            .anyMatch(p -> Double.valueOf(1.0).equals(p.getCompletionPercentage()));

                    if (!finished) {
                        completedAll = false;
                        break;
                    }
                }

                if (completedAll) {
                    graduated++;
                }
            }
        }
        return graduated;
    }
    private String generateUniquePartnerCode() {
        String code;
        do {
            code = "PN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (partnerRepository.isCheckPartnerCode(code));
        return code;
    }
}
