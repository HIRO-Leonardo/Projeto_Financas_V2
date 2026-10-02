package com.DespesasFinanceiras.V1.Controller;

import com.DespesasFinanceiras.V1.Service.ContabilidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contabilidade")
public class ContabilidadeController {
    private final ContabilidadeService contabilidadeService;

    public ContabilidadeController(ContabilidadeService contabilidadeService) {
        this.contabilidadeService = contabilidadeService;
    }

    @GetMapping("/{ano}-{mes}")
    @Operation(summary = "Retorna tanto a receita e despesa referente ao mes", description = "Retorna todas as despesas e receitas do mes EX:08{mes}", tags = {"Contabilidade"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quando a despesa não é encontrada no banco de dados")
    })
    public Map<String, List> stringListMap(@PathVariable Integer ano, @PathVariable Integer mes){
        var resultaReceitaList = contabilidadeService.listReceitas(ano,mes);
        var resultDespesaList = contabilidadeService.listarDespesas(ano, mes);

        Map<String,List> result = new HashMap<>();
        result.put("receita", resultaReceitaList);
        result.put("despesa", resultDespesaList);

        return ResponseEntity.ok(result).getBody();
    }
}
