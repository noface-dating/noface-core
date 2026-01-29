package com.duri.duricore.terms.exception;

import org.springframework.http.HttpStatus;

public enum TermsErrorCode {
    REQUIRED_TERMS_MISSING(HttpStatus.BAD_REQUEST, "필수 약관이 누락되었습니다."),
    TERM_NOT_ACTIVE(HttpStatus.BAD_REQUEST, "비활성화된 약관입니다."),
    TERM_VERSION_MISMATCH(HttpStatus.BAD_REQUEST, "약관 버전이 일치하지 않습니다."),
    CONSENT_EMPTY(HttpStatus.BAD_REQUEST, "동의 항목이 비어 있습니다.");

    private final HttpStatus status;
    private final String message;

    TermsErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus status() {
        return status;
    }

    public String message() {
        return message;
    }
}
