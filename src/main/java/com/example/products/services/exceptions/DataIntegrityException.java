package com.example.products.services.exceptions;

public class DataIntegrityException extends RuntimeException {
    private static final long serialVersionUID = -88680151668732048L;

    public DataIntegrityException(String msg){
        super(msg);
    }
}