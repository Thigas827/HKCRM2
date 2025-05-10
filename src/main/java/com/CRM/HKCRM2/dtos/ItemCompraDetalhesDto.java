package com.CRM.HKCRM2.dtos;

import java.math.BigDecimal;

public record ItemCompraDetalhesDto(
    String nomeProduto,
    Integer quantidade,
    BigDecimal precoUnitario,
    BigDecimal subtotal
) {}
