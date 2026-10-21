package com.DespesasFinanceiras.V1.Entity;

import com.DespesasFinanceiras.V1.DTOS.ReceitaDTOTest;
import com.DespesasFinanceiras.V1.Enuns.CategoriaReceitaEnumTest;
import com.DespesasFinanceiras.V1.Enuns.SituacaoEnumTest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@SoftDelete
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceitaEntityTest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "ID da receita", required = true)
    private Long id;

    @Schema(description = "Nome da receita",example = "Salario", required = true)
    private String name;

    @Schema(description = "Descricao da receita", example = "Salario recebido dia 05/10/26", required = true)
    private String descricao;

    @Column(precision = 10, scale = 2)
    @Schema(description = "Valor da receita", example = "25.00", required = true)
    private BigDecimal valor;

    @Schema(description = "Categoria da receita", example = "SALARIO", required = true)
    private CategoriaReceitaEnumTest categoriaReceitaEnumTest;

    @Schema(description = "Data da receita", example = "2026-10-09T19:48:00", required = true)
    private LocalDateTime localDateTimeEntrada;

    @Schema(description = "Situação da despesa", example = "A_RECEBER", required = true)
    private SituacaoEnumTest situacaoEnumTest;

    @Schema(description = "Quem fez a despesa", example = "Daniel", required = true)
    private String autor;

    public ReceitaEntityTest(ReceitaDTOTest receitaDTOTest){
        this.name = receitaDTOTest.name();
        this.valor = receitaDTOTest.valor();
        this.descricao = receitaDTOTest.descricao();
        this.categoriaReceitaEnumTest = receitaDTOTest.categoriaReceitaEnumTest();
        this.localDateTimeEntrada = receitaDTOTest.localDateTimeEntrada();
        this.situacaoEnumTest = receitaDTOTest.situacaoEnumTest();
        this.autor = receitaDTOTest.autor();
    }
}
