package com.DespesasFinanceiras.V1.Service;

import com.DespesasFinanceiras.V1.DTOS.ReceitaDTO;
import com.DespesasFinanceiras.V1.Entity.ReceitaEntity;
import com.DespesasFinanceiras.V1.Enuns.CategoriaReceitaEnum;
import com.DespesasFinanceiras.V1.Enuns.SituacaoEnum;
import com.DespesasFinanceiras.V1.Exceptions.NotFoundException;
import com.DespesasFinanceiras.V1.Repository.ReceitaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReceitaService {
    private final ReceitaRepository receitaRepository;

    public ReceitaService(ReceitaRepository receitaRepository) {
        this.receitaRepository = receitaRepository;
    }


    public Page<ReceitaEntity> findAllByIdDesc(int page, int size){
        return receitaRepository.findAllByOrderByIdDesc(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
    }

    public List<ReceitaEntity> listAll(){
        List<ReceitaEntity> receitaEntities = receitaRepository.findAll();
        return receitaEntities;
    }

    public ReceitaEntity findById(Long id){
        return receitaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Receita não encontrada com id "));
    }

    public ReceitaEntity createReceita(ReceitaDTO dto){
        ReceitaEntity despesasEntity = new ReceitaEntity(dto);
        var cadastrarReceita = receitaRepository.save(despesasEntity);
        return cadastrarReceita;
    }

    public ReceitaEntity updateReceita(Long id, ReceitaDTO receitaDTOAtualizada){
        var receitaExistente = receitaRepository.findById(id);
        if (receitaExistente.isEmpty()){
            throw new RuntimeException("Receita não encontrada com id: " + id);
        }
        ReceitaEntity receita = receitaExistente.get();
        receita.setName(receitaDTOAtualizada.name());
        receita.setDescricao(receitaDTOAtualizada.descricao());
        receita.setCategoriaReceitaEnum(receitaDTOAtualizada.categoriaReceitaEnum());
        receita.setValor(receitaDTOAtualizada.valor());
        receita.setSituacaoEnum(receitaDTOAtualizada.situacaoEnum());
        return receitaRepository.save(receita);
    }

    public void deleteReceita(Long id){
        if (!receitaRepository.existsById(id)){
            throw new RuntimeException("Receita não encontrada com id: " + id);
        }
        receitaRepository.deleteById(id);
    }

    public BigDecimal sumAll(){
        var result = receitaRepository.sumAllByValor();
        if (result == null) return BigDecimal.ZERO;
        var valorFormatado = result.setScale(2, RoundingMode.HALF_UP);
        return valorFormatado;
    }

    public BigDecimal sumByType(CategoriaReceitaEnum receitaEnum){
        var result = receitaRepository.sumByType(receitaEnum);
        if (result == null) return BigDecimal.ZERO;
        var valorFormatado = result.setScale(2, RoundingMode.HALF_UP);
        return valorFormatado;
    }
    public BigDecimal sumByTypeSituacion(SituacaoEnum situacaoEnum){
        var result = receitaRepository.sumBySituacion(situacaoEnum);
        if (result == null) return BigDecimal.ZERO;
        var valorFormatado = result.setScale(2, RoundingMode.HALF_UP);
        return valorFormatado;
    }

    public List<ReceitaEntity> receitaMes(Integer ano, Integer mes){
        var resultado = receitaRepository.findByAnoMes(ano, mes);
        return resultado;
    }

    public List<ReceitaEntity> listarComFiltros(CategoriaReceitaEnum categoriaReceitaEnum, SituacaoEnum situacaoEnum, Integer mes){
        Specification<ReceitaEntity> spec = ReceitaRepository.comFiltros(categoriaReceitaEnum,situacaoEnum,mes);
        return receitaRepository.findAll(spec);
    }

    public List<List<ReceitaEntity>> listBetweenTwoMonth(Integer ano, Integer  mesAnterior, Integer ano2, Integer mesPosterior){
        var resultListMesAnterior = receitaRepository.selectBetweenTwoMonth(ano,mesAnterior);
        var resultListMesPosterior = receitaRepository.selectBetweenTwoMonth(ano2,mesPosterior);

        List<List<ReceitaEntity>> resultList = new ArrayList<>();
        resultList.add(resultListMesAnterior);
        resultList.add(resultListMesPosterior);

        return resultList;
    }

    public List<ReceitaEntity> findByAutor(String autor){
        return receitaRepository.findByAutor(autor);
    }

}
