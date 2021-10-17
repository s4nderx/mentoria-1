package com.dextra.mentoria.products.controllers.exceptions;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.servlet.http.HttpServletRequest;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationError extends StandardError {
    @Serial
    private static final long serialVersionUID = 5845325026677519769L;

    public ValidationError(Exception exception, HttpServletRequest request) {
        super(exception, request);

        MethodArgumentNotValidException ex = (MethodArgumentNotValidException) exception;
        List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
        List<String> fields = fieldErrorList.stream().map(FieldError::getField).collect(Collectors.toList());

        super.setMessage(this.getValidationErrorMessage(fields));

        for(FieldError f : fieldErrorList){
            this.addError(f.getField(), f.getDefaultMessage());
        }
    }

    private final List<FieldMessage> errors = new ArrayList<>();

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void addError(String fieldName, String message){
        errors.add(new FieldMessage(fieldName, message));
    }


    private String getValidationErrorMessage(List<String> fields) {
        return "Validation failed for arguments: " + String.join(", ", fields);
    }
}