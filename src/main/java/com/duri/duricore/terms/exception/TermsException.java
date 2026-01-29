package com.duri.duricore.terms.exception;

public class TermsException extends RuntimeException {

    private final TermsErrorCode errorCode;

    public TermsException(TermsErrorCode errorCode) {
        super(errorCode.message());
        this.errorCode = errorCode;
    }

    public TermsErrorCode getErrorCode() {
        return errorCode;
    }
}
