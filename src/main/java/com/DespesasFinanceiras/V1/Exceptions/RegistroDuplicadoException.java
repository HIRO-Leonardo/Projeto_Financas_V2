package com.DespesasFinanceiras.V1.Exceptions;

public class RegistroDuplicadoException extends RuntimeException {
    public RegistroDuplicadoException(String message) {
        super(message);
    }
}
