package com.duri.duricore.converter;

import com.duri.duricore.onboarding.exception.OnboardingException;
import com.duri.duricore.onboarding.exception.OnboardingErrorCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * face_preference와 face_features CHAR(10) 컬럼을 List<Integer> 형태로 변환하는 Converter
 * 
 * DB에 저장될 때: List<Integer> → CHAR(10) 문자열 (예: "0000011111")
 * DB에서 조회될 때: CHAR(10) 문자열 → List<Integer>
 * 
 * 예시:
 * DB: "0000011111" (10자리 문자열)
 * Java: List<Integer> [0,0,0,0,0,1,1,1,1,1]
 * 
 * 각 인덱스는 질문 순서 (q1~q10)
 * 값은 선택지 (0: 첫 번째 선택지, 1: 두 번째 선택지)
 */
@Converter
public class FacePreferenceConverter implements AttributeConverter<List<Integer>, String> {

    private static final int EXPECTED_LENGTH = 10;

    @Override
    public String convertToDatabaseColumn(List<Integer> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            // 빈 리스트면 "0000000000"으로 초기화 (10자리 0으로 채움)
            return "0000000000";
        }
        
        // List<Integer>를 10자리 문자열로 변환
        // 예: [0,0,0,0,0,1,1,1,1,1] → "0000011111"
        String result = attribute.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
        
        // 길이가 10이 아니면 예외 발생
        if (result.length() != EXPECTED_LENGTH) {
            throw new OnboardingException(OnboardingErrorCode.JSON_CONVERSION_ERROR);
        }
        
        return result;
    }

    @Override
    public List<Integer> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String trimmed = dbData.trim();
        
        // 길이가 10이 아니면 예외 발생
        if (trimmed.length() != EXPECTED_LENGTH) {
            throw new OnboardingException(OnboardingErrorCode.JSON_CONVERSION_ERROR);
        }
        
        // 문자열을 List<Integer>로 변환
        // 예: "0000011111" → [0,0,0,0,0,1,1,1,1,1]
        List<Integer> result = new ArrayList<>();
        for (char c : trimmed.toCharArray()) {
            if (c != '0' && c != '1') {
                throw new OnboardingException(OnboardingErrorCode.JSON_CONVERSION_ERROR);
            }
            result.add(Character.getNumericValue(c));
        }
        
        return result;
    }
}

