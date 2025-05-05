package com.CRM.HKCRM2.model;

import jakarta.persistence.*;


@Entity
@Table(name = "role")
public class Role {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome_role", nullable = false, unique = true)
    // Define o nome da role, que não pode ser nulo e deve ser único
    private String nome; 

    // Construtor padrão
    public Role() { 
    }
    // Construtor com parâmetros
    public Role(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
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

    public void setName(String nome) {
        this.nome = nome;
    }

}
