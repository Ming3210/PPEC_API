package com.ra.base_spring_boot.dto.request;


import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequestDTO {
    private Long courseId;
    private BigDecimal amount;
    private String currency;
    private String description;
}
