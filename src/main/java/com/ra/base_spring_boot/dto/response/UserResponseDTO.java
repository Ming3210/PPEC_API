package com.ra.base_spring_boot.dto.response;

import com.ra.base_spring_boot.model.constants.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private String username;
    private String fullName;
    private Long id;
    private String email;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String address;
    private RoleName role;
}
