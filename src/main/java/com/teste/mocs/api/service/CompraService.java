package com.teste.mocs.api.service;

import com.teste.mocs.api.client.MockClient;
import com.teste.mocs.api.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompraService {

    private final MockClient mockClient;

    public List<CompraDetalhadaDTO> listarComprasOrdenadas() {
        log.info("Iniciando a listagem de compras ordenadas por valor.");
        List<CompraDetalhadaDTO> compras = mockClient.getClientes().stream()
                .flatMap(cliente -> cliente.getCompras().stream()
                        .map(compra -> {
                            Produto produto = buscarProdutoPorCodigo(compra.getCodigo());
                            return new CompraDetalhadaDTO(
                                    cliente.getNome(),
                                    cliente.getCpf(),
                                    produto,
                                    compra.getQuantidade(),
                                    produto.getPreco() * compra.getQuantidade()
                            );
                        }))
                .sorted(Comparator.comparingDouble(CompraDetalhadaDTO::getValorTotal))
                .toList();

        log.info("Listagem de compras concluída. Total de compras processadas: {}", compras.size());
        return compras;
    }

    public CompraDetalhadaDTO maiorCompraPorAno(Integer ano) {
        log.info("Buscando maior compra para o ano: {}", ano);
        CompraDetalhadaDTO maiorCompra = mockClient.getClientes().stream()
                .flatMap(cliente -> cliente.getCompras().stream()
                        .filter(compra -> {
                            Produto produto = buscarProdutoPorCodigo(compra.getCodigo());
                            return produto.getAno_compra() != null && produto.getAno_compra().equals(ano);
                        })
                        .map(compra -> criarCompraDetalhadaDTO(cliente, compra)))
                .max(Comparator.comparing(CompraDetalhadaDTO::getValorTotal))
                .orElse(null);

        if (maiorCompra != null) {
            log.info("Maior compra encontrada: Cliente: {}, Valor Total: {}", maiorCompra.getNomeCliente(), maiorCompra.getValorTotal());
        } else {
            log.warn("Nenhuma compra encontrada para o ano: {}", ano);
        }
        return maiorCompra;
    }

    public List<ClienteFielDTO> clientesMaisFieis() {
        log.info("Buscando os 3 clientes mais fiéis.");

        return mockClient.getClientes().stream()
                .map(cliente -> {
                    double valorTotalCompras = cliente.getCompras().stream()
                            .mapToDouble(compra -> {
                                Produto produto = buscarProdutoPorCodigo(compra.getCodigo());
                                return produto.getPreco() * compra.getQuantidade();
                            })
                            .sum();

                    return new ClienteFielDTO(
                            cliente.getNome(),
                            cliente.getCpf(),
                            cliente.getCompras().size(),
                            valorTotalCompras
                    );
                })
                .sorted((c1, c2) -> Double.compare(c2.getValorTotalCompras(), c1.getValorTotalCompras()))
                .limit(3)
                .collect(Collectors.toList());
    }

    public RecomendacaoDTO recomendacaoPorClienteETipo(String cpf, String tipo) {
        log.info("Gerando recomendação de vinho para o cliente com CPF: {} e tipo de vinho: {}", cpf, tipo);
        Cliente cliente = buscarClientePorCpf(cpf);

        Map<String, Long> tiposMaisComprados = cliente.getCompras().stream()
                .map(compra -> buscarProdutoPorCodigo(compra.getCodigo()).getTipo_vinho())
                .collect(Collectors.groupingBy(t -> t, Collectors.counting()));

        String tipoMaisComprado = tiposMaisComprados.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        log.info("Recomendação gerada para o cliente {}: Tipo mais comprado: {}", cliente.getNome(), tipoMaisComprado);
        return new RecomendacaoDTO(tipoMaisComprado);
    }

    public Produto buscarProdutoPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            log.error("Código do produto está vazio ou nulo!");
            throw new IllegalArgumentException("Código do produto não pode ser nulo ou vazio!");
        }

        log.debug("Buscando produto com código: {}", codigo);

        return mockClient.getProdutos().stream()
                .filter(produto -> produto.getCodigo().equals(Integer.parseInt(codigo)))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Produto com código {} não encontrado!", codigo);
                    return new RuntimeException("Produto não encontrado!");
                });
    }

    public Cliente buscarClientePorCpf(String cpf) {
        log.debug("Buscando cliente com CPF: {}", cpf);
        return mockClient.getClientes().stream()
                .filter(cliente -> cliente.getCpf().equals(cpf))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Cliente com CPF {} não encontrado!", cpf);
                    return new RuntimeException("Cliente não encontrado!");
                });
    }

    private double calcularValorTotalCompra(Compra compra) {
        Produto produto = buscarProdutoPorCodigo(compra.getCodigo());
        double valorTotal = produto.getPreco() * compra.getQuantidade();
        log.debug("Calculando valor total da compra. Produto: {}, Quantidade: {}, Valor Total: {}", produto.getTipo_vinho(), compra.getQuantidade(), valorTotal);
        return valorTotal;
    }

    private CompraDetalhadaDTO criarCompraDetalhadaDTO(Cliente cliente, Compra compra) {
        Produto produto = buscarProdutoPorCodigo(compra.getCodigo());
        double valorTotal = calcularValorTotalCompra(compra);
        log.debug("Criando DTO da compra detalhada para Cliente: {}, Produto: {}, Valor Total: {}", cliente.getNome(), produto.getTipo_vinho(), valorTotal);
        return new CompraDetalhadaDTO(
                cliente.getNome(),
                cliente.getCpf(),
                produto,
                compra.getQuantidade(),
                valorTotal
        );
    }
}
