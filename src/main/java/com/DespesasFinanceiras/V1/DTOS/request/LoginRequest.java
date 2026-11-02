package com.DespesasFinanceiras.V1.DTOS.request;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(@NotEmpty(message = "E-mail e obrigatório") String email,
                           @NotEmpty(message = "Senha e obrigatório") String password) {
}
