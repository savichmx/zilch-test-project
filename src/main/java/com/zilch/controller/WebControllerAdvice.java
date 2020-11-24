package com.zilch.controller;

import com.zilch.exception.CustomerNotFoundException;
import com.zilch.exception.TransactionNotFoundException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
public class WebControllerAdvice {

    @ExceptionHandler(TransactionNotFoundException.class)
    public void handleTransactionNotFoundException(TransactionNotFoundException e) {
        log.warn(e.getMessage());
        throw e;
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public void handleCustomerNotFoundException(CustomerNotFoundException e) {
        log.warn(e.getMessage());
        throw e;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServerError> handleExceptions(Exception e, WebRequest request) {
        log.error(e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(new ServerError(INTERNAL_SERVER_ERROR, "Internal error", (ServletWebRequest) request), INTERNAL_SERVER_ERROR);
    }

    @Getter
    private static class ServerError {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;

        private ServerError(HttpStatus httpStatus, String message, ServletWebRequest request) {
            this.timestamp = LocalDateTime.now();
            this.status = httpStatus.value();
            this.error = httpStatus.getReasonPhrase();
            this.message = message;
            this.path = request.getRequest().getRequestURI();
        }
    }
}
