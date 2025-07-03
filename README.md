# Truckstop Services

A modular Spring Boot microservices project for managing the operations of a truck stop, including inventory, fuel, restaurant, merchandise, and accounting (payables/receivables). The system is designed for extensibility, maintainability, and clear separation of business domains.

## Table of Contents

- [Features](#features)
- [Modules Overview](#modules-overview)
- [API Endpoints](#api-endpoints)
- [Data Model](#data-model)
- [Sample Data](#sample-data)
---

## Features

- **Inventory Management**: Track and update inventory for fuel, merchandise, and restaurant items.
- **Fuel Management**: FIFO-based fuel inventory, delivery, and sales tracking.
- **Restaurant & Merchandise**: Accept deliveries, update stock, and process sales.
- **Shift Processing**: Parse and process shift and inventory reports.
- **Accounting**: Generate invoices for deliveries and integrate with accounts payable.
- **Extensible**: Easily add new inventory types, sales channels, or accounting logic.


## Modules Overview

- **accounting**: Handles invoices and accounts payable.
- **inventory**
  - **fuel**: Fuel inventory, deliveries, and sales.
  - **merchandise**: Bottled drinks, snacks, and non-restaurant food.
  - **restaurant**: Hot food inventory and deliveries.
  - **manager**: Orchestrates inventory updates and reporting.
- **processing**: Shift report parsing, inventory updates, and sales aggregation.
- **posdataingest**: (Reserved for POS data ingestion logic.)

---

## API Endpoints

### Fuel Inventory

- `GET /api/inventory/fuel/viewInventory`  
  View all fuel inventory.

- `POST /api/inventory/fuel/update/FuelInventory/reduceGallons`  
  Deduct sold gallons from inventory.

- `PUT /api/inventory/fuel/update/FuelInventory/FuelDelivery`  
  Update inventory with a new fuel delivery.

- `PUT /api/inventory/fuel/update/Diesel/FIFO`  
  Sell diesel using FIFO inventory logic.

- `PUT /api/inventory/fuel/update/RegularFuel/FIFO`  
  Sell regular fuel using FIFO.

- `PUT /api/inventory/fuel/update/PremiumFuel/FIFO`  
  Sell premium fuel using FIFO.

### Merchandise & Restaurant Inventory

- `PUT /api/inventory`  
  Update merchandise inventory from sales.

- `PUT /api/inventory/delivery/merchandise`  
  Accept a merchandise delivery (CSV upload).

- `PUT /api/inventory/delivery/restaurant`  
  Accept a restaurant delivery (CSV upload).

### Shift Processing

- `GET /api/shiftProcessing/postShift`  
  Instructions for posting a shift.

- `POST /api/shiftProcessing/postShift`  
  Upload shift and inventory reports for processing.

---

## Data Model

### Fuel

- **FuelDelivery**: Represents a delivery of diesel, regular, and premium fuel.
- **Diesel, RegularOctane, PremiumOctane, MidGradeOctane**: Fuel inventory entities.
- **HistoricalFuel**: Archived fuel inventory.

### Merchandise

- **ColdBeverage**: Bottled drinks.
- **NonRestaurantFood**: Snacks and non-restaurant food.
- **Consumable**: Base class for merchandise items.

### Restaurant

- **HotFood**: Restaurant menu items.
- **RestaurantModel**: Base class for restaurant inventory.

### Processing

- **ShiftReport**: Aggregates sales and inventory for a shift.
- **FuelSales, MerchandiseSales, RestaurantSales, TobaccoSales**: Sales breakdowns.
- **InventoryDto**: Used for inventory updates.

### Accounting

- **Invoice, InvoiceDetail**: For accounts payable.

---

## Sample Data

Sample reports and delivery files are provided in the `reports/` directory:

- **Shift Reports**:  
  - `reports/shift/shift1.txt`, `shift2.txt`  
    ```
    DATE: 2024-04-01
    SHIFT_NUMBER: 1
    ...
    FUEL_SALES:
      DIESEL_TRANSACTIONS: 150
      ...
    ```

- **Inventory Reports**:  
  - `reports/inventory/shift1Inventory.txt`, `shift2Inventory.txt`  
    ```
    INVENTORY:
      BOTTLED_DRINKS_DETAILS:
        SKU_CODE: 100, QUANTITY: 10
        ...
    ```

- **Merchandise Delivery**:  
  - `reports/order/merchandise_delivery.csv`  
    ```
    ID,Product Name,Price,Brand,Quantity,Size,ItemType
    100,Coke,1.99,Coca-Cola,20,20oz,D
    ...
    ```

- **Restaurant Delivery**:  
  - `reports/order/restaurant_delivery.csv`  
    ```
    SKU Code,Product Name,Qty,Size,Price,Total Price
    HF123,Cheese Pizza,5,Large,7.99,39.95
    ...
    ```

---
