package com.duri.duricore.onboarding.exception;

import com.duri.duricore.common.exception.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OnboardingErrorCode implements BaseErrorCode {

    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "ONBOARDING-404-1", "프로필을 찾을 수 없습니다. 이전 회원가입 단계를 완료해주세요."),
    INVALID_QUESTION_ID(HttpStatus.BAD_REQUEST, "ONBOARDING-400-1", "유효하지 않은 질문 ID입니다."),
    INVALID_CHOICE_ID(HttpStatus.BAD_REQUEST, "ONBOARDING-400-2", "유효하지 않은 선택지 ID입니다."),
    DUPLICATE_SUBMISSION(HttpStatus.CONFLICT, "ONBOARDING-409-1", "이미 취향 정보가 제출되었습니다. 중복 제출은 불가능합니다."),
    JSON_CONVERSION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ONBOARDING-500-1", "JSON 변환 중 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}

