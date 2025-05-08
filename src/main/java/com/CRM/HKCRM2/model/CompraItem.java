package com.CRM.HKCRM2.model;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import com.CRM.HKCRM2.model.CompraItemPK;


@Entity
@Table(name = "compra_item")
public class CompraItem {

    @EmbeddedId
    private CompraItemPK id;

    @ManyToOne @MapsId("compraId")
    @JoinColumn(name = "compra_id")
    private Compra compra;

    @ManyToOne @MapsId("produtoId")
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "preco_unit", nullable = false)
    private BigDecimal precoUnit;

    // Construtor padrão para JPA
    public CompraItem() {}

    // Construtor para uso no serviço
    public CompraItem(Compra compra, Produto produto, Integer quantidade, BigDecimal precoUnit) {
        this.id = new CompraItemPK(compra.getId(), produto.getId());
        this.compra = compra;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnit = precoUnit;
    }

    // Getters e setters
    public CompraItemPK getId() {
        return id;
    }
    public void setId(CompraItemPK id) {
        this.id = id;
    }
    public Compra getCompra() {
        return compra;
    }
    public void setCompra(Compra compra) {
        this.compra = compra;
    }
    public Produto getProduto() {
        return produto;
    }
    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    public Integer getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
    public BigDecimal getPrecoUnit() {
        return precoUnit;
    }
    public void setPrecoUnit(BigDecimal precoUnit) {
        this.precoUnit = precoUnit;
    }

    public String getProdutoNome() {
        return produto != null ? produto.getNome() : "";
    }
}
