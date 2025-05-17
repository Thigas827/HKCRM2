package com.CRM.HKCRM2.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "indicacao")
public class Indicacao {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "indicador_id", nullable = false)
    private UsuarioMod indicador;

    @Column(name = "email_indicado", nullable = false)
    private String emailIndicado;

    @Column(name = "data_indicacao", nullable = false)
    private LocalDate dataIndicacao;

    @Column(name = "data_conversao")
    private LocalDate dataConversao;

    @Column(name = "status", nullable = false)
    private String status; // PENDENTE, CONVERTIDA, CANCELADA

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UsuarioMod getIndicador() {
        return indicador;
    }

    public void setIndicador(UsuarioMod indicador) {
        this.indicador = indicador;
    }

    public String getEmailIndicado() {
        return emailIndicado;
    }

    public void setEmailIndicado(String emailIndicado) {
        this.emailIndicado = emailIndicado;
    }

    public LocalDate getDataIndicacao() {
        return dataIndicacao;
    }

    public void setDataIndicacao(LocalDate dataIndicacao) {
        this.dataIndicacao = dataIndicacao;
    }

    public LocalDate getDataConversao() {
        return dataConversao;
    }

    public void setDataConversao(LocalDate dataConversao) {
        this.dataConversao = dataConversao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
