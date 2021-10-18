package com.dextra.mentoria.products.services.exceptions;

import java.io.Serial;

public class PatchException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 247153251294203344L;

    public PatchException(String msg){
        super(msg);
    }
}