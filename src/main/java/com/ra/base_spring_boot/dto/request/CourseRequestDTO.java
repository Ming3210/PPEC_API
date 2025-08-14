package com.ra.base_spring_boot.dto.request;


import com.ra.base_spring_boot.model.constants.Level;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequestDTO {

    @NotBlank(message = "Mã khóa học không được để trống")
    @Size(max = 50, message = "Mã khóa học không được vượt quá 50 ký tự")
    private String code;

    @NotBlank(message = "Tiêu đề khóa học không được để trống")
    @Size(max = 255, message = "Tiêu đề không được vượt quá 255 ký tự")
    private String title;

    @Size(max = 500, message = "Phụ đề không được vượt quá 500 ký tự")
    private String subtitle;

    @Size(max = 2000, message = "Mô tả không được vượt quá 2000 ký tự")
    private String description;

    @Size(max = 100, message = "Nhà cung cấp không được vượt quá 100 ký tự")
    private String provider;

    @NotNull(message = "Cấp độ không được để trống")
    private Level level;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", message = "Giá phải lớn hơn hoặc bằng 0")
    private BigDecimal price;

    @DecimalMin(value = "0.0", message = "Giá gốc phải lớn hơn hoặc bằng 0")
    private BigDecimal originalPrice;

    @Min(value = 0, message = "Số học viên phải lớn hơn hoặc bằng 0")
    private Integer studentsCount;

    @Size(max = 50, message = "Thời lượng không được vượt quá 50 ký tự")
    private String duration;

    @Min(value = 0, message = "Số bài học phải lớn hơn hoặc bằng 0")
    private Integer lessonCount;

    private Boolean isActive = true;

    @NotNull(message = "Center ID không được để trống")
    private Long centerId;

    private MultipartFile imageFile;
}
