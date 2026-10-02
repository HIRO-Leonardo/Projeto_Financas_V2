package com.DespesasFinanceiras.V1.Repository;

import com.DespesasFinanceiras.V1.Enuns.CategoriaEnum;

import java.math.BigDecimal;

public interface TotalPorCategoria {
    CategoriaEnum getCategoria();
    BigDecimal getValor();
}
