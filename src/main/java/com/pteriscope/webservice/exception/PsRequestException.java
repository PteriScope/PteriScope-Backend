package com.pteriscope.webservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PsRequestException extends RuntimeException{

    private final HttpStatus status;

    public PsRequestException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
