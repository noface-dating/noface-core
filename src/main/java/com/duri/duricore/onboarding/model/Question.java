package com.duri.duricore.onboarding.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * questions.json에서 로드되는 질문 모델
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private String gender;
    private String questionId;
    private String questionText;
    private Integer timeLimit;
    private List<Choice> choices;
}

