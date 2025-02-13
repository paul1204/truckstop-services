package com.truckstopservices.inventory.fuel.dto;

import com.truckstopservices.accounting.model.Invoice;
import com.truckstopservices.inventory.fuel.entity.PremiumOctane;
import com.truckstopservices.inventory.fuel.model.Fuel;

public record FuelDeliveryResponse<Fuel>(
        boolean success,
        String message,
        Fuel[] fuelType,
        Invoice vendorInvoice
) {}