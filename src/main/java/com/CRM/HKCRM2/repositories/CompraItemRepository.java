package com.CRM.HKCRM2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CRM.HKCRM2.model.CompraItem;
import com.CRM.HKCRM2.model.CompraItemPK;

public interface CompraItemRepository extends JpaRepository<CompraItem, CompraItemPK> {

}
