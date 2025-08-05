package com.truckstopservices.accounting.accountsPayable.repository;

import com.truckstopservices.accounting.accountsPayable.entity.InvoiceEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class InvoiceRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    public void testSaveInvoice() {
        // Create a test invoice
        InvoiceEntity invoice = new InvoiceEntity(
                "INV-001",
                "Test Company",
                "01/01/2023",
                100.0
        );

        // Save the invoice
        InvoiceEntity savedInvoice = invoiceRepository.save(invoice);

        // Verify the invoice was saved with the correct data
        assertNotNull(savedInvoice);
        assertEquals("INV-001", savedInvoice.getInvoiceNumber());
        assertEquals("Test Company", savedInvoice.getCompanyName());
        assertEquals("01/01/2023", savedInvoice.getDate());
        assertEquals(100.0, savedInvoice.getAmount());
        assertNotNull(savedInvoice.getCreatedAt());
    }

    @Test
    public void testFindInvoiceById() {
        // Create and persist a test invoice
        InvoiceEntity invoice = new InvoiceEntity(
                "INV-002",
                "Another Company",
                "02/01/2023",
                200.0
        );
        entityManager.persist(invoice);
        entityManager.flush();

        // Find the invoice by ID
        Optional<InvoiceEntity> foundInvoice = invoiceRepository.findById("INV-002");

        // Verify the invoice was found with the correct data
        assertTrue(foundInvoice.isPresent());
        assertEquals("INV-002", foundInvoice.get().getInvoiceNumber());
        assertEquals("Another Company", foundInvoice.get().getCompanyName());
        assertEquals("02/01/2023", foundInvoice.get().getDate());
        assertEquals(200.0, foundInvoice.get().getAmount());
    }

    @Test
    public void testFindAllInvoices() {
        // Create and persist multiple test invoices
        InvoiceEntity invoice1 = new InvoiceEntity(
                "INV-003",
                "Company A",
                "03/01/2023",
                300.0
        );
        InvoiceEntity invoice2 = new InvoiceEntity(
                "INV-004",
                "Company B",
                "04/01/2023",
                400.0
        );
        entityManager.persist(invoice1);
        entityManager.persist(invoice2);
        entityManager.flush();

        // Find all invoices
        List<InvoiceEntity> invoices = invoiceRepository.findAll();

        // Verify all invoices were found
        assertFalse(invoices.isEmpty());
        assertTrue(invoices.stream().anyMatch(inv -> inv.getInvoiceNumber().equals("INV-003")));
        assertTrue(invoices.stream().anyMatch(inv -> inv.getInvoiceNumber().equals("INV-004")));
    }

    @Test
    public void testUpdateInvoice() {
        // Create and persist a test invoice
        InvoiceEntity invoice = new InvoiceEntity(
                "INV-005",
                "Original Company",
                "05/01/2023",
                500.0
        );
        entityManager.persist(invoice);
        entityManager.flush();

        // Find the invoice and update it
        Optional<InvoiceEntity> foundInvoice = invoiceRepository.findById("INV-005");
        assertTrue(foundInvoice.isPresent());
        
        InvoiceEntity invoiceToUpdate = foundInvoice.get();
        invoiceToUpdate.setCompanyName("Updated Company");
        invoiceToUpdate.setAmount(550.0);
        
        invoiceRepository.save(invoiceToUpdate);

        // Find the invoice again and verify it was updated
        Optional<InvoiceEntity> updatedInvoice = invoiceRepository.findById("INV-005");
        assertTrue(updatedInvoice.isPresent());
        assertEquals("Updated Company", updatedInvoice.get().getCompanyName());
        assertEquals(550.0, updatedInvoice.get().getAmount());
    }

    @Test
    public void testDeleteInvoice() {
        // Create and persist a test invoice
        InvoiceEntity invoice = new InvoiceEntity(
                "INV-006",
                "Company to Delete",
                "06/01/2023",
                600.0
        );
        entityManager.persist(invoice);
        entityManager.flush();

        // Verify the invoice exists
        assertTrue(invoiceRepository.findById("INV-006").isPresent());

        // Delete the invoice
        invoiceRepository.deleteById("INV-006");

        // Verify the invoice was deleted
        assertFalse(invoiceRepository.findById("INV-006").isPresent());
    }
}