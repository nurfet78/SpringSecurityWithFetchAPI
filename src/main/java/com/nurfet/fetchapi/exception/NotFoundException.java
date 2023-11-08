package com.nurfet.fetchapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Requested item not found")
public class NotFoundException extends RuntimeException{

    private Long objIdentifier;

    public <T> NotFoundException(Class<T> cls, Long id) {
        super(cls.getSimpleName() + " с id: " + id + " не найден!");
    }

    public Long getObjIdentifier() {
        return objIdentifier;
    }
}
