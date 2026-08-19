package com.historical;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DailySalesEtlService {

    private final JdbcTemplate primaryJdbcTemplate;
    private final NamedParameterJdbcTemplate historicalJdbcTemplate;

    public DailySalesEtlService(
            JdbcTemplate primaryJdbcTemplate,
            @Qualifier("historicalJdbcTemplate") NamedParameterJdbcTemplate historicalJdbcTemplate) {
        this.primaryJdbcTemplate = primaryJdbcTemplate;
        this.historicalJdbcTemplate = historicalJdbcTemplate;
    }

    /**
     * Scheduled job that runs daily at 1:00 AM using the database-to-database SQL implementation.
     */
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void runDailySalesEtl() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        runDailySalesEtlSqlForDate(yesterday);
    }

    /**
     * Option A: EL pipeline pulling data into Java Memory.
     */
    @Transactional
    public void runDailySalesEtlJavaForDate(LocalDate date) {
        ensureHistoricalTablesExist();
        String dateString = date.toString();

        // 1. EXTRACT raw data from the primary database into Java lists
        List<RawSale> sales = extractSalesFromPrimary(dateString);
        List<RawSalesItem> items = extractSalesItemsFromPrimary(dateString);

        // 2. CLEAN (Delete existing records in historical database for idempotency)
        cleanHistoricalDataForDate(dateString);

        // 3. LOAD data into the historical database from memory
        loadSalesFromMemory(sales);
        loadSalesItemsFromMemory(items);
    }

    /**
     * Option B: EL pipeline running entirely database-to-database.
     */
    @Transactional
    public void runDailySalesEtlSqlForDate(LocalDate date) {
        ensureHistoricalTablesExist();
        String dateString = date.toString();

        // 1. CLEAN (Delete existing records in historical database for idempotency)
        cleanHistoricalDataForDate(dateString);

        // 2. LOAD (Direct cross-database copy)
        historicalJdbcTemplate.getJdbcOperations().update("""
            INSERT INTO sales (sales_id, sales_date, sales_time, sales_amount, shift_number, terminal)
            SELECT sales_id, sales_date, sales_time, sales_amount, shift_number, terminal
            FROM truckstop_services.sales
            WHERE sales_date = ?
        """, dateString);

        historicalJdbcTemplate.getJdbcOperations().update("""
            INSERT INTO sales_items (id, sku_code, item_name, quantity, unit_price, sales_type, sales_id)
            SELECT id, sku_code, item_name, quantity, unit_price, sales_type, sales_id
            FROM truckstop_services.sales_items
            WHERE sales_id IN (
                SELECT sales_id FROM truckstop_services.sales WHERE sales_date = ?
            )
        """, dateString);
    }

    private void ensureHistoricalTablesExist() {
        String createSalesTableSql = """
            CREATE TABLE IF NOT EXISTS sales (
                sales_id VARCHAR(255) PRIMARY KEY,
                sales_date DATE NOT NULL,
                sales_time TIME NOT NULL,
                sales_amount DOUBLE NOT NULL,
                shift_number INT,
                terminal VARCHAR(255)
            )
        """;

        String createSalesItemsTableSql = """
            CREATE TABLE IF NOT EXISTS sales_items (
                id VARCHAR(255) PRIMARY KEY,
                sku_code VARCHAR(255) NOT NULL,
                item_name VARCHAR(255) NOT NULL,
                quantity DOUBLE NOT NULL,
                unit_price DOUBLE NOT NULL,
                sales_type INT,
                sales_id VARCHAR(255),
                FOREIGN KEY (sales_id) REFERENCES sales(sales_id) ON DELETE CASCADE
            )
        """;

        historicalJdbcTemplate.getJdbcOperations().execute(createSalesTableSql);
        historicalJdbcTemplate.getJdbcOperations().execute(createSalesItemsTableSql);
    }

    private List<RawSale> extractSalesFromPrimary(String dateString) {
        String query = """
            SELECT sales_id, sales_date, sales_time, sales_amount, shift_number, terminal
            FROM sales
            WHERE sales_date = ?
        """;

        return primaryJdbcTemplate.query(query, (rs, rowNum) -> new RawSale(
                rs.getString("sales_id"),
                rs.getDate("sales_date").toString(),
                rs.getTime("sales_time").toString(),
                rs.getDouble("sales_amount"),
                rs.getObject("shift_number") != null ? rs.getInt("shift_number") : null,
                rs.getString("terminal")
        ), dateString);
    }

    private List<RawSalesItem> extractSalesItemsFromPrimary(String dateString) {
        String query = """
            SELECT si.id, si.sku_code, si.item_name, si.quantity, si.unit_price, si.sales_type, si.sales_id
            FROM sales_items si
            JOIN sales s ON si.sales_id = s.sales_id
            WHERE s.sales_date = ?
        """;

        return primaryJdbcTemplate.query(query, (rs, rowNum) -> new RawSalesItem(
                rs.getString("id"),
                rs.getString("sku_code"),
                rs.getString("item_name"),
                rs.getDouble("quantity"),
                rs.getDouble("unit_price"),
                rs.getObject("sales_type") != null ? rs.getInt("sales_type") : null,
                rs.getString("sales_id")
        ), dateString);
    }

    private void cleanHistoricalDataForDate(String dateString) {
        String deleteItemsSql = """
            DELETE FROM sales_items
            WHERE sales_id IN (
                SELECT sales_id FROM sales WHERE sales_date = ?
            )
        """;

        String deleteSalesSql = """
            DELETE FROM sales
            WHERE sales_date = ?
        """;

        historicalJdbcTemplate.getJdbcOperations().update(deleteItemsSql, dateString);
        historicalJdbcTemplate.getJdbcOperations().update(deleteSalesSql, dateString);
    }

    private void loadSalesFromMemory(List<RawSale> sales) {
        String insertSql = """
            INSERT INTO sales (sales_id, sales_date, sales_time, sales_amount, shift_number, terminal)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        for (RawSale sale : sales) {
            historicalJdbcTemplate.getJdbcOperations().update(
                    insertSql,
                    sale.salesId(),
                    sale.salesDate(),
                    sale.salesTime(),
                    sale.salesAmount(),
                    sale.shiftNumber(),
                    sale.terminal()
            );
        }
    }

    private void loadSalesItemsFromMemory(List<RawSalesItem> items) {
        String insertSql = """
            INSERT INTO sales_items (id, sku_code, item_name, quantity, unit_price, sales_type, sales_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        for (RawSalesItem item : items) {
            historicalJdbcTemplate.getJdbcOperations().update(
                    insertSql,
                    item.id(),
                    item.skuCode(),
                    item.itemName(),
                    item.quantity(),
                    item.unitPrice(),
                    item.salesType(),
                    item.salesId()
            );
        }
    }

    // Java 21 Records for raw DTO representation
    private record RawSale(
            String salesId,
            String salesDate,
            String salesTime,
            double salesAmount,
            Integer shiftNumber,
            String terminal
    ) {}

    private record RawSalesItem(
            String id,
            String skuCode,
            String itemName,
            double quantity,
            double unitPrice,
            Integer salesType,
            String salesId
    ) {}
}
