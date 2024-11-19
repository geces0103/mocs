package com.teste.mocs.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteFielDTO {
    private String nome;
    private String cpf;
    private int quantidadeCompras;
    private double valorTotalCompras;
}