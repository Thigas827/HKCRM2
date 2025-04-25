package com.CRM.HKCRM2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity // Define a classe como uma entidade JPA 
@Table(name = "doce") // Define o nome da tabela no banco de dados
public class Doce extends Produto { // A classe Doce herda da classe Produto
    
    private String categoria; // Atributo específico da classe Doce
    private String sabor; // Atributo específico da classe Doce

    public Doce() {
        super(); // Chama o construtor da classe pai (Produto)
    }

    public Doce(Integer id, String nome, Double preco, String sabor) {
        super(id, nome, preco); // Chama o construtor da classe pai (Produto)
        this.sabor = sabor; // Inicializa o atributo sabor
    }

    public String getSabor() {
        return sabor; // Retorna o sabor do doce
    }

    public void setSabor(String sabor) {
        this.sabor = sabor; // Define o sabor do doce
    }
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

}
