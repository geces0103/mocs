package com.teste.mocs.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteFielResponse {
    private String nomeCliente;
    private String cpfCliente;
    private Integer totalCompras;
}