package com.DespesasFinanceiras.V1.Service;

import com.DespesasFinanceiras.V1.DTOS.ReceitaDTO;
import com.DespesasFinanceiras.V1.Entity.ReceitaEntity;
import com.DespesasFinanceiras.V1.Enuns.CategoriaReceitaEnum;
import com.DespesasFinanceiras.V1.Enuns.SituacaoEnum;
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
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ReceitaServiceTest {

    @Mock
    private ReceitaRepository receitaRepository;

    @Mock
    private ReceitaService receitaService;

    @Mock
    private ReceitaEntity receitaEntity1;

    @Mock
    private ReceitaEntity receitaEntity2;

    @Mock
    private ReceitaEntity receitaEntity3;

    @Mock
    private ReceitaEntity receitaEntity4;

    @Mock
    private ReceitaEntity receitaEntity5;

    @Mock
    private ReceitaDTO receitaDTO1;

    @Mock
    private ReceitaDTO receitaDTO2;

    @BeforeEach
    void setup(){
        receitaService = new ReceitaService(receitaRepository);

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
                LocalDateTime.now(),
                SituacaoEnum.PAGA,
                "Daniel");

        receitaEntity5 = new ReceitaEntity(1l,"n/a",
                "Salario",
                new BigDecimal("900.00"),
                CategoriaReceitaEnum.SALARIO,
                LocalDateTime.now(),
                SituacaoEnum.PAGA,
                "Daniel");


        receitaDTO1 = new ReceitaDTO("Salario",
                new BigDecimal("900.00"),
                "n/a",
                CategoriaReceitaEnum.SALARIO,
                LocalDateTime.now(),
                SituacaoEnum.PAGA,
                "Leonardo");

        receitaDTO2 = new ReceitaDTO("Salario",
                new BigDecimal("935.00"),
                "n/a",
                CategoriaReceitaEnum.SALARIO,
                LocalDateTime.now(),
                SituacaoEnum.PAGA,
                "Leonardo");
    };

    @Test
    void deveSalvarReceitaNoBanco(){
        Mockito.when(receitaRepository.save(Mockito.any(ReceitaEntity.class))).thenReturn(receitaEntity1);
        System.out.println(receitaEntity1.getValor());
        var result = receitaService.createReceita(receitaDTO1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(new BigDecimal("900.00"), result.getValor());
    }

    @Test
    void deveRetornarReceitaPeloId(){
        Mockito.when(receitaRepository.findById(1l)).thenReturn(Optional.ofNullable(receitaEntity5));

        var result = receitaService.findById(1l);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(new BigDecimal("900.00"), result.getValor());

    }

    @Test
    void deveAtualizarReceita(){


        Mockito.when(receitaRepository.findById(30l)).thenReturn(Optional.ofNullable(receitaEntity3));

        receitaEntity3.setId(30l);
        receitaEntity3.setName("Azur lane");

        Mockito.when(receitaRepository.save(Mockito.any(ReceitaEntity.class))).thenReturn(receitaEntity3);
        var classConvertida = ReceitaDTO.fromEntity(receitaEntity3);

        var result = receitaService.updateReceita(30L, classConvertida );

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Azur lane", result.getName());
    }


}
