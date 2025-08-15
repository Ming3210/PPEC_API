package com.ra.base_spring_boot.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillRequestDTO {


    @NotBlank(message = "Tên kỹ năng không được để trống")
    @Size(max = 255, message = "Tên kỹ năng không được vượt quá 255 ký tự")
    private String name;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    private String description;
}
