package com.CRM.HKCRM2.repositories;

import com.CRM.HKCRM2.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    boolean existsByEmail(String email);
}
