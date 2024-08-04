package com.truckstopservices.processing.controller;

import com.truckstopservices.processing.service.ProcessingService;


import com.truckstopservices.processing.dto.ShiftReportDto;
import com.truckstopservices.processing.service.ProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

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
    public ResponseEntity<ShiftReportDto> processShift(@RequestParam("file") MultipartFile rawShiftReport) {
        ShiftReportDto shiftReportDto;
        try {

            if (rawShiftReport.isEmpty()) {
                return new ResponseEntity<ShiftReportDto>((ShiftReportDto) null, HttpStatus.BAD_REQUEST);
            }

            String rawShiftString = new String(rawShiftReport.getBytes(), StandardCharsets.UTF_8);
            shiftReportDto = processingService.parsePOSFile(rawShiftString);

        } catch (Exception e) {
            return new ResponseEntity<ShiftReportDto>((ShiftReportDto) null, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(shiftReportDto, HttpStatus.CREATED);
    }
}
