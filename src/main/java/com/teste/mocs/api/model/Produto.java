package com.teste.mocs.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {
    private Integer codigo;

    private String tipo_vinho;

    private Double preco;

    private String safra;

    private Integer ano_compra;
}