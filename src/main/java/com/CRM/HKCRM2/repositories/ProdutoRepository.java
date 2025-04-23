package com.CRM.HKCRM2.repositories;
import com.CRM.HKCRM2.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

}
