package com.truckstopservices.posdataingest.model;

import com.truckstopservices.accounting.sales.entity.SalesItem;
import java.util.List;

public record POSSaleDto(
        Double totalSalesAmount,
        List<SalesItem> salesItems
) {
}
