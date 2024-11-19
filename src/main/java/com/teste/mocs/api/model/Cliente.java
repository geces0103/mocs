package com.teste.mocs.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Cliente {

    private String cpf;

    private String nome;

    private List<Compra> compras;
}
