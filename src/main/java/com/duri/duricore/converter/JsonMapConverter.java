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
 * 일반 JSON 컬럼을 Map<String, Object> 형태로 변환하는 Converter
 * hobbies, additional_information 등에 사용
 */
@Converter
public class JsonMapConverter implements AttributeConverter<Map<String, Object>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
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
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty() || dbData.equals("{}")) {
            return new HashMap<>();
        }
        try {
            TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {};
            return objectMapper.readValue(dbData, typeRef);
        } catch (Exception e) {
            throw new OnboardingException(OnboardingErrorCode.JSON_CONVERSION_ERROR);
        }
    }
}

