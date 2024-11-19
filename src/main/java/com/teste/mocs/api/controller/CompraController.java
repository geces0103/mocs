package com.teste.mocs.api.controller;

import com.teste.mocs.api.model.*;
import com.teste.mocs.api.service.CompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class CompraController {

    private final CompraService compraService;

    @Operation(summary = "Listar compras ordenadas por valor", description = "Retorna uma lista de compras ordenadas pelo valor total.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/compras")
    public ResponseEntity<List<CompraDetalhadaDTO>> listarComprasOrdenadas() {
        log.info("Listando compras ordenadas por valor.");
        List<CompraDetalhadaDTO> compras = compraService.listarComprasOrdenadas();
        return ResponseEntity.ok(compras);
    }

    @Operation(summary = "Maior compra de um ano específico", description = "Retorna a maior compra realizada no ano especificado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Compra encontrada"),
            @ApiResponse(responseCode = "400", description = "Erro ao buscar compra para o ano especificado")
    })
    @GetMapping("/maior-compra/{ano}")
    public ResponseEntity<CompraDetalhadaDTO> maiorCompraPorAno(
            @Parameter(description = "Ano da compra") @PathVariable Integer ano) {
        log.info("Buscando maior compra do ano: {}", ano);
        try {
            CompraDetalhadaDTO maiorCompra = compraService.maiorCompraPorAno(ano);
            return ResponseEntity.ok(maiorCompra);
        } catch (Exception e) {
            log.error("Erro ao buscar maior compra para o ano {}: {}", ano, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Top 3 clientes mais fiéis", description = "Retorna os 3 clientes mais fiéis com base no número de compras e valor total.")
    @ApiResponse(responseCode = "200", description = "Clientes mais fiéis encontrados")
    @GetMapping("/clientes-fieis")
    public ResponseEntity<List<ClienteFielDTO>> clientesMaisFieis() {
        log.info("Buscando os Top 3 clientes mais fiéis.");
        List<ClienteFielDTO> clientesFieis = compraService.clientesMaisFieis();
        return ResponseEntity.ok(clientesFieis);
    }

    @Operation(summary = "Recomendação de vinho", description = "Retorna a recomendação de vinho para o cliente com base no histórico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recomendação gerada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Cliente não encontrado ou erro na recomendação")
    })
    @GetMapping("/recomendacao/{cpf}/{tipo}")
    public ResponseEntity<RecomendacaoDTO> recomendacaoPorClienteETipo(
            @Parameter(description = "CPF do cliente") @PathVariable String cpf,
            @Parameter(description = "Tipo de vinho preferido") @PathVariable String tipo) {
        log.info("Gerando recomendação para CPF: {} e Tipo: {}", cpf, tipo);
        try {
            RecomendacaoDTO recomendacao = compraService.recomendacaoPorClienteETipo(cpf, tipo);
            return ResponseEntity.ok(recomendacao);
        } catch (RuntimeException e) {
            log.error("Erro ao gerar recomendação para CPF: {} - {}", cpf, e.getMessage());
            return ResponseEntity.badRequest().body(new RecomendacaoDTO("Cliente não encontrado ou erro na recomendação."));
        }
    }
}
