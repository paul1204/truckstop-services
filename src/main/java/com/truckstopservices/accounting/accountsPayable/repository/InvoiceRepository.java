package com.truckstopservices.accounting.accountsPayable.repository;

import com.truckstopservices.accounting.accountsPayable.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, String> {

}