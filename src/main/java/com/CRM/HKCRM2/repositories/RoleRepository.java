package com.CRM.HKCRM2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CRM.HKCRM2.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> { // Interface para o repositório de papéis, estendendo JpaRepository
    // Métodos de consulta para o repositório de papéis
    Role findByNome(String nome); // Método para encontrar um papel pelo nome
    

}
