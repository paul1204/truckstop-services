package com.truckstopservices.accounting.invoice.repository;

import com.truckstopservices.accounting.invoice.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, String> {

}