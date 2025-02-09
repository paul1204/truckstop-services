package com.truckstopservices.accounting.accountsPayable.service.interfaces;

import com.truckstopservices.accounting.model.Invoice;

public interface AccountsPayableInterface {
        Invoice createInvoice(String companyName, String date, Double Amount);
}
