package com.softuni.mehana.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class NullCartItemEntitiesException extends RuntimeException{

    public NullCartItemEntitiesException(String message) {
        super(message);
    }
}
