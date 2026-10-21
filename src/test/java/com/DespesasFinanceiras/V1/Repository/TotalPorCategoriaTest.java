package com.DespesasFinanceiras.V1.Repository;

import com.DespesasFinanceiras.V1.Enuns.CategoriaEnumTest;

import java.math.BigDecimal;

public interface TotalPorCategoriaTest {
    CategoriaEnumTest getCategoria();
    BigDecimal getValor();
}
