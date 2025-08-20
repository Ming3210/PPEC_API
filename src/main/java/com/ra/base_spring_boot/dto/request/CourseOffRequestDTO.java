package com.ra.base_spring_boot.dto.request;


import com.ra.base_spring_boot.model.constants.TargetAudience;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseOffRequestDTO {

    @NotBlank(message = "Tên khóa học không được để trống")
    @Size(max = 255, message = "Tên khóa học không được vượt quá 255 ký tự")
    private String name;

    @Size(max = 2000, message = "Mô tả không được vượt quá 2000 ký tự")
    private String description;

    @NotNull(message = "Đối tượng mục tiêu không được để trống")
    private TargetAudience targetAudience;

    @NotNull(message = "Số giờ học dự kiến không được để trống")
    @Min(value = 1, message = "Số giờ học phải ít nhất 1 giờ")
    @Max(value = 1000, message = "Số giờ học không được vượt quá 1000 giờ")
    private Integer estimatedHours;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", message = "Giá phải lớn hơn hoặc bằng 0")
    private BigDecimal price;

    private List<Long> skillIds;

    private Long partnerId;

    private MultipartFile bannerFile;
}

