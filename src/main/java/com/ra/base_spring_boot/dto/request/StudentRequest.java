package com.ra.base_spring_boot.dto.request;

import com.ra.base_spring_boot.model.constants.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequest {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(max = 50, message = "Tên đăng nhập tối đa 50 ký tự")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "Mật khẩu tối thiểu 8 ký tự")
    private String password;

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(max = 255, message = "Họ và tên tối đa 255 ký tự")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @Pattern(
            regexp = "^(\\+\\d{1,3}[- ]?)?\\d{9,15}$",
            message = "Số điện thoại không hợp lệ"
    )
    private String phoneNumber;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 255, message = "Địa chỉ tối đa 255 ký tự")
    private String address;

    @Schema(type = "string", format = "binary", description = "Ảnh đại diện (bắt buộc)")
    private MultipartFile avatar;

    @NotBlank(message = "Mã sinh viên không được để trống")
    @Size(max = 50, message = "Mã sinh viên tối đa 50 ký tự")
    private String studentCode;

    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    @NotNull(message = "Ngày sinh không được sé trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotNull(message = "Giới tính không được để trống")
    private Gender gender;

    @NotNull(message = "Khoa không được để trống")
    private Long departmentId;

    @Pattern(
            regexp = "^[0-9]{4}-[0-9]{4}$",
            message = "Năm học phải công khải")
    private String academicYear;

    @NotNull(message = "Ngành học không được để trống")
    private Long industryId;
}
