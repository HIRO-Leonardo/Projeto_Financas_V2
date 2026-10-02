package com.DespesasFinanceiras.V1.Controller;

import com.DespesasFinanceiras.V1.DTOS.DespesaDTO;
import com.DespesasFinanceiras.V1.Entity.DespesasEntity;
import com.DespesasFinanceiras.V1.Enuns.SituacaoEnum;
import com.DespesasFinanceiras.V1.Repository.TotalPorCategoria;
import com.DespesasFinanceiras.V1.Service.DespesaService;
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
@RequestMapping("/despesa")
public class DespesasController {

    private final DespesaService despesaService;

    public DespesasController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @GetMapping
    @Operation(summary = "Lista todas as despesas", description = "Lista todas as despesa no banco de dados", tags = {"Despesa"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quando a despesa não é encontrada no banco de dados")
    })
    public ResponseEntity listAll(){
        var result = despesaService.listAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/paginacao")
    @Operation(summary = "Lista todas as despesas com paginacao", description = "Lista todas as despesa no banco de dados com pageable", tags = {"Despesa"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quando a despesa não é encontrada no banco de dados")
    })
        public ResponseEntity<List<DespesasEntity>> findAllByIdDesc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(despesaService.findAllByOrderByIdDesc(page, size).getContent());
    }


    @PostMapping
    @Operation(summary = "Cadastra as despesas", description = "Faz o cadastro das despesa no banco de dados", tags = {"Despesa"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quando a despesa não é encontrada no banco de dados")
    })
    public ResponseEntity cadastrarDespesa(@RequestBody @Valid DespesaDTO despesaDTO){
        this.despesaService.createDespesa(despesaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quando a despesa não é encontrada no banco de dados")
    })
    @Operation(summary = "Atualiza a despesas pelo id", description = "Atualiza as despesa no banco de dados", tags = {"Despesa"})
    public ResponseEntity atualizarDespesa(@RequestBody @Valid DespesaDTO despesaDTO, @PathVariable Long id){
        DespesasEntity despesaAtualizada = despesaService.updateDespesa(id, despesaDTO);

        return ResponseEntity.ok(despesaAtualizada);
    }


    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Quando a despesa não é encontrada no banco de dados")
    })
    @Operation(summary = "Deleta a despesas pelo id", description = "deleta a despesa no banco de dados", tags = {"Despesa"})
    public ResponseEntity deletarDespesa(@PathVariable Long id){
        despesaService.deleteDespesa(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/saldo/{metodo}")
    @Operation(summary = "Valor total pelo metodo", description = "somas todas as despesas pelo situacao ex:LAZER", tags = {"Despesa"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quando a despesa não é encontrada no banco de dados")
    })
    public BigDecimal valorDespesaSituacao(@PathVariable SituacaoEnum metodo){
        var result = despesaService.saldoTotal(metodo);
        System.out.println(result);
        return result;
    }
    @GetMapping("/buscar/{mes}-{ano}")
    @Operation(summary = "Lista as despesas pelo mes e ano", description = "Lista todas as despesas do mes e ano 12/2026", tags = {"Despesa"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quando a despesa não é encontrada no banco de dados")
    })
    public List<DespesasEntity> listByMonthYear(@PathVariable Integer mes, @PathVariable Integer ano){
        var result = despesaService.despesaMes(ano,mes);
        return result;
    }

    @GetMapping("/relatorio-mensal-categoria/{mes}-{ano}")
    @Operation(summary = "Relatorio Mensal das despesas", description = "Relatorio das despesas", tags = {"Despesa"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quando a despesa não é encontrada no banco de dados")
    })
    public List<TotalPorCategoria> relatorioMensal(@PathVariable Integer mes, @PathVariable Integer ano){
        return ResponseEntity.ok(despesaService.relatorioMensal(mes, ano)).getBody();
    }
    @GetMapping("/comparationbetweentwomonth/{ano}-{mesAnterior}-{ano2}-{mesPosterior}")
    @Operation(summary = "Compara entre um mes e o outro mes", description = "Compara ex: entre o mes 7 e o mes 8", tags = {"Despesa"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quando a despesa não é encontrada no banco de dados")
    })
    public ResponseEntity<List<List<DespesasEntity>>> listBetweenTwoMonth(@PathVariable Integer ano,@PathVariable Integer mesAnterior,@PathVariable Integer ano2,@PathVariable Integer mesPosterior){
        var resultado = despesaService.listBetweenTwoMonthDespesa(ano,mesAnterior,ano2,mesPosterior);
        return ResponseEntity.ok(resultado);
    }
}
