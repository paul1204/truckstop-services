package com.truckstopservices.accounting.accountsPayable.service.implementation;

import com.truckstopservices.accounting.accountsPayable.entity.InvoiceEntity;
import com.truckstopservices.accounting.accountsPayable.repository.InvoiceRepository;
import com.truckstopservices.accounting.model.Invoice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AccountsPayableImplementationIntegrationTest {

    @Autowired
    private AccountsPayableImplementation accountsPayableImplementation;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    public void testCreateInvoice_PersistsToDatabase() {
        // Arrange
        String companyName = "Integration Test Company";
        String date = "01/15/2023";
        Double amount = 150.0;

        // Act
        Invoice createdInvoice = accountsPayableImplementation.createInvoice(companyName, date, amount);

        // Assert
        // Verify the returned invoice has the correct data
        assertNotNull(createdInvoice);
        assertNotNull(createdInvoice.invoiceNumber());
        assertEquals(companyName, createdInvoice.invoiceDetails().getCompanyName());
        assertEquals(date, createdInvoice.invoiceDetails().getDate());
        assertEquals(amount, createdInvoice.invoiceDetails().getAmount());

        // Verify the invoice was actually persisted to the database
        Optional<InvoiceEntity> persistedInvoice = invoiceRepository.findById(createdInvoice.invoiceNumber());
        assertTrue(persistedInvoice.isPresent());
        assertEquals(companyName, persistedInvoice.get().getCompanyName());
        assertEquals(date, persistedInvoice.get().getDate());
        assertEquals(amount, persistedInvoice.get().getAmount());
        assertNotNull(persistedInvoice.get().getCreatedAt());
    }

    @Test
    public void testCreateInvoice_WithWhitespace_TrimsCompanyName() {
        // Arrange
        String companyName = "  Company With Whitespace  ";
        String date = "02/15/2023";
        Double amount = 250.0;

        // Act
        Invoice createdInvoice = accountsPayableImplementation.createInvoice(companyName, date, amount);

        // Assert
        // Verify the company name was trimmed
        assertEquals("Company With Whitespace", createdInvoice.invoiceDetails().getCompanyName());

        // Verify the trimmed company name was persisted to the database
        Optional<InvoiceEntity> persistedInvoice = invoiceRepository.findById(createdInvoice.invoiceNumber());
        assertTrue(persistedInvoice.isPresent());
        assertEquals("Company With Whitespace", persistedInvoice.get().getCompanyName());
    }

    @Test
    public void testCreateInvoice_GeneratesUniqueInvoiceNumbers() {
        // Arrange
        String companyName = "Unique Invoice Company";
        String date = "03/15/2023";
        Double amount = 350.0;

        // Act
        Invoice invoice1 = accountsPayableImplementation.createInvoice(companyName, date, amount);
        Invoice invoice2 = accountsPayableImplementation.createInvoice(companyName, date, amount);

        // Assert
        // Verify that each invoice has a unique invoice number
        assertNotEquals(invoice1.invoiceNumber(), invoice2.invoiceNumber());

        // Verify both invoices were persisted to the database
        assertTrue(invoiceRepository.findById(invoice1.invoiceNumber()).isPresent());
        assertTrue(invoiceRepository.findById(invoice2.invoiceNumber()).isPresent());
    }

    @Test
    public void testCreateInvoice_WithMultipleInvoices_AllPersisted() {
        // Arrange
        String[] companyNames = {"Company A", "Company B", "Company C"};
        String[] dates = {"04/15/2023", "04/16/2023", "04/17/2023"};
        Double[] amounts = {450.0, 550.0, 650.0};

        // Act
        Invoice[] invoices = new Invoice[3];
        for (int i = 0; i < 3; i++) {
            invoices[i] = accountsPayableImplementation.createInvoice(companyNames[i], dates[i], amounts[i]);
        }

        // Assert
        // Verify all invoices were persisted to the database
        for (int i = 0; i < 3; i++) {
            Optional<InvoiceEntity> persistedInvoice = invoiceRepository.findById(invoices[i].invoiceNumber());
            assertTrue(persistedInvoice.isPresent());
            assertEquals(companyNames[i], persistedInvoice.get().getCompanyName());
            assertEquals(dates[i], persistedInvoice.get().getDate());
            assertEquals(amounts[i], persistedInvoice.get().getAmount());
        }
    }
}