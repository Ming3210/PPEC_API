package com.ra.base_spring_boot.dto.request;

import com.ra.base_spring_boot.model.Skill;
import com.ra.base_spring_boot.model.constants.TargetAudience;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseOffRequest {
    @NotBlank(message = "Tên khóa học không được để trống!")
    private String name;

    @NotBlank(message = "Vui lòng nhập URL!")
    private String bannerUrl;

    private TargetAudience targetAudience;

    private String description;

    @NotNull(message = "Vui lòng nhập thời lượng!")
    @Min(value = 0, message = "Thời lượng phải lớn hơn 0!")
    private Integer estimatedHours;

    @NotNull(message = "Vui lòng nhập id trung tâm!")
    private Long centerId;

    @NotNull(message = "Vui lòng nhập giá!")
    @Min(value = 0, message = "Giá phải lớn hơn 0!")
    private BigDecimal price;

    private List<Long> skillsId;
}
