package com.truckstopservices.accounting.invoice.service.interfaces;

import com.truckstopservices.accounting.model.Invoice;

public interface InvoiceService {
    Invoice createInvoice(String companyName, String date, Double amount);
}