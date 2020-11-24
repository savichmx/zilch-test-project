package com.zilch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TransactionNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -8575030503579522773L;

    public TransactionNotFoundException(String message) {
        super(message);
    }

}
