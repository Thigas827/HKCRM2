package com.CRM.HKCRM2.dtos;

import jakarta.validation.constraints.NotNull;

public record ProdutoDto(
    @NotNull String nome,
    @NotNull Double preco
) {}
