package com.truckstopservices.accounting.accountsPayable.service.implementation;
import com.truckstopservices.accounting.accountsPayable.entity.InvoiceEntity;
import com.truckstopservices.accounting.accountsPayable.repository.InvoiceRepository;
import com.truckstopservices.accounting.accountsPayable.service.interfaces.AccountsPayableInterface;
import com.truckstopservices.accounting.model.Invoice;
import com.truckstopservices.accounting.model.InvoiceDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AccountsPayableImplementation implements AccountsPayableInterface {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public AccountsPayableImplementation(InvoiceRepository invoiceRepository) {
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
