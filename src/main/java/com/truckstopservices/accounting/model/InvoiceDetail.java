package com.truckstopservices.accounting.model;

public class InvoiceDetail {
    String companyName;
    String date;
    Double amount;

    public InvoiceDetail(String companyName, String date, Double amount) {
        this.companyName = companyName;
        this.date = date;
        this.amount = amount;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDate() {
        return date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
