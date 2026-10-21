package com.DespesasFinanceiras.V1.Service;

import com.DespesasFinanceiras.V1.DTOS.DespesaDTO;
import com.DespesasFinanceiras.V1.Entity.DespesasEntity;
import com.DespesasFinanceiras.V1.Enuns.*;
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
import java.time.LocalDateTime;

@Service
@ExtendWith(MockitoExtension.class)
public class DespesaServiceTest {

    @Mock
    private DespesasRepository despesasRepositoryTest;

    @Mock
    private DespesaService service;

    @Mock
    private DespesaDTO despesaNova;

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
}
