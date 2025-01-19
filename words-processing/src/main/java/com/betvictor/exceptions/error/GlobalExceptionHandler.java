package com.betvictor.exceptions.error;


import com.betvictor.exceptions.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Log4j2
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
    log.error(
        "Handling {} exception with error code: {}",
        ex.getClass().getSimpleName(),
        ex.getErrorCode());

    final ErrorCode errorCode = ex.getErrorCode();
    return ResponseEntity.status(errorCode.getStatus())
        .contentType(MediaType.APPLICATION_JSON)
        .body(
            ErrorResponse.builder()
                .name(errorCode.name())
                .message(ex.getMessage())
                .build());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    log.error(
        "Handling {} exception: {}", ex.getClass().getSimpleName(), ex.getMessage(), ex.getCause());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(
            ErrorResponse.builder()
                .name(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .message("error.internal_server")
                .build());
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMissingHeaders(Exception ex) {

    log.error(
        "Handling {} exception: {}", ex.getClass().getSimpleName(), ex.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .contentType(MediaType.APPLICATION_JSON)
        .body(
            ErrorResponse.builder()
                .name(HttpStatus.BAD_REQUEST.name())
                .message("Length time does not match to any of [SHORT, MEDIUM, LONG, VERYLONG] .")
                .build()
        );
  }
}