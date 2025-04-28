package com.CRM.HKCRM2.dtos;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;


public record DoceDtos(
        @NotNull String nome,
        @NotNull Double preco,
        @NotNull String sabor,
        @NotNull Integer quantidade,
        LocalDate dataCompra    
        ) 
{}
