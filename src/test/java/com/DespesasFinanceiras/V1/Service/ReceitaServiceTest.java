package com.DespesasFinanceiras.V1.Service;

import com.DespesasFinanceiras.V1.DTOS.ReceitaDTO;
import com.DespesasFinanceiras.V1.Entity.ReceitaEntity;
import com.DespesasFinanceiras.V1.Enuns.CategoriaEnum;
import com.DespesasFinanceiras.V1.Enuns.CategoriaReceitaEnum;
import com.DespesasFinanceiras.V1.Enuns.SituacaoEnum;
import com.DespesasFinanceiras.V1.Exceptions.NotFoundException;
import com.DespesasFinanceiras.V1.Repository.ReceitaRepository;
import com.DespesasFinanceiras.V1.Repository.TotalPorCategoria;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
                LocalDateTime.of(2026, 9, 15, 14, 30),
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

    @Test
    void deveDeletarReceita(){
        Mockito.lenient().when(receitaRepository.findById(30L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> receitaService.deleteReceita(30L));

        Mockito.verify(receitaRepository, Mockito.never()).deleteById(30L);
    }

    @Test
    void deveRetornarListaReceitaComPageable(){
        Page<ReceitaEntity> pageFalse = new PageImpl<>(List.of(receitaEntity1,receitaEntity2,receitaEntity3));
        Mockito.when(receitaRepository.findAllByOrderByIdDesc(Mockito.any(Pageable.class))).thenReturn(pageFalse);

        var result = receitaService.findAllByIdDesc(1,4);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.getTotalElements());
        Assertions.assertEquals(1, result.getTotalPages());
    }

    @Test
    void deveRetornarUmaListaPeloAutor(){
        Mockito.when(receitaRepository.findByAutor("Leonardo")).thenReturn(List.of(receitaEntity1,receitaEntity2,receitaEntity3));

        var result = receitaService.findByAutor("Leonardo");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(3,result.size());
        Assertions.assertEquals("Salario", result.get(0).getName());
    }

    @Test
    void deveRetonarUmaListaDeDespesasEntreDoisMeses(){
        Mockito.when(receitaRepository.selectBetweenTwoMonth(2026,10)).thenReturn(List.of(receitaEntity1,receitaEntity2,receitaEntity3));
        Mockito.when(receitaRepository.selectBetweenTwoMonth(2026,9)).thenReturn(List.of(receitaEntity4));

        var result = receitaService.listBetweenTwoMonth(2026,9,2026,10);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Salario", result.get(0).get(0).getName());
    }

    @Test
    void deveRetornarSaldoPelaSituacao(){
        Mockito.when(receitaRepository.sumBySituacion(SituacaoEnum.PAGA)).thenReturn(receitaEntity3.getValor());

        var result = receitaService.sumByTypeSituacion(SituacaoEnum.PAGA);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(new BigDecimal("935.00"), result);
    }

    @Test
    void deveRetonarUmaListaDeDespesasPeloMes(){
        Mockito.when(receitaRepository.findByAnoMes(2026,10)).thenReturn(List.of(receitaEntity1,receitaEntity2,receitaEntity3));
        var result = receitaService.receitaMes(2026,10);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(new BigDecimal("935.00"), result.get(2).getValor());
    }


}
