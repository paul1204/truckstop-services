//package com.truckstopservices.inventory.manager.service;
//
//import com.truckstopservices.inventory.manager.entity.OrderDTO;
//import com.truckstopservices.inventory.merchandise.beverages.entity.Beverage;
//import com.truckstopservices.inventory.merchandise.config.Brands;
//import com.truckstopservices.inventory.merchandise.snacks.entity.Chips;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static com.truckstopservices.inventory.manager.config.Merchants.A1Supplies;
//
//public class InventoryOrderService {
//
//
//    //public OrderDTO placeOrder(OrderDTO order){
//    public OrderDTO placeOrder(){
//        //API call to make Order from Vendor
//        List<?> order = Arrays.asList(
//                new Chips("123", "Plain", 1.49, Brands.LAYS),
//                new Chips("456", "Spicy", 1.49, Brands.LAYS),
//                new Chips("789", "Cool Ranch", 1.49, Brands.DORITOS),
//                new Beverage("B123", "Coca Cola", "12oz", 1.49, Brands.COCACOLA),
//                new Beverage("B456", "Diet Coke", "12oz", 1.49, Brands.COCACOLA),
//                new Beverage("B789", "Sprite", "12oz", 1.49, Brands.COCACOLA)
//                // Add more predefined snacks as needed
//        );
//        return new OrderDTO("123", A1Supplies, 199.99, order);
//    }
//
//}
