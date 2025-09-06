package com.truckstopservices.accounting.invoice.service.implementation;

import com.truckstopservices.accounting.invoice.entity.InvoiceEntity;
import com.truckstopservices.accounting.invoice.repository.InvoiceRepository;
import com.truckstopservices.accounting.invoice.service.interfaces.InvoiceService;
import com.truckstopservices.accounting.model.Invoice;
import com.truckstopservices.accounting.model.InvoiceDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Transactional
    @Override
    public Invoice createInvoice(String companyName, String date, Double amount) {
        String invoiceNumber = UUID.randomUUID().toString();

        // Create and save the invoice entity to the database
        InvoiceEntity invoiceEntity = new InvoiceEntity(
            invoiceNumber,
            companyName.trim(),
            date,
            amount
        );
        invoiceRepository.save(invoiceEntity);

        // Return the invoice object for the API response
        return new Invoice(invoiceNumber, new InvoiceDetail(companyName.trim(), date, amount));
    }
}