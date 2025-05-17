package com.CRM.HKCRM2.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

/**
 * Entidade que representa um usuário/cliente no sistema.
 * 
 * Características principais:
 * - Usa UUID como identificador único
 * - Armazena informações básicas do cliente (nome, email, etc.)
 * - Mantém controle de papéis/roles para autorização
 * - Registra data de criação automaticamente
 * 
 * Campos importantes:
 * - id: UUID gerado automaticamente
 * - nome: nome do usuário/cliente
 * - email: email único para identificação
 * - senha: senha criptografada
 * - endereco: endereço do cliente
 * - telefone: telefone para contato
 * - roles: conjunto de papéis/permissões do usuário
 * 
 * Segurança:
 * - Email deve ser único no sistema
 * - Senha é armazenada de forma criptografada
 * - Roles permitem controle de acesso granular
 */
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
    
    @Column(name = "endereco")
    private String endereco;

    @Column(name = "telefone")
    private String telefone;

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


    public UsuarioMod(UUID id, String nome, String email, String senha, LocalDateTime createdAt, String endereco, String telefone) { // Construtor com parâmetros
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.createdAt = createdAt;
        this.endereco = endereco;
        this.telefone = telefone;
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

    public String getEndereco() { // Getter para o endereço
        return endereco;
    }
    public void setEndereco(String endereco) { // Setter para o endereço
        this.endereco = endereco;
    }
    public String getTelefone() { // Getter para o telefone
        return telefone;
    }
    public void setTelefone(String telefone) { // Setter para o telefone
        this.telefone = telefone;
    }

      // Não criar setter para createdAt (é gerado automaticamente)
      public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}

