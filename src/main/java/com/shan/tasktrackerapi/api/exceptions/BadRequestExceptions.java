package com.shan.tasktrackerapi.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestExceptions extends RuntimeException {
    public BadRequestExceptions(String message, String name){

        super(message);

    }
}
