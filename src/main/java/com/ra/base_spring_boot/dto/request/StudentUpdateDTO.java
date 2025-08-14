package com.ra.base_spring_boot.dto.request;

import com.ra.base_spring_boot.model.constants.Gender;
import com.ra.base_spring_boot.model.constants.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentUpdateDTO {

    @NotBlank
    private String fullName;

    @Past(message = "Ngày sinh phải ở quá khứ")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{9,15}$",
            message = "Số điện thoại không hợp lệ")
    private String phoneNumber;

    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank
    private String address;

    private RoleName role;

    private Long departmentId;

    private Long industryId;

    private MultipartFile avatar;
}
