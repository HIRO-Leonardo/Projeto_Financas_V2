package com.DespesasFinanceiras.V1.Service;

import com.DespesasFinanceiras.V1.Entity.DespesasEntity;
import com.DespesasFinanceiras.V1.Entity.ReceitaEntity;
import com.DespesasFinanceiras.V1.Repository.DespesasRepository;
import com.DespesasFinanceiras.V1.Repository.ReceitaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ContabilidadeService {

    private final ReceitaRepository receitaRepository;
    private final DespesasRepository despesasRepository;

    public ContabilidadeService(ReceitaRepository receitaRepository, DespesasRepository despesasRepository) {
        this.receitaRepository = receitaRepository;
        this.despesasRepository = despesasRepository;
    }

    public List<DespesasEntity> listarDespesas(Integer ano, Integer mes){
        var result = despesasRepository.selectAllDespesasPerMonth(ano,mes);
        return result;
    }

    public List<ReceitaEntity> listReceitas(Integer ano, Integer mes){
        var result = receitaRepository.listAllReceitaPerMonth(ano,mes);
        return result;
    }
}
