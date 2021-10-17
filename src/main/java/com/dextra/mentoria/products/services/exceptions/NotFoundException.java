package com.dextra.mentoria.products.services.exceptions;

import java.io.Serial;

public class NotFoundException  extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 6956508524642781764L;

    public NotFoundException(String msg){
        super(msg);
    }

}