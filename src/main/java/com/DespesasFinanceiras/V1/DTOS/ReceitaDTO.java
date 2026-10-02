package com.DespesasFinanceiras.V1.DTOS;

import com.DespesasFinanceiras.V1.Enuns.CategoriaReceitaEnum;
import com.DespesasFinanceiras.V1.Enuns.SituacaoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReceitaDTO(@NotBlank(message = "Nome é obrigatório") String name,
                         @NotNull(message = "Valor é obrigatório ")  BigDecimal valor,
                         @NotBlank(message = "Descrição é obrigatório ") String descricao,
                         @NotNull(message = "Categoria é obrigatório ") CategoriaReceitaEnum categoriaReceitaEnum,
                         @NotNull(message = "Data é obrigatório ") LocalDateTime localDateTimeEntrada,
                         @NotNull(message = "Situação é obrigatório ") SituacaoEnum situacaoEnum,
                         @NotBlank(message = "Autor é brigatório!!") String autor) {
}
