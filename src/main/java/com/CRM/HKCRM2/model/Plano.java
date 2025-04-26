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

    // Define o tipo do plano, por exemplo, "Básico", "Premium", etc.
    @Column(name = "tipo_plano", nullable = false)
    // A coluna tipo_plano é obrigatória (não pode ser nula) 
    private String tipoPlano;

    // Representa a data de início da vigência do plano
    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    // Representa a data de término da vigência do plano 
    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    // Representa a data de vencimento do plano
    // A definição da coluna inclui uma configuração padrão para 30 dias após a data atual
    @Column(name = "data_vencimento", columnDefinition = "DATE NOT NULL DEFAULT CURRENT_DATE + INTERVAL '30 days'")
    private LocalDate dataVencimento;

    // Representa o valor mensal a ser pago pelo plano
    @Column(name = "valor_mensalidade", nullable = false)
    private Double valorMensalidade;
    

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Double getValorMensalidade() {
        return valorMensalidade;
    }

    public void setValorMensalidade(Double valorMensalidade) {
        this.valorMensalidade = valorMensalidade;
    }

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
