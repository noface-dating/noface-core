package com.duri.duricore.converter;

import com.duri.duricore.onboarding.exception.OnboardingException;
import com.duri.duricore.onboarding.exception.OnboardingErrorCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.HashMap;
import java.util.Map;

/**
 * face_preference JSON 컬럼을 Map<String, Map<String, Integer>> 형태로 변환하는 Converter
 *
 * DB에 저장될 때: Map → JSON 문자열
 * DB에서 조회될 때: JSON 문자열 → Map
 *
 * 예시:
 * DB: {"animalFace": {"cat": 3, "dog": 1}, "bangs": {"yes": 2, "no": 0}}
 * Java: Map<String, Map<String, Integer>>
 */
@Converter
public class FacePreferenceConverter implements AttributeConverter<Map<String, Map<String, Integer>>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Map<String, Integer>> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "{}";
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new OnboardingException(OnboardingErrorCode.JSON_CONVERSION_ERROR);
        }
    }

    @Override
    public Map<String, Map<String, Integer>> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty() || dbData.equals("{}")) {
            return new HashMap<>();
        }
        try {
            TypeReference<Map<String, Map<String, Integer>>> typeRef = new TypeReference<>() {};
            return objectMapper.readValue(dbData, typeRef);
        } catch (Exception e) {
            throw new OnboardingException(OnboardingErrorCode.JSON_CONVERSION_ERROR);
        }
    }
}


