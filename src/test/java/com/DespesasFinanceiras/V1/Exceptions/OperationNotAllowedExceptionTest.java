package com.DespesasFinanceiras.V1.Exceptions;

public class OperationNotAllowedExceptionTest extends RuntimeException {
    public OperationNotAllowedExceptionTest(String message) {
        super(message);
    }
}
