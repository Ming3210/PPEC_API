package com.ra.base_spring_boot.dto.response;

import com.ra.base_spring_boot.model.constants.CertificateStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateResponseDTO {
    private Long id;
    private String code;
    private String name;
    private LocalDateTime examDate;
    private String description;
    private CertificateStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String creatorName;
    private String updaterName;
    private Long totalUserCertificates;
}
