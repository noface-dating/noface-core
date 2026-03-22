package com.duri.duricore.onboarding.exception;

import com.duri.duricore.common.exception.BaseErrorCode;
import com.duri.duricore.common.exception.CoreException;

public class OnboardingException extends CoreException {
    
    public OnboardingException(BaseErrorCode errorCode) {
        super(errorCode);
    }
}

