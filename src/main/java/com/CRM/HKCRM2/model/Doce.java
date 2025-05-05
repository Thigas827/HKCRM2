package com.CRM.HKCRM2.model;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
// Esta classe representa um "Doce" no sistema. Ela é uma extensão da classe "Produto",
// o que significa que herda as características básicas de um produto, como ID, nome e preço.
// Além disso, ela adiciona informações específicas de um doce, como o sabor, a quantidade disponível
// e a data de compra. A classe também está configurada para ser usada com um banco de dados,
// onde cada doce é armazenado em uma tabela chamada "doce".

@Entity // Define a classe como uma entidade JPA
@Table(name = "doce") // Define o nome da tabela no banco de dados
@PrimaryKeyJoinColumn(name = "id") // Define a chave primária da tabela
public class Doce extends Produto { // A chave primária da tabela Doce é a mesma da tabela Produto. Isso significa
                                    // que a tabela Doce herda a chave primária da tabela Produto

    @Column(name = "quantidade", nullable = false) // Define a coluna quantidade como não nula
    private Integer quantidade;

    @Column(name = "sabor", nullable = false)
    private String sabor;

    @Column(name = "data_compra", columnDefinition = "DATE NOT NULL DEFAULT CURRENT_DATE", insertable = false, updatable = false)
    private LocalDate dataCompra;

    public Doce() {
        super(); // Chama o construtor da classe pai (Produto)
    }

    public Doce(Integer id, String nome, Double preco, String sabor, Integer quantidade, LocalDate dataCompra) {
        // Construtor da classe Doce que recebe parâmetros para inicializar os atributos
        super(id, nome, preco);
        this.sabor = sabor;
        this.quantidade = quantidade;
        this.dataCompra = dataCompra;
    }

    public String getSabor() {
        return sabor;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDate getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDate dataCompra) {
        this.dataCompra = dataCompra;
    }

}
