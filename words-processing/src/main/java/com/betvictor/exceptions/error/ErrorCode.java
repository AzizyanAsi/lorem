package com.betvictor.exceptions.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    LOREM_API_EMPTY_RESPONSE(HttpStatus.EXPECTATION_FAILED, "Lorem Api response was empty."),
    LOREM_API_RESPONSE(HttpStatus.UNPROCESSABLE_ENTITY, "Request to Lorem Api was failed."),
    PRODUCE_MESSAGE_TO_KAFKA_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "Send message to kafka was failed.");

    private final HttpStatus status;
    private final String message;
}
