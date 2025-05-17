package com.CRM.HKCRM2.dtos;

import java.util.UUID;

public class IndicacaoDto {
    private UUID indicadorId;
    private String emailIndicado;

    // Construtores
    public IndicacaoDto() {}

    public IndicacaoDto(UUID indicadorId, String emailIndicado) {
        this.indicadorId = indicadorId;
        this.emailIndicado = emailIndicado;
    }

    // Getters e Setters
    public UUID getIndicadorId() {
        return indicadorId;
    }

    public void setIndicadorId(UUID indicadorId) {
        this.indicadorId = indicadorId;
    }

    public String getEmailIndicado() {
        return emailIndicado;
    }

    public void setEmailIndicado(String emailIndicado) {
        this.emailIndicado = emailIndicado;
    }
}
