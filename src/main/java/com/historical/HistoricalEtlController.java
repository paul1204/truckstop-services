package com.historical;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/historical/etl")
public class HistoricalEtlController {

    private final DailySalesEtlService etlService;

    public HistoricalEtlController(DailySalesEtlService etlService) {
        this.etlService = etlService;
    }

    /**
     * POST endpoint to manually trigger the daily sales EL pipeline for today.
     * Accessible via POST /api/historical/etl/today
     */
    @PostMapping("/today")
    public ResponseEntity<String> runEtlForToday() {
        etlService.runDailySalesEtlForToday();
        return ResponseEntity.ok("Raw daily sales EL job executed successfully for today!");
    }
}
