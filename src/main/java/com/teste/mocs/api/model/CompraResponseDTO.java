package com.teste.mocs.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraResponseDTO {
    private String nomeCliente;
    private String cpfCliente;
    private Produto produto;
    private Integer quantidade;
    private Double valorTotal;
}