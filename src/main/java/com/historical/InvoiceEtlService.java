package com.historical;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvoiceEtlService {

    private final JdbcTemplate primaryJdbcTemplate;
    private final NamedParameterJdbcTemplate historicalJdbcTemplate;

    public InvoiceEtlService(
            JdbcTemplate primaryJdbcTemplate,
            @Qualifier("historicalJdbcTemplate") NamedParameterJdbcTemplate historicalJdbcTemplate) {
        this.primaryJdbcTemplate = primaryJdbcTemplate;
        this.historicalJdbcTemplate = historicalJdbcTemplate;
    }

    /**
     * Runs the ETL pipeline for accounts payable (vendor invoices) entirely database-to-database.
     */
    @Transactional
    public void runAccountsPayableEtl() {
        ensureHistoricalTablesExist();

        // 1. CLEAN (Delete all existing records in the historical database for idempotency)
        cleanHistoricalData();

        // 2. LOAD (Direct cross-database copy)
        historicalJdbcTemplate.getJdbcOperations().update("""
            INSERT INTO accounts_payable (invoice_number, company_name, date, amount, created_at)
            SELECT invoice_number, company_name, date, amount, created_at
            FROM truckstop_services.invoices
        """);
    }

    private void ensureHistoricalTablesExist() {
        String createTableSql = """
            CREATE TABLE IF NOT EXISTS accounts_payable (
                invoice_number VARCHAR(255) PRIMARY KEY,
                company_name VARCHAR(255),
                date VARCHAR(255),
                amount DOUBLE,
                created_at DATETIME
            )
        """;
        historicalJdbcTemplate.getJdbcOperations().execute(createTableSql);
    }

    private void cleanHistoricalData() {
        historicalJdbcTemplate.getJdbcOperations().update("DELETE FROM accounts_payable");
    }
}
