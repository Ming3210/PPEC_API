package com.ra.base_spring_boot.dto.response;

import com.ra.base_spring_boot.model.Course;
import com.ra.base_spring_boot.model.Partner;
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
    private Partner partner;
    private List<Course> courses;
}
