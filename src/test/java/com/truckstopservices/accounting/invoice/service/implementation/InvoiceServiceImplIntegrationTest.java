package com.truckstopservices.accounting.invoice.service.implementation;

import com.truckstopservices.accounting.invoice.entity.InvoiceEntity;
import com.truckstopservices.accounting.invoice.repository.InvoiceRepository;
import com.truckstopservices.accounting.model.Invoice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class InvoiceServiceImplIntegrationTest {

    @Autowired
    private InvoiceServiceImpl invoiceService;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    public void testCreateInvoice() {
        // Arrange
        String companyName = "Test Company";
        String date = "01/01/2023";
        Double amount = 100.0;

        // Act
        Invoice createdInvoice = invoiceService.createInvoice(companyName, date, amount);

        // Assert
        assertNotNull(createdInvoice);
        assertNotNull(createdInvoice.invoiceNumber());
        assertEquals(companyName, createdInvoice.invoiceDetails().getCompanyName());
        assertEquals(date, createdInvoice.invoiceDetails().getDate());
        assertEquals(amount, createdInvoice.invoiceDetails().getAmount());

        // Verify the invoice was saved to the database
        Optional<InvoiceEntity> persistedInvoice = invoiceRepository.findById(createdInvoice.invoiceNumber());
        assertTrue(persistedInvoice.isPresent());
        assertEquals(companyName, persistedInvoice.get().getCompanyName());
        assertEquals(date, persistedInvoice.get().getDate());
        assertEquals(amount, persistedInvoice.get().getAmount());
    }

    @Test
    public void testCreateInvoiceWithWhitespace() {
        // Arrange
        String companyName = "  Company With Whitespace  ";
        String date = "02/01/2023";
        Double amount = 200.0;

        // Act
        Invoice createdInvoice = invoiceService.createInvoice(companyName, date, amount);

        // Assert
        assertNotNull(createdInvoice);
        assertEquals("Company With Whitespace", createdInvoice.invoiceDetails().getCompanyName());

        // Verify the invoice was saved to the database with trimmed company name
        Optional<InvoiceEntity> persistedInvoice = invoiceRepository.findById(createdInvoice.invoiceNumber());
        assertTrue(persistedInvoice.isPresent());
        assertEquals("Company With Whitespace", persistedInvoice.get().getCompanyName());
    }

    @Test
    public void testCreateMultipleInvoices() {
        // Arrange
        String[] companyNames = {"Company A", "Company B", "Company C"};
        String[] dates = {"03/01/2023", "04/01/2023", "05/01/2023"};
        Double[] amounts = {300.0, 400.0, 500.0};
        Invoice[] invoices = new Invoice[3];

        // Act
        for (int i = 0; i < 3; i++) {
            invoices[i] = invoiceService.createInvoice(companyNames[i], dates[i], amounts[i]);
        }

        // Assert
        for (int i = 0; i < 3; i++) {
            assertNotNull(invoices[i]);
            assertEquals(companyNames[i], invoices[i].invoiceDetails().getCompanyName());
            assertEquals(dates[i], invoices[i].invoiceDetails().getDate());
            assertEquals(amounts[i], invoices[i].invoiceDetails().getAmount());

            // Verify each invoice was saved to the database
            Optional<InvoiceEntity> persistedInvoice = invoiceRepository.findById(invoices[i].invoiceNumber());
            assertTrue(persistedInvoice.isPresent());
            assertEquals(companyNames[i], persistedInvoice.get().getCompanyName());
            assertEquals(dates[i], persistedInvoice.get().getDate());
            assertEquals(amounts[i], persistedInvoice.get().getAmount());
        }
    }
}