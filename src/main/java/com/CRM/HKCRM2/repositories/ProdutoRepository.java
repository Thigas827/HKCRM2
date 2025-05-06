package com.CRM.HKCRM2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CRM.HKCRM2.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    // Aqui você pode adicionar métodos personalizados, se necessário
    // Por exemplo, você pode adicionar métodos para buscar produtos por categoria, etc.

}
