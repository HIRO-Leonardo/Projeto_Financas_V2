package com.DespesasFinanceiras.V1.DTOS;

import com.DespesasFinanceiras.V1.Enuns.CategoriaEnumTest;
import com.DespesasFinanceiras.V1.Enuns.SituacaoEnumTest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DespesaDTOTest(@NotBlank(message = "Nome é obrigatório") String name,
                             @NotBlank(message = "Descrição é obrigatório") String descricao,
                             @NotNull(message = "valor é obrigatória") BigDecimal valor,
                             @NotNull(message = "Data é obrigatório ") LocalDateTime localDateTime,
                             @NotNull(message = "Categoria é obrigatório ") CategoriaEnumTest categoriaEnumTest,
                             @NotNull(message = "Situação é obrigatório ") SituacaoEnumTest situacaoEnumTest,
                             @NotBlank(message = "Autor é brigatório!!") String autor){
}
