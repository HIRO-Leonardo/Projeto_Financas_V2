package com.DespesasFinanceiras.V1.Service;

import com.DespesasFinanceiras.V1.Entity.DespesasEntity;
import com.DespesasFinanceiras.V1.Entity.ReceitaEntity;
import com.DespesasFinanceiras.V1.Enuns.CategoriaEnum;
import com.DespesasFinanceiras.V1.Enuns.CategoriaReceitaEnum;
import com.DespesasFinanceiras.V1.Enuns.SituacaoEnum;
import com.DespesasFinanceiras.V1.Repository.DespesasRepository;
import com.DespesasFinanceiras.V1.Repository.ReceitaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ContabilidadeServiceTest {
    @Mock
    private ReceitaRepository receitaRepository;

    @Mock
    private DespesasRepository despesasRepository;

    @Mock
    private ContabilidadeService contabilidadeService;

    @Mock
    private ReceitaEntity receitaEntity1;

    @Mock
    private ReceitaEntity receitaEntity2;

    @Mock
    private ReceitaEntity receitaEntity3;

    @Mock
    private ReceitaEntity receitaEntity4;

    @Mock
    private DespesasEntity despesasEntity1;

    @Mock
    private DespesasEntity despesasEntity2;

    @Mock
    private DespesasEntity despesasEntity3;

    @Mock
    private DespesasEntity despesasEntity4;

    @BeforeEach
    void setup(){
        contabilidadeService = new ContabilidadeService(receitaRepository,despesasRepository );

        // Parte da receita entity

        receitaEntity1 = new ReceitaEntity("n/a",
                "Salario",
                new BigDecimal("900.00"),
                CategoriaReceitaEnum.SALARIO,
                LocalDateTime.now(),
                SituacaoEnum.PAGA,
                "Leonardo");

        receitaEntity2 = new ReceitaEntity("n/a",
                "Salario",
                new BigDecimal("930.00"),
                CategoriaReceitaEnum.SALARIO,
                LocalDateTime.now(),
                SituacaoEnum.PAGA,
                "Leonardo");

        receitaEntity3 = new ReceitaEntity(20l,"n/a",
                "Salario",
                new BigDecimal("935.00"),
                CategoriaReceitaEnum.SALARIO,
                LocalDateTime.now(),
                SituacaoEnum.PAGA,
                "Leonardo");

        receitaEntity4 = new ReceitaEntity("n/a",
                "Salario",
                new BigDecimal("900.00"),
                CategoriaReceitaEnum.SALARIO,
                LocalDateTime.of(2026, 9, 15, 14, 30),
                SituacaoEnum.PAGA,
                "Daniel");



        // Parte da despesa entity
        despesasEntity1 = new DespesasEntity(
                1L,
                "Claude Code",
                "IA",
                new BigDecimal("110.00"),
                LocalDateTime.now(),
                CategoriaEnum.LAZER,
                SituacaoEnum.A_PAGAR,
                "Daniel"
        );

        despesasEntity2 = new DespesasEntity(
                2L,
                "Google AI",
                "IA",
                new BigDecimal("23.00"),
                LocalDateTime.now(),
                CategoriaEnum.LAZER,
                SituacaoEnum.A_PAGAR,
                "Daniel"
        );

        despesasEntity3 = new DespesasEntity(
                3L,
                "Azur lane",
                "Game",
                new BigDecimal("110.00"),
                LocalDateTime.now(),
                CategoriaEnum.LAZER,
                SituacaoEnum.A_PAGAR,
                "Daniel"
        );

        despesasEntity4 = new DespesasEntity(
                3L,
                "Azur lane",
                "Game",
                new BigDecimal("110.00"),
                LocalDateTime.of(2026, 9, 15, 14, 30),
                CategoriaEnum.LAZER,
                SituacaoEnum.A_PAGAR,
                "Daniel"
        );

    }

    @Test
    void deveRetornarListaReceita(){
        Mockito.when(receitaRepository.listAllReceitaPerMonth(2026,10)).thenReturn(List.of(receitaEntity1,receitaEntity2,receitaEntity3));

        var result = contabilidadeService.listReceitas(2026,10);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.size());
    }

    @Test
    void deveRetornarListaDespesa(){
        Mockito.when(despesasRepository.selectAllDespesasPerMonth(2026,10)).thenReturn(List.of(despesasEntity1,despesasEntity2,despesasEntity3));


        var result = contabilidadeService.listarDespesas(2026,10);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.size());
    }
}
