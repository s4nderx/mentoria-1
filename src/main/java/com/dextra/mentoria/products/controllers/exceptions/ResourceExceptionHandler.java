package com.dextra.mentoria.products.controllers.exceptions;


import com.dextra.mentoria.products.services.exceptions.DataIntegrityException;
import com.dextra.mentoria.products.services.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public StandardError entityNotFound(NotFoundException exception, HttpServletRequest request){
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(NOT_FOUND.value());
        error.setError("Resource not found");
        error.setMessage(exception.getMessage());
        error.setPath(request.getRequestURI());
        return error;
    }

    @ExceptionHandler(DataIntegrityException.class)
    @ResponseStatus(BAD_REQUEST)
    public StandardError dataBase(DataIntegrityException exception, HttpServletRequest request){
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(BAD_REQUEST.value());
        error.setError("Data Integrity error");
        error.setMessage(exception.getMessage());
        error.setPath(request.getRequestURI());
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ValidationError validation(MethodArgumentNotValidException exception, HttpServletRequest request){
        ValidationError error = new ValidationError();
        List<FieldError> fieldErrorList = exception.getBindingResult().getFieldErrors();
        List<String> fields = fieldErrorList.stream().map(FieldError::getField).collect(Collectors.toList());

        error.setTimestamp(Instant.now());
        error.setStatus(UNPROCESSABLE_ENTITY.value());
        error.setError("Validation Exception");
        error.setMessage(this.getValidationErrorMessage(fields));
        error.setPath(request.getRequestURI());
        for(FieldError f : fieldErrorList){
            error.addError(f.getField(), f.getDefaultMessage());
        }

        return error;
    }

    private String getValidationErrorMessage(List<String> strs) {
        return "Validation failed for arguments: " + String.join(", ", strs);
    }
}
