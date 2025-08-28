package com.ra.base_spring_boot.advice;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.ra.base_spring_boot.dto.ResponseWrapper;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.exception.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalHandleException {

    private <T> ResponseEntity<APIResponse<T>> buildErrorResponse(String message, T data, HttpStatus status) {
        APIResponse<T> response = APIResponse.<T>builder()
                .status(false)
                .message(message)
                .data(data)
                .httpStatus(status)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Map<String, String>>> handleValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return buildErrorResponse("Dữ liệu không hợp lệ", errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<APIResponse<String>> handleBadCredentials(BadCredentialsException ex) {
        return buildErrorResponse("Lỗi xác thực", "Tên đăng nhập hoặc mật khẩu không đúng", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<APIResponse<String>> handleAuthenticationException(AuthenticationException ex) {
        return buildErrorResponse("Lỗi xác thực", ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<APIResponse<String>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        return buildErrorResponse("Kích thước tệp vượt quá giới hạn", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<APIResponse<String>> handleBusinessException(BusinessException ex) {
        return buildErrorResponse("Lỗi nghiệp vụ", ex.getMessage(), HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<?> handleHttpBadRequest(HttpBadRequest ex)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseWrapper.builder()
                        .data(ex.getMessage())
                        .code(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST)
                        .build()
        );
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
        return buildErrorResponse(ex.getMessage(), null, HttpStatus.BAD_REQUEST);
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
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<APIResponse<String>> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        return buildErrorResponse("Truy cập bị từ chối", ex.getMessage(), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<APIResponse<String>> handleNoSuchElementException(NoSuchElementException ex) {
        return buildErrorResponse("Lỗi", ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<APIResponse<String>> handleUnknownField(UnrecognizedPropertyException ex) {
        APIResponse<String> response = APIResponse.<String>builder()
                .status(false)
                .message("Trường '" + ex.getPropertyName() + "' không được phép thay đổi")
                .data(null)
                .httpStatus(HttpStatus.FORBIDDEN)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<APIResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String errorMessage = "Dữ liệu đã tồn tại";

        Throwable rootCause = ex.getRootCause();
        if (rootCause != null && rootCause.getMessage() != null) {
            String causeMsg = rootCause.getMessage();

            if (causeMsg.contains("Duplicate entry")) {
                int start = causeMsg.indexOf("Duplicate entry") + 16;
                int end = causeMsg.indexOf("for key");
                if (start > 0 && end > start) {
                    String duplicateValue = causeMsg.substring(start, end).trim().replace("'", "");
                    errorMessage = "Giá trị '" + duplicateValue + "' đã tồn tại";
                }
            }
        }

        APIResponse<String> response = APIResponse.<String>builder()
                .status(false)
                .message(errorMessage)
                .data(null)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({ BindException.class, MethodArgumentTypeMismatchException.class })
    public ResponseEntity<APIResponse<Map<String, String>>> handleBindErrors(Exception ex) {
        Map<String, String> errors = new HashMap<>();

        if (ex instanceof BindException bindEx) {
            bindEx.getFieldErrors().forEach(err -> {
                String field = err.getField();
                Object rejectedValue = err.getRejectedValue();
                errors.put(field, "Giá trị '" + rejectedValue + "' không hợp lệ cho trường " + field);
            });
        } else if (ex instanceof MethodArgumentTypeMismatchException typeEx) {
            errors.put(typeEx.getName(),
                    "Sai kiểu dữ liệu, yêu cầu: " + (typeEx.getRequiredType() != null ? typeEx.getRequiredType().getSimpleName() : "khác"));
        }

        return buildErrorResponse("Không thể chuyển đổi dữ liệu", errors, HttpStatus.BAD_REQUEST);
    }

}
