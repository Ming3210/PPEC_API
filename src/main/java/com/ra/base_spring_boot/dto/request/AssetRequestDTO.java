package com.ra.base_spring_boot.dto.request;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetRequestDTO {

    @NotBlank(message = "Mã tài sản không được để trống")
    @Size(max = 50, message = "Mã tài sản tối đa 50 ký tự")
    private String code;

    @NotBlank(message = "Tên tài sản không được để trống")
    @Size(max = 200, message = "Tên tài sản tối đa 200 ký tự")
    private String name;

    @Size(max = 1000, message = "Ghi chú không được vượt quá 1000 ký tự")
    private String notes;

    @NotNull(message = "Người phụ trách không được để trống")
    private Long assignedUserId;
}
