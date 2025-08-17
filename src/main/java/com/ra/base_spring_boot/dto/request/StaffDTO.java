package com.ra.base_spring_boot.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffDTO {
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

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{9,15}$", message = "Số điện thoại không hợp lệ")
    private String phoneNumber;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate dateOfBirth;
    @NotNull(message = "Quê quán không được để trống")
    private String hometown;
    private MultipartFile avatarUrl;
    private String avatar;
    @NotNull(message = "Năm bắt đầu làm việc không được để trống")
    private Integer startYear;

    @NotBlank(message = "Vị trí không được để trống")
    private String position;

    // Thông tin School
    @NotNull(message = "Trường không được để trống")
    private Long schoolId; // dùng ID để map vào entity School
}

