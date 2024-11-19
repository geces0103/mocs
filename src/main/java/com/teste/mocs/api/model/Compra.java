package com.teste.mocs.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Compra {

    private String codigo;

    private Integer quantidade;
}
