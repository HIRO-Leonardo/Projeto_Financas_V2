package com.DespesasFinanceiras.V1.Enuns;

public enum CategoriaReceitaEnumTest {
    SALARIO("Salário"),
    FREELANCE("Freelance"),
    INVESTIMENTOS("Investimentos"),
    RENDA_EXTRA("Renda Extra"),
    PRESENTE("Presente"),
    REEMBOLSO("Reembolso"),
    VENDA("Venda"),
    OUTROS("Outros");

    private final String descricao;

    CategoriaReceitaEnumTest(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
