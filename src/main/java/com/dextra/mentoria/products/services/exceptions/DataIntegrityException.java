package com.dextra.mentoria.products.services.exceptions;

import java.io.Serial;

public class DataIntegrityException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -88680151668732048L;

    public DataIntegrityException(String msg){
        super(msg);
    }
}