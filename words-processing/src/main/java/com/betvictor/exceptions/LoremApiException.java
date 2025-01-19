package com.betvictor.exceptions;

import com.betvictor.exceptions.error.ErrorCode;

public class LoremApiException extends BaseException {
    public LoremApiException() {
        super(ErrorCode.LOREM_API_RESPONSE);
    }
}