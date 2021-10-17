package com.dextra.mentoria.products.controllers.exceptions;


import com.dextra.mentoria.products.services.exceptions.DataIntegrityException;
import com.dextra.mentoria.products.services.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionHandlerController {

    @Hidden
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public StandardError entityNotFound(NotFoundException exception, HttpServletRequest request){
        return new StandardError(exception, request)
                .setStatus(NOT_FOUND.value())
                .setError("Resource not found");
    }

    @ExceptionHandler(DataIntegrityException.class)
    @ResponseStatus(BAD_REQUEST)
    public StandardError dataBase(DataIntegrityException exception, HttpServletRequest request){
        return new StandardError(exception, request)
                .setStatus(BAD_REQUEST.value())
                .setError("Data Integrity error");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ValidationError validation(MethodArgumentNotValidException exception, HttpServletRequest request){
        return (ValidationError) new ValidationError(exception, request)
                .setError("Validation Exception")
                .setStatus(UNPROCESSABLE_ENTITY.value());
    }

}
