package com.DespesasFinanceiras.V1.Controller;


import com.DespesasFinanceiras.V1.DTOS.ReceitaDTO;

import com.DespesasFinanceiras.V1.Entity.ReceitaEntity;
import com.DespesasFinanceiras.V1.Enuns.CategoriaReceitaEnum;
import com.DespesasFinanceiras.V1.Enuns.SituacaoEnum;
import com.DespesasFinanceiras.V1.Service.ReceitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/receita")
public class ReceitaControllerTest {

    private final ReceitaService receitaServiceTest;

    public ReceitaControllerTest(ReceitaService receitaServiceTest) {
        this.receitaServiceTest = receitaServiceTest;

    }

    /*@GetMapping("/todas")
    public ResponseEntity listAll(){
        var result = receitaService.listAll();
        return ResponseEntity.ok(result);
    }*/

    @GetMapping("/paginacao")
    @Operation(summary = "Lista todas as receitas", description = "Lista todas as receitas", tags = {"Receita"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quando a despesa não é encontrada no banco de dados")
    })
    public ResponseEntity<List<ReceitaEntity>> findAllByIdDesc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok( receitaServiceTest.findAllByIdDesc(page, size).getContent());
    }

    @PostMapping
    @Operation(summary = "Cadastra a receita", description = "Cadastra a receita", tags = {"Receita"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quando a despesa não é encontrada no banco de dados")
    })
    public ResponseEntity cadastrarDespesa(@RequestBody @Valid ReceitaDTO receitaDTOTest){
        this.receitaServiceTest.createReceita(receitaDTOTest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quando a despesa não é encontrada no banco de dados")
    })
    @Operation(summary = "Atualiza a receita pelo id", description = "Atualiza a receita pelo id", tags = {"Receita"})
    public ResponseEntity atualizarReceita(@RequestBody @Valid ReceitaDTO receitaDTOTest, @PathVariable Long id){
        ReceitaEntity receitaAtualizada = receitaServiceTest.updateReceita(id, receitaDTOTest);

        return ResponseEntity.ok(receitaAtualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta a receita pelo id", description = "Deleta a receita pelo id", tags = {"Receita"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Quando a despesa não é encontrada no banco de dados")
    })
    public ResponseEntity deletarReceita(@PathVariable Long id){
        receitaServiceTest.deleteReceita(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Encontra a receita pelo id", description = "Encontra a receita pelo id", tags = {"Receita"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quando a despesa não é encontrada no banco de dados")
    })
    public ReceitaEntity findById(@PathVariable @Valid Long id){
        ReceitaEntity receitaEntityTest = receitaServiceTest.findById(id);
        return receitaEntityTest;

    }


    @GetMapping("saldo/{situacao}")
    @Operation(summary = "Soma todas as receitas por uma determinada categoria de receita", description = "Soma todas as receitas por uma determinada categoria de receita", tags = {"Receita"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quando a despesa não é encontrada no banco de dados")
    })
    public BigDecimal valorReceitaByCategoria(@PathVariable CategoriaReceitaEnum situacao){
        var result = receitaServiceTest.sumByType(situacao);
        System.out.println(result);
        return result;
    }

    @GetMapping("/saldo_situacao/{situacaoPagamento}")
    @Operation(summary = "Soma todas as receitas por uma determinada situacao de pagamento da receita", description = "Soma todas as receitas por uma determinada situacao de pagamento da receita", tags = {"Receita"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quando a despesa não é encontrada no banco de dados")
    })
    public BigDecimal valorReceitaSituacao(@PathVariable SituacaoEnum situacaoPagamento){
        var result = receitaServiceTest.sumByTypeSituacion(situacaoPagamento);
        System.out.println(result);
        return result;
    }

    @GetMapping("/buscar/{mes}-{ano}")
    @Operation(summary = "Lista a receita pelo mes", description = "Lista a receita pelo mes", tags = {"Receita"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quando a despesa não é encontrada no banco de dados")
    })
    public List<ReceitaEntity> listByMonthYear(@PathVariable Integer mes, @PathVariable Integer ano){
        var result = receitaServiceTest.receitaMes(ano,mes);
        return result;
    }

    @GetMapping
    @Operation(summary = "Lista a receita pelo mes por parametros", description = "Lista a receita pelo mes por parametros", tags = {"Receita"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quando a despesa não é encontrada no banco de dados")
    })
    public ResponseEntity<List<ReceitaEntity>> listar(
            @RequestParam(required = false) CategoriaReceitaEnum categoriaReceitaEnumTest,
            @RequestParam(required = false) SituacaoEnum situacaoEnumTest,
            @RequestParam(required = false) Integer mes
    ){return ResponseEntity.ok(receitaServiceTest.listarComFiltros(categoriaReceitaEnumTest, situacaoEnumTest,mes));}

    @GetMapping("/comparationbetweentwomonth/{ano}-{mesAnterior}-{ano2}-{mesPosterior}")
    @Operation(summary = "Compara uma receita de um mes com o outro mes", description = "Compara uma receita de um mes com o outro mes", tags = {"Receita"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quando a despesa não é encontrada no banco de dados")
    })
    public ResponseEntity<List<List<ReceitaEntity>>> listBetweenTwoMonth(@PathVariable Integer ano, @PathVariable Integer mesAnterior, @PathVariable Integer ano2, @PathVariable Integer mesPosterior){
        var resultado = receitaServiceTest.listBetweenTwoMonth(ano,mesAnterior,ano2,mesPosterior);
        return ResponseEntity.ok(resultado);
    }
}
