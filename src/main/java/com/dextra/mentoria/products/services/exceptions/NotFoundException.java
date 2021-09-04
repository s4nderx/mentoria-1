package com.dextra.mentoria.products.services.exceptions;

public class NotFoundException  extends RuntimeException {
    private static final long serialVersionUID = 6956508524642781764L;

    public NotFoundException(String msg){
        super(msg);
    }

}