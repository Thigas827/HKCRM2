package com.CRM.HKCRM2.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

/**
 * Classe que representa um produto no sistema.
 * 
 * Esta é a classe base para todos os produtos do sistema. Utiliza herança
 * com estratégia JOINED, permitindo que tipos específicos de produtos (como Doce)
 * possam estender esta classe e adicionar seus próprios atributos.
 * 
 * Atributos básicos:
 * - id: identificador único do produto
 * - nome: nome do produto
 * - preco: preço unitário do produto
 */
@Entity(name = "produto")
@Inheritance(strategy = InheritanceType.JOINED)
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private Double preco;

    // Construtor padrão
    public Produto() {
    }

    // Construtor com parâmetros
    public Produto(Integer id, String nome, Double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
