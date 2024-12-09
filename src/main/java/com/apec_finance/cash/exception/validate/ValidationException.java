package com.apec_finance.cash.exception.validate;

import com.apec_finance.cash.comon.CommonErrorCodes;
import com.apec_finance.cash.exception.base_exception.CustomException;

public class ValidationException extends CustomException {
    public ValidationException(String message) {
        super(CommonErrorCodes.VALIDATION_ERROR, message);
    }
}
