package com.apec_finance.cash.comon;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getCode();

    String getMessage();

    HttpStatus getHttpStatus();
}
