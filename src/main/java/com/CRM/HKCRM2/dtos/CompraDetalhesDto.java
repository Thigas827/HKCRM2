package com.CRM.HKCRM2.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CompraDetalhesDto(
    Integer id,
    LocalDateTime dataCompra,
    BigDecimal valorTotal,
    List<ItemCompraDetalhesDto> itens
) {}
