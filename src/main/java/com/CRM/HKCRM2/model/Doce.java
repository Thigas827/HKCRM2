package com.CRM.HKCRM2.model;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity                                             // Define a classe como uma entidade JPA 
@Table(name = "doce")                               // Define o nome da tabela no banco de dados
@PrimaryKeyJoinColumn(name = "id")                  // Define a chave primária da tabela 
public class Doce extends Produto {                 // A chave primária da tabela Doce é a mesma da tabela Produto. Isso significa que a tabela Doce herda a chave primária da tabela Produto

    private Integer quantidade;       
    private String sabor;

    @Column(name = "data_compra", columnDefinition = "DATE NOT NULL DEFAULT CURRENT_DATE")
    private LocalDate dataCompra;           

    public Doce() {
        super();                                // Chama o construtor da classe pai (Produto)
    }

    public Doce(Integer id, String nome, Double preco, String sabor, Integer quantidade, LocalDate dataCompra) {
        // Construtor da classe Doce que recebe parâmetros para inicializar os atributos
        super(id, nome, preco);                     // Chama o construtor da classe pai (Produto)
        this.sabor = sabor;                         // Inicializa o atributo sabor
        this.quantidade = quantidade;               // Inicializa o atributo quantidade
        this.dataCompra = dataCompra;               // Inicializa o atributo dataCompra
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
