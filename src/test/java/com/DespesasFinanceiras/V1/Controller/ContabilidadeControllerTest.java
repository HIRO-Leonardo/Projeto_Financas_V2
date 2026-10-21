package com.DespesasFinanceiras.V1.Controller;

import com.DespesasFinanceiras.V1.Service.ContabilidadeService;
import com.DespesasFinanceiras.V1.Service.ContabilidadeServiceTest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contabilidade")
public class ContabilidadeControllerTest {
    private final ContabilidadeService contabilidadeServiceTest;

    public ContabilidadeControllerTest(ContabilidadeService contabilidadeServiceTest) {
        this.contabilidadeServiceTest = contabilidadeServiceTest;
    }

    @GetMapping("/{ano}-{mes}")
    @Operation(summary = "Retorna tanto a receita e despesa referente ao mes", description = "Retorna todas as despesas e receitas do mes EX:08{mes}", tags = {"Contabilidade"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quando a despesa não é encontrada no banco de dados")
    })
    public Map<String, List> stringListMap(@PathVariable Integer ano, @PathVariable Integer mes){
        var resultaReceitaList = contabilidadeServiceTest.listReceitas(ano,mes);
        var resultDespesaList = contabilidadeServiceTest.listarDespesas(ano, mes);

        Map<String,List> result = new HashMap<>();
        result.put("receita", resultaReceitaList);
        result.put("despesa", resultDespesaList);

        return ResponseEntity.ok(result).getBody();
    }
}
