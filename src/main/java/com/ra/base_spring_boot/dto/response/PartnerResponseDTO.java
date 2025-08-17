package com.ra.base_spring_boot.dto.response;

import com.ra.base_spring_boot.model.constants.PartnerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerResponseDTO {
    private Long id;
    private String partnerCode;
    private String name;
    private String description;
    private Integer numberOfEmployees;
    private Integer numberOfCourses;
    private String address;
    private String avatarUrl;
    private PartnerStatus status;
    private Set<String> industries;
}

