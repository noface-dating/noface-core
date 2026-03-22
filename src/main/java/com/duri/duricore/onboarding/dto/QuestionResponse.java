package com.duri.duricore.onboarding.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 질문 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    private String questionId;
    private String questionText;
    private Integer timeLimit;
    private List<ChoiceResponse> choices;
}

