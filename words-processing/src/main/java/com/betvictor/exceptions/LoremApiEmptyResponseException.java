package com.betvictor.exceptions;


import com.betvictor.exceptions.error.ErrorCode;

public class LoremApiEmptyResponseException extends BaseException {

    public LoremApiEmptyResponseException() {
        super(ErrorCode.LOREM_API_EMPTY_RESPONSE);
    }
}
