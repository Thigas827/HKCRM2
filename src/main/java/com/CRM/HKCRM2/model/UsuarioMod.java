package com.CRM.HKCRM2.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Entity(name = "usuario")
public class UsuarioMod {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "nome_usuario", nullable = false)
    private String nome;

    @Column(name = "email_usuario", nullable = false, unique = true)
    private String email;

    @Column(name = "senha_usuario", nullable = false)
    private String senha;
    
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
      name = "usuario_role",
      joinColumns = @JoinColumn(name = "usuario_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    private Set<Role> roles = new HashSet<>();

    public UsuarioMod() { // Construtor padrão
    } 


    public UsuarioMod(UUID id, String nome, String email, String senha, LocalDateTime createdAt) { // Construtor com parâmetros
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.createdAt = createdAt;
    }

    public UUID getId() { // Getter para o ID
        return id;
    }
    public void setId(UUID id) { // Setter para o ID
        this.id = id;
    }
    public String getNome() { // Getter para o nome
        return nome;
    }
    public void setNome(String nome) { // Setter para o nome
        this.nome = nome;
    }
    public String getEmail() { // Getter para o email
        return email;
    }
    public void setEmail(String email) { // Setter para o email
        this.email = email;
    }
    public String getSenha() { // Getter para a senha
        return senha;
    }
    public void setSenha(String senha) { // Setter para a senha
        this.senha = senha;
    }
    public LocalDateTime getCreatedAt() { // Getter para a data de criação
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) { // Setter para a data de criação
        this.createdAt = createdAt;
    }

      // Não criar setter para createdAt (é gerado automaticamente)
      public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}

