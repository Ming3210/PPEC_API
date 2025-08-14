package com.ra.base_spring_boot.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataErrorResponse<T> {
    private boolean success;            // ✅ Thành công hay không
    private String message;             // ✅ Thông báo
    private T data;                     // ✅ Kết quả trả về
    private Object errors;              // ✅ Lỗi validation (nếu có)
    private int statusCode;             // ✅ Mã HTTP
    private String status;              // ✅ Status tương ứng (OK, BAD_REQUEST...)
    private String path;                // ✅ Đường dẫn request
    private LocalDateTime timestamp;    // ✅ Thời gian phản hồi
}
