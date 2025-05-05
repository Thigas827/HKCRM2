package com.CRM.HKCRM2.model;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

// Classe que representa um plano de assinatura, com informações como tipo, datas de vigência
// e valor mensal, mapeada para a tabela "plano" no banco de dados.
// A classe herda de Produto, indicando que é um tipo específico de produto

@Entity
@Table(name = "plano")
@PrimaryKeyJoinColumn(name = "id") // Define a chave primária da tabela
public class Plano extends Produto {

    // Define o tipo do plano, por exemplo, "Básico", "Premium", etc.
    @Column(name = "tipo_plano", nullable = false) // (não pode ser nula)
    private String tipoPlano;

    // Representa a data de início da vigência do plano
    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    // Representa a data de término da vigência do plano
    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    // Representa a data de vencimento do plano
    // A definição da coluna inclui uma configuração padrão para 30 dias após a data
    // atual
    @Column(name = "data_vencimento", columnDefinition = "DATE NOT NULL DEFAULT CURRENT_DATE + INTERVAL '30 days'")
    private LocalDate dataVencimento;

    // Representa o valor mensal a ser pago pelo plano
    @Column(name = "valor_mensalidade", nullable = false)
    private Double valorMensalidade;

    // Construtor vazio da classe Plano
    public Plano() {
        super();
    }

    // Construtor da classe Plano
    public Plano(Integer id, String nome, Double preco, String tipoPlano, LocalDate dataInicio, LocalDate dataFim,
            LocalDate dataVencimento, Double valorMensalidade) {
        super(id, nome, preco); // Chama o construtor da classe pai (Produto)
        this.tipoPlano = tipoPlano;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.dataVencimento = dataVencimento;
        this.valorMensalidade = valorMensalidade;
    }

    // Getters e Setters para acessar e modificar os atributos da classe
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
