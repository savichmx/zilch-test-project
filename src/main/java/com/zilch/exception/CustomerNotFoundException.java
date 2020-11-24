package com.zilch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 7949576925226290313L;

    public CustomerNotFoundException(String message) {
        super(message);
    }

}
