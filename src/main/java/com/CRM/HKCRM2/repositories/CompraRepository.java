package com.CRM.HKCRM2.repositories;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.CRM.HKCRM2.model.Compra;


public interface CompraRepository extends JpaRepository<Compra, Integer> {
        List<Compra> findByClienteIdOrderByDataCompraDesc(UUID clienteId);
}
