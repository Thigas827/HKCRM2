package com.CRM.HKCRM2.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "plano")
@PrimaryKeyJoinColumn(name = "id")                  // Define a chave primária da tabela
public class Plano extends Produto {

    @Column(name = "tipo_plano")
    private String tipoPlano;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private LocalDate dataVencimento;
    private Double valorMensalidade;
    

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getTipoPlano() {
        return tipoPlano;
    }

    public void setTipoPlano(String tipoPlano) {
        this.tipoPlano = tipoPlano;
    }
    

}
