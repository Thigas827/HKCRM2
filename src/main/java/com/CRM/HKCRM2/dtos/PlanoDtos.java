package com.CRM.HKCRM2.dtos;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record PlanoDtos (
        @NotNull String nome,
        @NotNull Double preco,
        @NotNull String tipoPlano,
        @NotNull LocalDate dataInicio,
        @NotNull LocalDate dataFim,
        @NotNull LocalDate dataVencimento,
        @NotNull Double valorMensalidade
    ) 
{}
