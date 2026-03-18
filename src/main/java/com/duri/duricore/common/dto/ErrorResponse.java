package com.duri.duricore.common.dto;

import com.duri.duricore.common.exception.BaseErrorCode;
import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime currentTime,
        int status,
        String code,
        String message
) {
    public static ErrorResponse from(BaseErrorCode errorCode) {
        return new ErrorResponse(
                LocalDateTime.now(),
                errorCode.getStatus().value(),
                errorCode.getCode(),
                errorCode.getMessage()
        );
    }

    public static ErrorResponse from(BaseErrorCode errorCode, String message) {
        return new ErrorResponse(
                LocalDateTime.now(),
                errorCode.getStatus().value(),
                errorCode.getCode(),
                message
        );
    }
}

