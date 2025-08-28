package com.ra.base_spring_boot.dto.request;

import com.ra.base_spring_boot.model.constants.AccountStatus;
import com.ra.base_spring_boot.model.constants.PartnerStatus;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
@Builder
public class PartnerDTO {

    private String partnerCode = null;

    @NotBlank(message = "Tên đối tác là bắt buộc")
    @Size(max = 255, message = "Tên đối tác không được vượt quá 255 ký tự")
    private String name;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    private String description;

    @PositiveOrZero(message = "Số lượng nhân viên phải là số không âm")
    private Integer numberOfEmployees;

    @PositiveOrZero(message = "Số lượng khóa học phải là số không âm")
    private Integer numberOfCourses;

    @NotBlank(message = "Địa chỉ là bắt buộc")
    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    private String address;

    private MultipartFile avatarUrl;
    private String avatar;
    private PartnerStatus status = PartnerStatus.ACTIVE;

    private Set<Long> industryIds;
    @Size(max = 100, message = "Danh sách giảng viên không được vượt quá 100 người")
    private Set<@NotNull(message = "ID giảng viên không được để trống") Long> lecturerIds;

    @Size(max = 100, message = "Danh sách trợ giảng không được vượt quá 100 người")
    private Set<@NotNull(message = "ID trợ giảng không được để trống") Long> teachingAssistantIds;

    @Size(max = 100, message = "Danh sách nhân viên dịch vụ không được vượt quá 100 người")
    private Set<@NotNull(message = "ID nhân viên dịch vụ không được để trống") Long> serviceStaffIds;
}
