package com.truckstopservices.processing.controller;

import com.truckstopservices.processing.dto.InventoryDto;
import com.truckstopservices.processing.service.ProcessingService;


import com.truckstopservices.processing.dto.ShiftReportDto;
import com.truckstopservices.processing.service.ProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/shiftProcessing")
public class ProcessingController {

    @Autowired
    private final ProcessingService processingService;

    public ProcessingController(ProcessingService processingService) {
        this.processingService = processingService;
    }

    @GetMapping("/postShift")
    public ResponseEntity<String> process() {
        return new ResponseEntity<>("Hello! Please use HTTP POST to upload file from path reports/shift/shift1.txt", HttpStatus.CREATED);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/postShift")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Map<String, Object>> processShift(@RequestParam("shift_report") MultipartFile rawShiftReport, @RequestParam("inventory_report") MultipartFile rawInventoryReport) {
        long startTime = System.currentTimeMillis();
        try {
            if (rawShiftReport.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            final ShiftReportDto[] shiftReportDto = new ShiftReportDto[1];
            @SuppressWarnings("unchecked")
            final List<List<InventoryDto>>[] inventoryList = new List[1];
            Thread parseShiftThread = new Thread(()-> {
                String rawShiftString = null;
                try {
                    rawShiftString = new String(rawShiftReport.getBytes(), StandardCharsets.UTF_8);
                    shiftReportDto[0] = processingService.parsePOSShiftFile(rawShiftString);
                    processingService.parseShiftDataAndSaveToRepo(shiftReportDto[0]);
                    processingService.updateFuelInventory(shiftReportDto[0]);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            Thread parseInventoryThread = new Thread(()->{
                try {
                    inventoryList[0] = processingService.parsePOSInventoryFile(rawInventoryReport);
                    processingService.updateInventory(inventoryList[0]);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            parseShiftThread.start();
            parseInventoryThread.start();

            parseShiftThread.join();
            parseInventoryThread.join();

            long endTime = System.currentTimeMillis();
            System.out.println("Execution Time: " + (endTime - startTime) + " milliseconds");
            return new ResponseEntity<>(Map.of("ShiftReport",shiftReportDto[0], "Inventory Report",inventoryList[0]), HttpStatus.CREATED);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            return new ResponseEntity<>( null, HttpStatus.BAD_REQUEST);
        }

    }
}
