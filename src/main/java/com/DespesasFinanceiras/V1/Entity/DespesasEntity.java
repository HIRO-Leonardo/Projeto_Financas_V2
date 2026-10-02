package com.DespesasFinanceiras.V1.Entity;

import com.DespesasFinanceiras.V1.DTOS.DespesaDTO;
import com.DespesasFinanceiras.V1.Enuns.CategoriaEnum;
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
public class DespesasEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "ID da despesa", required = true)
    private Long id;

    @Schema(description = "Nome da despesa", example = "Gasolina", required = true)
    private String name;

    @Schema(description = "Descricao da despesa", example = "Gasolina do dia 22/09/26", required = true)
    private String descricao;

    @Schema(description = "Valor da despesa", example = "25.00", required = true)
    @Column(precision = 10, scale = 2)
    private BigDecimal valor;

    @Schema(description = "Data da despesa", example = "2026-10-09T19:48:00", required = true)
    private LocalDateTime localDateTime;

    @Schema(description = "Categoria da despesa", example = "LAZER", required = true)
    private CategoriaEnum categoriaEnum;

    @Schema(description = "Situação da despesa", example = "A_PAGAR", required = true)
    private SituacaoEnum situacaoEnum;

    @Schema(description = "Quem fez a despesa", example = "Daniel", required = true)
    private String autor;

    public DespesasEntity(DespesaDTO dto){
        this.name = dto.name();
        this.descricao = dto.descricao();
        this.valor = dto.valor();
        this.categoriaEnum = dto.categoriaEnum();
        this.localDateTime = dto.localDateTime();
        this.situacaoEnum = dto.situacaoEnum();
        this.autor = dto.autor();
    }
}
