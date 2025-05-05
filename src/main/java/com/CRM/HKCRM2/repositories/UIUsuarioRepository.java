package com.CRM.HKCRM2.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CRM.HKCRM2.model.UsuarioMod;

public interface UIUsuarioRepository extends JpaRepository <UsuarioMod, UUID> { // Interface para o repositório de usuários, estendendo JpaRepository
    // Métodos de consulta para o repositório de usuários
    UsuarioMod findByEmail(String email); // Método para encontrar um usuário pelo email
    void deleteByEmail(String email); // Método para deletar um usuário pelo email

}
