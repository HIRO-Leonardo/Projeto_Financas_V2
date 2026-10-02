package com.DespesasFinanceiras.V1.DTOS;

import com.DespesasFinanceiras.V1.Enuns.CategoriaEnum;
import com.DespesasFinanceiras.V1.Enuns.SituacaoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DespesaDTO(@NotBlank(message = "Nome é obrigatório") String name,
                         @NotBlank(message = "Descrição é obrigatório") String descricao,
                         @NotNull(message = "valor é obrigatória") BigDecimal valor,
                         @NotNull(message = "Data é obrigatório ") LocalDateTime localDateTime,
                         @NotNull(message = "Categoria é obrigatório ") CategoriaEnum categoriaEnum,
                         @NotNull(message = "Situação é obrigatório ") SituacaoEnum situacaoEnum,
                         @NotBlank(message = "Autor é brigatório!!") String autor){
}
