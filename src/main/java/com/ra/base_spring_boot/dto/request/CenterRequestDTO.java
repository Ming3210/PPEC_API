package com.ra.base_spring_boot.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CenterRequestDTO {

    @NotBlank(message = "Tên trung tâm không được để trống")
    @Size(max = 255, message = "Tên trung tâm không được vượt quá 255 ký tự")
    private String name;

    @Size(max = 500, message = "Địa chỉ không được vượt quá 500 ký tự")
    private String address;

    @NotNull(message = "ID người dùng không được để trống")
    private Long userId;

    private MultipartFile logoFile;
}
