package com.truckstopservices.inventory.merchandise.dto;

import java.util.List;
import java.util.Map;
import com.truckstopservices.inventory.merchandise.model.Consumable;
import com.truckstopservices.inventory.merchandise.dto.ChartEntry;

public record AllProductsChartData(List<? extends Consumable> allProducts, Map<String, List<ChartEntry>> chartData) {}