package com.historical;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/historical/etl")
public class HistoricalEtlController {

    private final DailySalesEtlService etlService;
    private final InvoiceEtlService invoiceEtlService;

    public HistoricalEtlController(DailySalesEtlService etlService, InvoiceEtlService invoiceEtlService) {
        this.etlService = etlService;
        this.invoiceEtlService = invoiceEtlService;
    }

    @PostMapping("/java/today")
    public ResponseEntity<String> runEtlJava() {
        long start = System.nanoTime();
        etlService.runDailySalesEtlJavaForDate(LocalDate.now());
        long durationMs = (System.nanoTime() - start) / 1_000_000;
        return ResponseEntity.ok("Java Memory EL executed in " + durationMs + " ms.");
    }


    @PostMapping("/sql/today")
    public ResponseEntity<String> runEtlSql() {
        long start = System.nanoTime();
        etlService.runDailySalesEtlSqlForDate(LocalDate.now());
        long durationMs = (System.nanoTime() - start) / 1_000_000;
        return ResponseEntity.ok("Pure SQL cross-database EL executed in " + durationMs + " ms.");
    }

    @PostMapping("/accounts-payable/sql/today")
    public ResponseEntity<String> runAccountsPayableEtlSql() {
        long start = System.nanoTime();
        invoiceEtlService.runAccountsPayableEtl();
        long durationMs = (System.nanoTime() - start) / 1_000_000;
        return ResponseEntity.ok("Accounts Payable SQL cross-database EL executed in " + durationMs + " ms.");
    }
}
