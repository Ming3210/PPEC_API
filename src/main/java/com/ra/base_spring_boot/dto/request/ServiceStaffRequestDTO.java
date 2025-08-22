package com.ra.base_spring_boot.dto.request;
import com.ra.base_spring_boot.model.constants.RoleName;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceStaffRequestDTO {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    private String password;

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    private String email;

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{9,15}$", message = "Số điện thoại không hợp lệ")
    private String phoneNumber;

    private LocalDate dateOfBirth;

    private String hometown;

    private MultipartFile avatar;

    @NotNull(message = "Trung tâm không được để trống")
    private Long partnerId;

    @NotBlank(message = "Chức vụ không được để trống")
    private String position;
    private RoleName role = RoleName.SERVICE_STAFF;
}

