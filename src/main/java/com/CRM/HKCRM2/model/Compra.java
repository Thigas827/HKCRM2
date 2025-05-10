package com.CRM.HKCRM2.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;

/**
 * Entidade que representa uma compra/venda no sistema.
 * 
 * Esta classe mantém as informações principais de uma transação:
 * - ID único da compra
 * - Cliente que realizou a compra
 * - Data e hora da compra
 * - Valor total
 * - Lista de itens comprados
 * 
 * Relacionamentos:
 * - ManyToOne com Usuario (cliente)
 * - OneToMany com CompraItem (itens da compra)
 * 
 * O cascade ALL em itens garante que ao salvar/excluir uma compra,
 * seus itens também sejam salvos/excluídos automaticamente.
 */
@Entity
@Table(name = "compra")
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private UsuarioMod cliente;

    @Column(name = "data_compra", nullable = false)
    private LocalDateTime dataCompra = LocalDateTime.now();

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL)
    private List<CompraItem> itens = new ArrayList<>();

    public Compra() {
    }

    public Compra(UsuarioMod cliente, BigDecimal valorTotal) {
        this.cliente = cliente;
        this.valorTotal = valorTotal;
        this.dataCompra = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UsuarioMod getCliente() {
        return cliente;
    }

    public void setCliente(UsuarioMod cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDateTime dataCompra) {
        this.dataCompra = dataCompra;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal total) {
        this.valorTotal = total;
    }

    public List<CompraItem> getItens() {
        return itens;
    }

    public void setItens(List<CompraItem> itens) {
        this.itens = itens;
    }

}
