package com.CRM.HKCRM2.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CRM.HKCRM2.model.UsuarioMod;

public interface UIUsuarioRepository extends JpaRepository <UsuarioMod, UUID> {
    Optional<UsuarioMod> findByEmail(String email);
    void deleteByEmail(String email);

}
