package com.DespesasFinanceiras.V1.Enuns;

public enum CategoriaReceitaEnum {
    SALARIO("Salário"),
    FREELANCE("Freelance"),
    INVESTIMENTOS("Investimentos"),
    RENDA_EXTRA("Renda Extra"),
    PRESENTE("Presente"),
    REEMBOLSO("Reembolso"),
    VENDA("Venda"),
    OUTROS("Outros");

    private final String descricao;

    CategoriaReceitaEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
