package com.DespesasFinanceiras.V1.Exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message){
        super(message);
    }
}
