package com.ra.base_spring_boot.advice;

import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandleException {

    private <T> ResponseEntity<APIResponse<T>> buildErrorResponse(String message, T data, HttpStatus status) {
        APIResponse<T> response = APIResponse.<T>builder()
                .status(false)
                .message(message)
                .data(data)
                .httpStatus(status)
                .timestamp(LocalDateTime.now().toString())
                .build();
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Map<String, String>>> handleValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return buildErrorResponse("Dữ liệu không hợp lệ", errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<APIResponse<String>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        return buildErrorResponse("Kích thước tệp vượt quá giới hạn", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<APIResponse<String>> handleNoResourceFoundException(NoResourceFoundException ex) {
        return buildErrorResponse("Không tìm thấy tài nguyên", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<APIResponse<String>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return buildErrorResponse("Không tìm thấy người dùng", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpBadRequest.class)
    public ResponseEntity<APIResponse<String>> handleHttpBadReqeust(HttpBadRequest ex) {
        return buildErrorResponse("Yêu cầu không hợp lệ", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpUnAuthorized.class)
    public ResponseEntity<APIResponse<String>> handleHttpUnAuthorized(HttpUnAuthorized ex) {
        return buildErrorResponse("Không được phép truy cập", ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpForbiden.class)
    public ResponseEntity<APIResponse<String>> handleHttpForbiden(HttpForbiden ex) {
        return buildErrorResponse("Truy cập bị từ chối", ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpNotFound.class)
    public ResponseEntity<APIResponse<String>> handleHttpNotFound(HttpNotFound ex) {
        return buildErrorResponse("Không tìm thấy", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PartnerAlreadyExistsException.class)
    public ResponseEntity<APIResponse<Object>> handlePartnerExists(PartnerAlreadyExistsException ex) {
        return buildErrorResponse("Đối tác đã tồn tại", null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse<Map<String, String>>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("errorDetail", ex.getMessage());
        return buildErrorResponse("Yêu cầu không hợp lệ", errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpConflict.class)
    public ResponseEntity<APIResponse<String>> handleHttpConflict(HttpConflict ex) {
        return buildErrorResponse("Xung đột dữ liệu", ex.getMessage(), HttpStatus.CONFLICT);
    }
}
