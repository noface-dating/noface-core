package com.duri.duricore.onboarding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * questions.json에서 로드되는 선택지 모델
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Choice {
    private String choiceId;
    private String imageUrl;
}

