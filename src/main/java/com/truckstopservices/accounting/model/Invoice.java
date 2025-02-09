package com.truckstopservices.accounting.model;

import java.util.ArrayList;


public record Invoice(String invoiceNumber, InvoiceDetail invoiceDetails) {
}
