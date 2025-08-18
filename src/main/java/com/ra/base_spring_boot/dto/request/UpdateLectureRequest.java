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
public class UpdateLectureRequest {
    private MultipartFile image;

    @NotBlank(message = "Mã giảng viên không được để trống")
    @Size(max = 50, message = "Mã giảng viên không vượt quá 50 ký tự")
    private String lecturerCode;

    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    private LocalDate dateOfBirth;

    @Size(max = 255, message = "Quê quán không vượt quá 255 ký tự")
    private String hometown;

    @NotNull(message = "Trường không được để trống")
    private Long DepartmentId;

    @NotNull(message = "Chuyên ngành không được để trống")
    private Long industryId;
    @Min(value = 1900, message = "Năm làm việc phải từ 1900 trở lên")
    @Max(value = 2100, message = "Năm làm việc không được vượt quá 2100")
    private Integer workYear;
}
