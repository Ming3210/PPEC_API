package com.ra.base_spring_boot.dto.request;

import com.ra.base_spring_boot.model.constants.CertificateStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateRequestDTO {

    @NotBlank(message = "Mã chứng chỉ không được để trống")
    @Size(max = 50, message = "Mã chứng chỉ không được vượt quá 50 ký tự")
    private String code;

    @NotBlank(message = "Tên chứng chỉ không được để trống")
    @Size(max = 200, message = "Tên chứng chỉ không được vượt quá 200 ký tự")
    private String name;

    private LocalDateTime examDate;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    private String description;

    @NotNull(message = "Trạng thái không được để trống")
    private CertificateStatus status;
}
