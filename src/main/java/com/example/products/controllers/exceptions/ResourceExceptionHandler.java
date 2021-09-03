package com.example.products.controllers.exceptions;


import com.example.products.services.exceptions.DataIntegrityException;
import com.example.products.services.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(NotFoundException exception, HttpServletRequest request){
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setError("Resource not found");
        error.setMessage(exception.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<StandardError> dataBase(DataIntegrityException exception, HttpServletRequest request){
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError("Data Integrity error");
        error.setMessage(exception.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException exception, HttpServletRequest request){
        ValidationError error = new ValidationError();
        List<FieldError> fieldErrorList = exception.getBindingResult().getFieldErrors();
        List<String> fields = fieldErrorList.stream().map(FieldError::getField).collect(Collectors.toList());

        error.setTimestamp(Instant.now());
        error.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        error.setError("Validation Exception");
        error.setMessage(this.getValidationErrorMessage(fields));
        error.setPath(request.getRequestURI());
        for(FieldError f : fieldErrorList){
            error.addError(f.getField(), f.getDefaultMessage());
        }

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    private String getValidationErrorMessage(List<String> strs) {
        return "Validation failed for arguments: " + String.join(", ", strs);
    }
}
