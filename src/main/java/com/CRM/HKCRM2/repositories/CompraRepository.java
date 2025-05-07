package com.CRM.HKCRM2.repositories;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.CRM.HKCRM2.model.Compra;


public interface CompraRepository extends JpaRepository<Compra, Integer> {
        List<Compra> findByClienteIdOrderByDataCompraDesc(Integer clienteId);
    // Aqui você pode adicionar métodos personalizados, se necessário
    // Por exemplo, você pode adicionar métodos para buscar compras por cliente, etc.

}
