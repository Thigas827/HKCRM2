package com.CRM.HKCRM2.dtos;

import java.util.List;
import java.util.UUID;

public record HistoricoComprasDto(
    UsuarioDtos cliente,
    List<CompraDetalhesDto> compras
) {}