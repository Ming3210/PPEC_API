package com.ra.base_spring_boot.dto.response;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponse{
    private Long id;
    private String questionText;
    private String type;
    private List<String> options;
    private String correctAnswer;
    private String explanation;
    private Integer orderNumber;
}
