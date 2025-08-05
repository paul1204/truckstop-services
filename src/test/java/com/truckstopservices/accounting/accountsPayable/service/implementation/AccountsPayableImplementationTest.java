package com.truckstopservices.accounting.accountsPayable.service.implementation;

import com.truckstopservices.accounting.accountsPayable.entity.InvoiceEntity;
import com.truckstopservices.accounting.accountsPayable.repository.InvoiceRepository;
import com.truckstopservices.accounting.model.Invoice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AccountsPayableImplementationTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    private AccountsPayableImplementation accountsPayableImplementation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountsPayableImplementation = new AccountsPayableImplementation(invoiceRepository);
    }

    @Test
    void createInvoice_shouldSaveInvoiceToDatabase() {
        // Arrange
        String companyName = "Test Company";
        String date = "01/01/2023";
        Double amount = 100.0;

        // Act
        Invoice result = accountsPayableImplementation.createInvoice(companyName, date, amount);

        // Assert
        assertNotNull(result);
        assertNotNull(result.invoiceNumber());
        assertEquals(companyName, result.invoiceDetails().getCompanyName());
        assertEquals(date, result.invoiceDetails().getDate());
        assertEquals(amount, result.invoiceDetails().getAmount());

        // Verify that the invoice was saved to the database
        ArgumentCaptor<InvoiceEntity> invoiceEntityCaptor = ArgumentCaptor.forClass(InvoiceEntity.class);
        verify(invoiceRepository, times(1)).save(invoiceEntityCaptor.capture());
        
        InvoiceEntity savedEntity = invoiceEntityCaptor.getValue();
        assertEquals(result.invoiceNumber(), savedEntity.getInvoiceNumber());
        assertEquals(companyName, savedEntity.getCompanyName());
        assertEquals(date, savedEntity.getDate());
        assertEquals(amount, savedEntity.getAmount());
        assertNotNull(savedEntity.getCreatedAt());
    }
}