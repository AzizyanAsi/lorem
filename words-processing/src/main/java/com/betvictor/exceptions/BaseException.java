package com.betvictor.exceptions;


import com.betvictor.exceptions.error.ErrorCode;
import lombok.Getter;

public abstract class BaseException extends RuntimeException {
  @Getter
  private final ErrorCode errorCode;
  private final String message;

  protected BaseException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.message = errorCode.getMessage();
  }

    @Override
  public String getMessage() {
    return message;
  }
}