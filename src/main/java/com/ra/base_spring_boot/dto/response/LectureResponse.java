package com.ra.base_spring_boot.dto.response;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureResponse {
    private Long id;
    private String fullName;
    private String imageUrl;
    private String email;
    private LocalDate dateOfBirth;
    private String lecturerCode;
    private String hometown;
    private Long departmentId;
    private Long industryId;
    private Integer workYear;
}
