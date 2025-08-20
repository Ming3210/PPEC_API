package com.ra.base_spring_boot.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponseDTO {
    private Long id;
    private String questionText;
    private String type;
    private List<String> options;
    private Integer orderNumber;
}

