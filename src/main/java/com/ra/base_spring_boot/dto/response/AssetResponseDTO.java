package com.ra.base_spring_boot.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetResponseDTO {
    private Long id;
    private String code;
    private String name;
    private String notes;
    private Long assignedUserId;
    private String assignedUserName;
    private String assignedUserEmail;
    private String assignedUserPhone;
}
