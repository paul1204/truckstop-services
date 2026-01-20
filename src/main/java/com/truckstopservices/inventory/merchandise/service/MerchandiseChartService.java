package com.truckstopservices.inventory.merchandise.service;

import com.truckstopservices.inventory.merchandise.model.Consumable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import com.truckstopservices.inventory.merchandise.dto.ChartEntry;
import com.truckstopservices.inventory.merchandise.model.DeliveryInfo;
import org.springframework.stereotype.Service;

@Service
public class MerchandiseChartService {

    public Map<String, List<ChartEntry>> getConsumablesChartData(List<? extends Consumable> consumables) {
        Map<String, List<ChartEntry>> chartData = consumables.stream()
            .flatMap(consumable ->
                IntStream.range(0, consumable.getDeliveries().size())
                    .mapToObj(i -> Stream.of(
                        new ChartEntry(i, consumable.getSkuCode(), "Cost of Good", consumable.getDeliveries().get(i).getDeliveryDate(), consumable.getDeliveries().get(i).getCostPerUnit(), consumable.getDeliveries().get(i).getCostPerUnit()),
                            new ChartEntry(i, consumable.getSkuCode(), "Cost of Good Sold", consumable.getDeliveries().get(i).getDeliveryDate() , consumable.getDeliveries().get(i).getRetailPrice() ,consumable.getDeliveries().get(i).getCostPerUnit())
                            ))
                    .flatMap(s -> s)
            )
            .collect(Collectors.groupingBy(entry -> entry.skuCode()));

        return chartData;
    }
}