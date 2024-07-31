package com.truckstopservices.processing.controller;


import com.truckstopservices.processing.dto.ShiftReportDto;
import com.truckstopservices.processing.service.ProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/shiftProcessing")
public class ProcessingController {

    @Autowired
    private final ProcessingService processingService;

    public ProcessingController(ProcessingService processingService){
        this.processingService = processingService;
    }

    @PostMapping("/postShift")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Void> processShift(@RequestBody ShiftReportDto shiftReportDto){
        try {
            processingService.parseShiftData(shiftReportDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
