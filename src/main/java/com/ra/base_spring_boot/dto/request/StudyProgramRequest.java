package com.ra.base_spring_boot.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StudyProgramRequest {
    @NotBlank(message = "Tên đầu mục không được để trống!")
    private String headerName;

    private List<String> descriptions;
}
