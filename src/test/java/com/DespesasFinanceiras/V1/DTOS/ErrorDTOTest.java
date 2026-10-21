package com.DespesasFinanceiras.V1.DTOS;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorDTOTest {
    private String status;
    private String message;
    private String lancamento;
    private String times;


    public ErrorDTOTest(String status, String message, String lancamento) {
        this.status = status;
        this.message = message;
        this.lancamento = lancamento;
        this.times = LocalDateTime.now().toString();
    }
}
