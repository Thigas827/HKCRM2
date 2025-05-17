package com.CRM.HKCRM2.repositories;

import com.CRM.HKCRM2.model.Indicacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IndicacaoRepository extends JpaRepository<Indicacao, UUID> {
    List<Indicacao> findByIndicadorIdOrderByDataIndicacaoDesc(UUID indicadorId);
    Optional<Indicacao> findByEmailIndicadoAndStatus(String emailIndicado, String status);
}
