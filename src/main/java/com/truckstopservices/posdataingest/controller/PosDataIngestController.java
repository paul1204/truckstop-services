package com.truckstopservices.posdataingest.controller;

import com.truckstopservices.accounting.sales.receipt.Receipt;
import com.truckstopservices.accounting.sales.service.SalesService;
import com.truckstopservices.common.types.SalesType;
import com.truckstopservices.inventory.fuel.service.FuelService;
import com.truckstopservices.inventory.merchandise.service.MerchandiseService;
import com.truckstopservices.posdataingest.model.POSSaleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pos-ingest")
public class PosDataIngestController {

    @Autowired
    private final MerchandiseService merchandiseService;

    public PosDataIngestController(MerchandiseService merchandiseService) {
        this.merchandiseService = merchandiseService;
    }

    @PostMapping("/sales")
    public ResponseEntity<Receipt> ingestData(@RequestBody POSSaleDto posSaleDto) {
        return new ResponseEntity<Receipt>(merchandiseService.reduceInventoryV2(posSaleDto), HttpStatus.ACCEPTED);
    }
}