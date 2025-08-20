package com.ra.base_spring_boot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetDetailPartnerResponse {
    private int classOpened;
    private double graduationRate;
    private int totalStudent;
    private PartnerResponseDTO partner;
    private List<CourseOnlineDTO> onlineCourses;
    private List<CourseOffResponse> offlineCourses;
}

