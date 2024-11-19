package com.teste.mocs.api.service;


import com.teste.mocs.api.client.MockClient;
import com.teste.mocs.api.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompraServiceTest {

    @InjectMocks
    private CompraService compraService;

    @Mock
    private MockClient mockClient;

    private Produto produtoMock;
    private Cliente clienteMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configurar mocks
        produtoMock = new Produto(1, "Tinto", 150.0, "2018", 2020);
        clienteMock = new Cliente(
                "96718391344",
                "Ian Joaquim Giovanni Santos",
                List.of(new Compra("1", 2)) // Compra: 2 * 150.0 = 300.0
        );
    }

    @Test
    void listarComprasOrdenadas_deveRetornarListaOrdenada() {
        when(mockClient.getClientes()).thenReturn(List.of(clienteMock));
        when(mockClient.getProdutos()).thenReturn(List.of(produtoMock));

        List<CompraDetalhadaDTO> resultado = compraService.listarComprasOrdenadas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Ian Joaquim Giovanni Santos", resultado.get(0).getNomeCliente());
        assertEquals(300.0, resultado.get(0).getValorTotal());
        verify(mockClient, times(1)).getClientes();
        verify(mockClient, times(1)).getProdutos();
    }


    @Test
    void maiorCompraPorAno_deveRetornarNullSeNenhumaCompraNoAno() {
        when(mockClient.getClientes()).thenReturn(List.of(clienteMock));
        when(mockClient.getProdutos()).thenReturn(List.of(produtoMock));

        CompraDetalhadaDTO maiorCompra = compraService.maiorCompraPorAno(2022);

        assertNull(maiorCompra);
        verify(mockClient, times(1)).getClientes();
        verify(mockClient, times(1)).getProdutos();
    }

    @Test
    void recomendacaoPorClienteETipo_deveRetornarTipoMaisComprado() {
        when(mockClient.getClientes()).thenReturn(List.of(clienteMock));
        when(mockClient.getProdutos()).thenReturn(List.of(produtoMock));

        RecomendacaoDTO recomendacao = compraService.recomendacaoPorClienteETipo("96718391344", "Tinto");

        assertNotNull(recomendacao);
        assertEquals("Tinto", recomendacao.getTipoVinho());
        verify(mockClient, times(1)).getClientes();
        verify(mockClient, times(1)).getProdutos();
    }
    
    @Test
    void buscarProdutoPorCodigo_deveRetornarProduto() {
        when(mockClient.getProdutos()).thenReturn(List.of(produtoMock));

        Produto produto = compraService.buscarProdutoPorCodigo("1");

        assertNotNull(produto);
        assertEquals(1, produto.getCodigo());
        assertEquals("Tinto", produto.getTipo_vinho());
        verify(mockClient, times(1)).getProdutos();
    }

    @Test
    void buscarProdutoPorCodigo_deveLancarExcecaoParaCodigoInvalido() {
        when(mockClient.getProdutos()).thenReturn(List.of(produtoMock));

        assertThrows(RuntimeException.class, () -> compraService.buscarProdutoPorCodigo("999"));
        verify(mockClient, times(1)).getProdutos();
    }

    @Test
    void buscarClientePorCpf_deveLancarExcecaoParaCpfInvalido() {
        when(mockClient.getClientes()).thenReturn(List.of(clienteMock));

        assertThrows(RuntimeException.class, () -> compraService.buscarClientePorCpf("99999999999"));
        verify(mockClient, times(1)).getClientes();
    }

    @Test
    void clientesMaisFieis_deveRetornarListaVaziaQuandoNaoHouverClientes() {
        when(mockClient.getClientes()).thenReturn(List.of());

        List<ClienteFielDTO> clientesFieis = compraService.clientesMaisFieis();

        assertEquals(0, clientesFieis.size());
    }


}