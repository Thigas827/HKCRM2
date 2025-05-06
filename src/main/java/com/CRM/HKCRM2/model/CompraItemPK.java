package com.CRM.HKCRM2.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class CompraItemPK implements Serializable {
    private Integer compraId;
    private Integer produtoId;


    public CompraItemPK() {

    }

    public CompraItemPK(Integer compraId, Integer produtoId) {
        this.compraId = compraId;
        this.produtoId = produtoId;
    }

    // equals e hashCode — essenciais para chaves compostas
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompraItemPK that = (CompraItemPK) o;
        return Objects.equals(compraId, that.compraId) &&
               Objects.equals(produtoId, that.produtoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compraId, produtoId);
    }

    // Getters e setters
    public Integer getCompraId() {
        return compraId;
    }
    public void setCompraId(Integer compraId) {
        this.compraId = compraId;
    }
    public Integer getProdutoId() {
        return produtoId;
    }
    public void setProdutoId(Integer produtoId) {
        this.produtoId = produtoId;
    }
}