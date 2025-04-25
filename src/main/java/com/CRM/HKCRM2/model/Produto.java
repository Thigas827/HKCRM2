package com.CRM.HKCRM2.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;


@Entity(name = "produto") //Define a classe como uma entidade JPA
@Inheritance(strategy = InheritanceType.JOINED) // Define a estratégia de herança para a entidade
public abstract class Produto {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

        private String nome;
        private Double preco;

        // Construtor padrão
        public Produto() {
        }
        // Construtor com parâmetros
        public Produto(Integer id ,String nome, Double preco) {
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
