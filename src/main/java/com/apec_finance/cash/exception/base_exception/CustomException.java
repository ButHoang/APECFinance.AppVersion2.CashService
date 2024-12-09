package com.apec_finance.cash.exception.base_exception;

import com.apec_finance.cash.comon.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String customMessage;

    protected CustomException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }
}
