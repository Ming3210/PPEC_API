package com.ra.base_spring_boot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse<T> {
    private Boolean status;
    private String message;
    private T data;
    private HttpStatus httpStatus;
    private String timestamp;
}
