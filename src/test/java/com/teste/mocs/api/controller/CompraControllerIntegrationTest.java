package com.teste.mocs.api.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste.mocs.api.model.*;
import com.teste.mocs.api.service.CompraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompraController.class)
class CompraControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompraService compraService;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente clienteMock;
    private Produto produtoMock;

    @BeforeEach
    void setup() {
        produtoMock = new Produto(1, "Tinto", 150.0, "2018", 2021);
        clienteMock = new Cliente("96718391344", "Ian Joaquim Giovanni Santos",
                List.of(new Compra("1", 2))); // 2 * 150 = 300
    }

    @Test
    void listarComprasOrdenadas_deveRetornarListaDeCompras() throws Exception {
        CompraDetalhadaDTO compraDetalhada = new CompraDetalhadaDTO(
                clienteMock.getNome(),
                clienteMock.getCpf(),
                produtoMock,
                2,
                300.0
        );

        when(compraService.listarComprasOrdenadas()).thenReturn(List.of(compraDetalhada));

        mockMvc.perform(get("/api/compras")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].nomeCliente").value(clienteMock.getNome()))
                .andExpect(jsonPath("$[0].valorTotal").value(300.0));
    }

    @Test
    void maiorCompraPorAno_deveRetornarMaiorCompra() throws Exception {
        CompraDetalhadaDTO maiorCompra = new CompraDetalhadaDTO(
                clienteMock.getNome(),
                clienteMock.getCpf(),
                produtoMock,
                2,
                300.0
        );

        when(compraService.maiorCompraPorAno(2020)).thenReturn(maiorCompra);

        mockMvc.perform(get("/api/maior-compra/2020")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCliente").value(clienteMock.getNome()))
                .andExpect(jsonPath("$.valorTotal").value(300.0));
    }

    @Test
    void clientesMaisFieis_deveRetornarTop3Clientes() throws Exception {
        ClienteFielDTO clienteFiel = new ClienteFielDTO(
                clienteMock.getNome(),
                clienteMock.getCpf(),
                5,
                1500.0
        );

        when(compraService.clientesMaisFieis()).thenReturn(List.of(clienteFiel));

        mockMvc.perform(get("/api/clientes-fieis")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].nome").value(clienteMock.getNome()))
                .andExpect(jsonPath("$[0].valorTotalCompras").value(1500.0));
    }

    @Test
    void recomendacaoPorClienteETipo_deveRetornarRecomendacao() throws Exception {
        RecomendacaoDTO recomendacao = new RecomendacaoDTO("Tinto");

        when(compraService.recomendacaoPorClienteETipo("96718391344", "Tinto"))
                .thenReturn(recomendacao);

        mockMvc.perform(get("/api/recomendacao/96718391344/Tinto")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoVinho").value("Tinto"));
    }
}