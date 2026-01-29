package com.duri.duricore.terms.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TermsExceptionHandler {

    @ExceptionHandler(TermsException.class)
    public ResponseEntity<ErrorResponse> handleTermsException(TermsException ex) {
        TermsErrorCode code = ex.getErrorCode();
        return ResponseEntity.status(code.status()).body(new ErrorResponse(code.name(), code.message()));
    }

    public record ErrorResponse(String code, String message) {}
}
