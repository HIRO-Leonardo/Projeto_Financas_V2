package com.DespesasFinanceiras.V1.Service;

import com.DespesasFinanceiras.V1.DTOS.DespesaDTO;
import com.DespesasFinanceiras.V1.Entity.DespesasEntity;
import com.DespesasFinanceiras.V1.Enuns.SituacaoEnum;
import com.DespesasFinanceiras.V1.Exceptions.NotFoundException;
import com.DespesasFinanceiras.V1.Repository.DespesasRepository;
import com.DespesasFinanceiras.V1.Repository.TotalPorCategoria;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DespesaService {
    private final DespesasRepository despesasRepository;


    public DespesaService(DespesasRepository despesasRepository) {
        this.despesasRepository = despesasRepository;
    }

    public List<DespesasEntity> listAll(){
        List<DespesasEntity> despesasEntities = despesasRepository.findAll();
        return despesasEntities;
    }



    public DespesasEntity createDespesa(DespesaDTO dto){
        DespesasEntity despesasEntity = new DespesasEntity(dto);
        var cadastrarDespesa = despesasRepository.save(despesasEntity);
        return cadastrarDespesa;
    }

    public DespesasEntity updateDespesa(Long id, @Valid DespesaDTO despesaDTOAtualizado){
        var despesaExistente = despesasRepository.findById(id);
        if (despesaExistente.isEmpty()){
            throw new RuntimeException("Despesa não encontrada com id: " + id);
        }
        DespesasEntity despesas = despesaExistente.get();
        despesas.setName(despesaDTOAtualizado.name());
        despesas.setDescricao(despesaDTOAtualizado.descricao());
        despesas.setValor(despesaDTOAtualizado.valor());
        despesas.setCategoriaEnum(despesaDTOAtualizado.categoriaEnum());
        despesas.setSituacaoEnum(despesaDTOAtualizado.situacaoEnum());
        return despesasRepository.save(despesas);
    }

    public boolean deleteDespesa(Long id){
        if (!despesasRepository.existsById(id)){
            throw new RuntimeException("Despesa não encontrada com id: " + id);
        }
        despesasRepository.deleteById(id);
        return true;
    }

    public BigDecimal saldoTotal(SituacaoEnum situacaoEnum){
        var result = despesasRepository.sumByTipo(situacaoEnum);
        if (result == null) return BigDecimal.ZERO;

        BigDecimal valorFormatado = result.setScale(2, RoundingMode.HALF_UP);
        return valorFormatado;
    }

    public List<DespesasEntity> despesaMes(Integer ano, Integer mes){
        List<DespesasEntity> resultado = despesasRepository.findByAnoMesDespesa(ano, mes);
        return resultado;
    }

    public List<TotalPorCategoria> relatorioMensal(Integer mes, Integer ano){
        var result = despesasRepository.totalPorCategoriaPorMes(mes, ano);
        return result;
    }

    public List<List<DespesasEntity>> listBetweenTwoMonthDespesa(Integer ano, Integer  mesAnterior, Integer ano2, Integer mesPosterior){
        List<DespesasEntity> mesAnteriorResult= despesasRepository.selectBetweenTwoMonthDespesa(ano, mesAnterior);
        List<DespesasEntity> mesPosteriorResult = despesasRepository.selectBetweenTwoMonthDespesa(ano2,mesPosterior);

        List<List<DespesasEntity>> lists = new ArrayList<>();
        lists.add(mesAnteriorResult);
        lists.add(mesPosteriorResult);

        return lists;
    }

    public List<DespesasEntity> findByAutorList(String autor){
        return despesasRepository.findByAutor(autor);
    }

    public Page<DespesasEntity> findAllByOrderByIdDesc(int page, int size){
        return despesasRepository.findAllByOrderByIdDesc(PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "id")));

    }
}
