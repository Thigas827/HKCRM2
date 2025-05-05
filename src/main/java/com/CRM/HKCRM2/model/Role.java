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
    private String name; 

    // Construtor padrão
    public Role() { 
    }
    // Construtor com parâmetros
    public Role(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
