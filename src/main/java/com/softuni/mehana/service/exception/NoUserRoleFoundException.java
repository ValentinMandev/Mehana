package com.softuni.mehana.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class NoUserRoleFoundException extends RuntimeException {

    public NoUserRoleFoundException(String message) {
        super(message);
    }
}
