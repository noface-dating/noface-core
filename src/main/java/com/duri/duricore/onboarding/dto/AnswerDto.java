package com.duri.duricore.onboarding.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 답변 데이터 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {
    private String questionId;
    private String choiceId;
    private Boolean isTimeout;
    private LocalDateTime answeredAt;
}

