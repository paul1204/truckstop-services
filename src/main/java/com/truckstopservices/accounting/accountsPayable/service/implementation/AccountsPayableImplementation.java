package com.truckstopservices.accounting.accountsPayable.service.implementation;
import com.truckstopservices.accounting.accountsPayable.service.interfaces.AccountsPayableInterface;
import com.truckstopservices.accounting.model.Invoice;
import com.truckstopservices.accounting.model.InvoiceDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AccountsPayableImplementation implements AccountsPayableInterface {
    @Transactional
    @Override
    public Invoice createInvoice(String companyName, String date, Double amount) {
        String invoiceNumber = UUID.randomUUID().toString();
        return new Invoice(invoiceNumber, new InvoiceDetail(companyName.trim(), date, amount));
    }
}
