package com.teste.mocs.api.client;

import com.teste.mocs.api.model.Cliente;
import com.teste.mocs.api.model.Compra;
import com.teste.mocs.api.model.Produto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MockClient {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${url.clientes}")
    private String urlCliente;

    @Value("${url.produtos}")
    private String urlProdutos;

    public List<Cliente> getClientes() {
        log.info("Get clientes");
        try{
            return Arrays.asList(Objects.requireNonNull(restTemplate.getForObject(urlCliente, Cliente[].class)));
        }catch (Exception e){
            log.error("Error get clientes", e);
            restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
                @Override
                public void handleError(ClientHttpResponse response) throws IOException {
                    if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                        e.getMessage();
                        System.out.println("Recurso não encontrado");
                    }
                    super.handleError(response);
                }
            });

        }
        return new ArrayList<>();
    }

    public List<Produto> getProdutos() {
        log.info("Get produtos");

        try{
            return Arrays.asList(Objects.requireNonNull(restTemplate.getForObject(urlProdutos, Produto[].class)));
        }catch (Exception e){
            log.error("Error get clientes", e);
            restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
                @Override
                public void handleError(ClientHttpResponse response) throws IOException {
                    if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                        e.getMessage();
                        System.out.println("Recurso não encontrado");
                    }
                    super.handleError(response);
                }
            });

        }
        return new ArrayList<>();
    }

    public List<Compra> getCompras() {
        log.info("Get produtos");

        try{
            return Arrays.asList(Objects.requireNonNull(restTemplate.getForObject(urlCliente, Compra[].class)));
        }catch (Exception e){
            log.error("Error get clientes", e);
            restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
                @Override
                public void handleError(ClientHttpResponse response) throws IOException {
                    if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                        e.getMessage();
                        System.out.println("Recurso não encontrado");
                    }
                    super.handleError(response);
                }
            });

        }
        return new ArrayList<>();
    }
}