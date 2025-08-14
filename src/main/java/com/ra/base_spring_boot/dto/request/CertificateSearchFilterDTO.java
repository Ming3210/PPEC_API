package com.ra.base_spring_boot.dto.request;

import com.ra.base_spring_boot.model.constants.CertificateStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateSearchFilterDTO {
    private String code;
    private String name;
    private CertificateStatus status;
    private LocalDateTime examDateFrom;
    private LocalDateTime examDateTo;
    private String sortBy = "createdAt";
    private String sortDirection = "desc";
    private int page = 0;
    private int size = 10;
}
