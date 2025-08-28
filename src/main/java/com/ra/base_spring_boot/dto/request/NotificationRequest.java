package com.ra.base_spring_boot.dto.request;

import com.ra.base_spring_boot.model.constants.RoleName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class NotificationRequest {
    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    @NotBlank(message = "Nội dung không được để trống")
    private String content;

    @NotNull(message = "Chọn quyền mốn gửi thông báo không được để trống")
    private List<RoleName> roles;
}
