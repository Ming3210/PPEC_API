package com.ra.base_spring_boot.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewsRequest {
    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    @NotBlank(message = "Ban tóm tắt không được để trống")
    private String summary;

    @NotBlank(message = "Noội dung không được để trống")
    private String content;

    private String imageUrl;

    @NotNull(message = "ID người dùng không được để trống")
    private Long userId;
}