package com.DespesasFinanceiras.V1.DTOS;

import com.DespesasFinanceiras.V1.Enuns.CategoriaReceitaEnumTest;
import com.DespesasFinanceiras.V1.Enuns.SituacaoEnumTest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReceitaDTOTest(@NotBlank(message = "Nome é obrigatório") String name,
                             @NotNull(message = "Valor é obrigatório ")  BigDecimal valor,
                             @NotBlank(message = "Descrição é obrigatório ") String descricao,
                             @NotNull(message = "Categoria é obrigatório ") CategoriaReceitaEnumTest categoriaReceitaEnumTest,
                             @NotNull(message = "Data é obrigatório ") LocalDateTime localDateTimeEntrada,
                             @NotNull(message = "Situação é obrigatório ") SituacaoEnumTest situacaoEnumTest,
                             @NotBlank(message = "Autor é brigatório!!") String autor) {
}
