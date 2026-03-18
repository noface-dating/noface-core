package com.duri.duricore.onboarding.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 얼굴 취향 결과 조회 응답 DTO
 * 배열 형태: [0,0,0,0,0,1,1,1,1,1]
 * 각 인덱스는 질문 순서, 값은 선택지 (0: 첫 번째, 1: 두 번째)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacePreferenceResultResponse {
    private List<Integer> facePreference;
}

