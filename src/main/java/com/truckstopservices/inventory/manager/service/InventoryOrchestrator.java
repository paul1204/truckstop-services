//package com.truckstopservices.inventory.manager.service;
//
//
//import com.truckstopservices.inventory.manager.entity.OrderDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class InventoryOrchestrator {
//    @Autowired
//    private InventoryBalanceService balanceService;
//    @Autowired
//    private InventoryOrderService orderService;
//
//    public InventoryOrchestrator(InventoryBalanceService balanceService, InventoryOrderService orderService) {
//        this.balanceService = balanceService;
//        this.orderService = orderService;
//    }
//
//    //public void placeOrder(OrderDTO order) {
//    public void placeOrder() {
//       OrderDTO newOrder = orderService.placeOrder();
//       balanceService.updateInventory(newOrder);
//    }
//
//
//
//
//}
