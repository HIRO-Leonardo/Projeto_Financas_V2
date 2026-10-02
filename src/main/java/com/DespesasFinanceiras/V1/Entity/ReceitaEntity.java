package com.DespesasFinanceiras.V1.Entity;

import com.DespesasFinanceiras.V1.DTOS.ReceitaDTO;
import com.DespesasFinanceiras.V1.Enuns.CategoriaReceitaEnum;
import com.DespesasFinanceiras.V1.Enuns.SituacaoEnum;
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
public class ReceitaEntity {
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
    private CategoriaReceitaEnum categoriaReceitaEnum;

    @Schema(description = "Data da receita", example = "2026-10-09T19:48:00", required = true)
    private LocalDateTime localDateTimeEntrada;

    @Schema(description = "Situação da despesa", example = "A_RECEBER", required = true)
    private SituacaoEnum situacaoEnum;

    @Schema(description = "Quem fez a despesa", example = "Daniel", required = true)
    private String autor;

    public ReceitaEntity(ReceitaDTO receitaDTO){
        this.name = receitaDTO.name();
        this.valor = receitaDTO.valor();
        this.descricao = receitaDTO.descricao();
        this.categoriaReceitaEnum = receitaDTO.categoriaReceitaEnum();
        this.localDateTimeEntrada = receitaDTO.localDateTimeEntrada();
        this.situacaoEnum = receitaDTO.situacaoEnum();
        this.autor = receitaDTO.autor();
    }
}
