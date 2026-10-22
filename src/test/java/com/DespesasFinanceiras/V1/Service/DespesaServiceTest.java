package com.DespesasFinanceiras.V1.Service;

import com.DespesasFinanceiras.V1.DTOS.DespesaDTO;
import com.DespesasFinanceiras.V1.Entity.DespesasEntity;
import com.DespesasFinanceiras.V1.Enuns.*;
import com.DespesasFinanceiras.V1.Exceptions.NotFoundException;
import com.DespesasFinanceiras.V1.Repository.DespesasRepository;
import com.DespesasFinanceiras.V1.Repository.DespesasRepositoryTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Service
@ExtendWith(MockitoExtension.class)
public class DespesaServiceTest {

    @Mock
    private DespesasRepository despesasRepositoryTest;

    @Mock
    private DespesaService service;

    @Mock
    private DespesaDTO despesaNova;

    @Mock
    private DespesaDTO despesaNova1;

    @Mock
    private DespesaDTO despesaNova2;

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
        service = new DespesaService(despesasRepositoryTest);

        despesaNova = new DespesaDTO(
                "Bilhetes de Cinema",
                "Ida ao cinema com amigos",
                new BigDecimal("30.00"),
                LocalDateTime.now(),
                CategoriaEnum.LAZER,
                SituacaoEnum.A_PAGAR,
                "Daniel"
        );

        despesaNova1 = new DespesaDTO(

                "Claude Code",
                "IA",
                new BigDecimal("110.00"),
                LocalDateTime.now(),
                CategoriaEnum.LAZER,
                SituacaoEnum.A_PAGAR,
                "Daniel"
        );

        despesaNova2 = new DespesaDTO(

                "Claude Code",
                "IA",
                new BigDecimal("110.00"),
                LocalDateTime.now(),
                CategoriaEnum.LAZER,
                SituacaoEnum.A_PAGAR,
                "Daniel"
        );


        despesaNova = new DespesaDTO(
                "Bilhetes de Cinema",
                "Ida ao cinema com amigos",
                new BigDecimal("30.00"),
                LocalDateTime.now(),
                CategoriaEnum.LAZER,
                SituacaoEnum.A_PAGAR,
                "Daniel"
        );

        despesaNova1 = new DespesaDTO(

                "Claude Code",
                "IA",
                new BigDecimal("110.00"),
                LocalDateTime.now(),
                CategoriaEnum.LAZER,
                SituacaoEnum.A_PAGAR,
                "Daniel"
        );

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
    void deveSalvarDespesaNoBanco(){
        DespesasEntity despesasEntitySaveReturn = new DespesasEntity();
        despesasEntitySaveReturn.setId(302L);
        despesasEntitySaveReturn.setName(despesaNova.name());
        despesasEntitySaveReturn.setValor(despesaNova.valor());

        Mockito.when(despesasRepositoryTest.save(Mockito.any(DespesasEntity.class))).thenReturn(despesasEntitySaveReturn);

        var result = service.createDespesa(despesaNova);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Bilhetes de Cinema", result.getName());
        Assertions.assertEquals(new BigDecimal("30.00"), result.getValor());


        Mockito.verify(despesasRepositoryTest, Mockito.times(1)).save(Mockito.any(DespesasEntity.class));
    }

    @Test
    void deveRetornarDespesaPeloId(){
        DespesasEntity despesasEntity = new DespesasEntity();
        despesasEntity.setId(302L);
        despesasEntity.setName(despesaNova.name());

        Mockito.when(despesasRepositoryTest.findById(302L)).thenReturn(Optional.of(despesasEntity));

        var result = service.findById(302L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Bilhetes de Cinema", result.getName());
    }

    @Test
    void deveAtualizarDespesa(){
        DespesasEntity despesasEntity = new DespesasEntity();
        despesasEntity.setId(302L);
        despesasEntity.setName(despesaNova.name());

        Mockito.when(despesasRepositoryTest.findById(302L)).thenReturn(Optional.of(despesasEntity));

        DespesasEntity despesasEntityAtual = new DespesasEntity();
        despesasEntityAtual.setId(302L);
        despesasEntityAtual.setName(despesaNova1.name());

        Mockito.when(despesasRepositoryTest.save(Mockito.any(DespesasEntity.class))).thenReturn(despesasEntityAtual);

        var result = service.updateDespesa(302L, despesaNova1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Claude Code", result.getName());

    }

    @Test
    void deveDeletarDespesa(){
        DespesasEntity despesasEntity = new DespesasEntity();
        despesasEntity.setId(302L);

        Mockito.lenient().when(despesasRepositoryTest.findById(302L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.deleteDespesa(302L));

        Mockito.verify(despesasRepositoryTest, Mockito.never()).deleteById(302L);

    }

    @Test
    void deveRetornarListaDespesas(){
        DespesasEntity despesa1 = new DespesasEntity();
        despesa1.setId(1L);
        despesa1.setName("Mercado");
        despesa1.setValor(new BigDecimal("150.00"));

        DespesasEntity despesa2 = new DespesasEntity();
        despesa2.setId(2L);
        despesa2.setName("Claude Code");
        despesa2.setValor(new BigDecimal("45.00"));

        DespesasEntity despesa3 = new DespesasEntity();
        despesa3.setId(3L);
        despesa3.setName("Farmácia");
        despesa3.setValor(new BigDecimal("45.00"));

        Mockito.when(despesasRepositoryTest.findAll()).thenReturn(List.of(despesa1,despesa2,despesa3));

        List<DespesasEntity> list = service.listAll();

        Assertions.assertNotNull(list);
        Assertions.assertEquals(3, list.size());
        Assertions.assertEquals("Mercado", list.get(0).getName());
        Assertions.assertEquals("Claude Code", list.get(1).getName());


        Mockito.verify(despesasRepositoryTest, Mockito.times(1)).findAll();
    }

    @Test
    void deveRetornarSaldoPelaSituacao(){

        Mockito.when(despesasRepositoryTest.sumByTipo(SituacaoEnum.A_PAGAR)).thenReturn(despesasEntity1.getValor().add(despesasEntity2.getValor()).add(despesasEntity3.getValor()));

        BigDecimal expected = new BigDecimal(243.00).setScale(2, RoundingMode.HALF_UP);
        var result = service.saldoTotal(SituacaoEnum.A_PAGAR);


        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected, result);

        Mockito.verify(despesasRepositoryTest, Mockito.times(1)).sumByTipo(SituacaoEnum.A_PAGAR);
    }

    @Test
    void deveRetonarUmaListaDeDespesasPeloMes(){

        Mockito.when(despesasRepositoryTest.findByAnoMesDespesa(2026,10)).thenReturn(List.of(despesasEntity1,despesasEntity2,despesasEntity3));

        var result = service.despesaMes(2026,10);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(new BigDecimal(110.00).setScale(2, RoundingMode.HALF_UP), result.get(0).getValor());
        Assertions.assertEquals(new BigDecimal(23.00).setScale(2, RoundingMode.HALF_UP), result.get(1).getValor());
        Assertions.assertEquals("Claude Code", result.get(0).getName());
    }

    @Test
    void deveRetonarUmaListaDeDespesasEntreDoisMeses(){

        Mockito.when(despesasRepositoryTest.selectBetweenTwoMonthDespesa(2026,10)).thenReturn(List.of(despesasEntity1,despesasEntity2,despesasEntity3));
        Mockito.when(despesasRepositoryTest.selectBetweenTwoMonthDespesa(2026,9)).thenReturn(List.of(despesasEntity4));

        var result = service.listBetweenTwoMonthDespesa(2026,9,2026,10);

        Assertions.assertEquals("Azur lane", result.get(0).get(0).getName());
    }

}
