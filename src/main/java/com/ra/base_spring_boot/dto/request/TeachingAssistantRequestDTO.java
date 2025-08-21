package com.ra.base_spring_boot.dto.request;

import com.ra.base_spring_boot.model.constants.RoleName;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeachingAssistantRequestDTO {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    private String password;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @Pattern(
            regexp = "^(\\+\\d{1,3}[- ]?)?\\d{9,15}$",
            message = "Số điện thoại không hợp lệ"
    )
    private String phoneNumber;

    private RoleName role = RoleName.ASSISTANT;

    @NotBlank(message = "Mã trợ giảng không được để trống")
    private String taCode;
    private MultipartFile avatarUrl;
    @NotNull(message = "Giảng viên phụ trách không được để trống")
    @Min(value = 1, message = "ID giảng viên phụ trách phải lớn hơn 0")
    private Long assignedLecturerId;
    @NotNull(message = "Khoa không được để trống")
    @Min(value = 1, message = "ID khoa phải lớn hơn 0")
    private Long departmentId;

}

