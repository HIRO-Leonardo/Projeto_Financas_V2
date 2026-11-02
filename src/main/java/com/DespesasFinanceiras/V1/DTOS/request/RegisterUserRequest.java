package com.DespesasFinanceiras.V1.DTOS.request;

import jakarta.validation.constraints.NotEmpty;

public record RegisterUserRequest(@NotEmpty(message = "Nome e obrigatório") String name,
                                  @NotEmpty(message = "E-mail e obrigatório") String email,
                                  @NotEmpty(message = "Senha e obrigatório") String password) {

}
