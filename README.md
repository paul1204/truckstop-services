# Truckstop Services

A modern, modular Spring Boot service managing truck stop operations, including fuel sales, retail inventory, restaurant orders, parking reservation management, and accounting (journal ledgers, invoicing, and accounts payable).

---

## 🚀 Key Features

* **FIFO Fuel Inventory**: Dynamic FIFO (First-In, First-Out) depletion logic for Diesel, Regular (87), Mid-Grade (89), and Premium (93) fuel batches.
* **Accounting Ledger**: Double-entry bookkeeping system with Journal Entries, Journal Lines, and transactional posting validations.
* **Invoicing**: Automatic generation of vendor invoices upon receiving merchandise or fuel deliveries.
* **Unified Inventory Management**: Integrated tracking for bottled beverages, packaged snacks, and hot restaurant food.
* **Parking Management**: Real-time reservation and rate control for truck parking spots.
* **Ingestion APIs**: POS data ingestion support for importing sales reports from register terminals.

---

## 🛠️ Technology Stack

* **Language**: Java 21 (utilizing modern features like Records and Sealed Classes)
* **Framework**: Spring Boot 3.x / 4.x
* **Data Access**: Spring Data JPA & Hibernate
* **Database**: MySQL 8.3
* **API Documentation**: Springdoc OpenAPI / Swagger UI
* **Deployment & Development**: Docker Compose & VS Code Devcontainers

---

## 📂 Project Architecture

The system is organized into modular packages representing business sub-domains:

```
src/main/java/com/truckstopservices/
├── accounting/             # Invoicing and vendor accounts payable
├── finance/                # General ledger journal entries, chart of accounts, and posting engines
├── inventory/
│   ├── fuel/               # Fuel inventory, deliveries, and FIFO sales calculations
│   ├── merchandise/        # Packaged foods, snacks, and beverage inventory
│   ├── restaurant/         # Hot food kitchen inventory
│   └── manager/            # Inventory orchestrators and balance reports
├── parking/                # Parking rates, reservations, and status management
└── posdataingest/          # Ingestion endpoints for POS terminal sales
```

---

## ⚙️ Setup & Installation

### Prerequisites
* Docker & Docker Compose
* Java 21 JDK (if running locally outside Docker)

### 1. Start the Database (MySQL)
The project is configured to use MySQL. Start the database container via Docker Compose:
```bash
docker compose up -d mysql
```
* **Host Port**: `3308` (maps to container port `3306`)
* **Database Name**: `truckstop_services`
* **Username/Password**: `root` / `password`
* **Database Initialization**: Schema and initial seeds are loaded automatically from [init.sql](file:///Users/paul/Documents/Code/truckstop-services/docker/mysql/init.sql).

### 2. Run the Spring Boot Application
Run the application locally using the Maven wrapper:
```bash
./mvnw spring-boot:run
```
The server will start on port `8080`.

### 3. API Documentation
Once the application is running, you can explore and test the endpoints via the Swagger UI interface:
👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 📌 Core API Endpoints

### ⛽ Fuel Operations
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/fuel/viewInventory` | View consolidated available gallons for all fuel types. |
| `GET` | `/fuel/viewRecentFuelDeliveries` | View recent fuel delivery batches (limits via `count`). |
| `PATCH` | `/fuel/update/FuelDelivery/retailPrices` | Update retail prices for specific fuel batches. |
| `PUT` | `/fuel/update/Diesel/FIFO` | Deduct diesel gallons sold using FIFO logic. |
| `PUT` | `/fuel/update/RegularFuel/FIFO` | Deduct regular (87) gallons sold using FIFO logic. |
| `PUT` | `/fuel/update/PremiumFuel/FIFO` | Deduct premium (93) gallons sold using FIFO logic. |

### 🅿️ Parking Management
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/parking/status` | List parking spots and their availability status. |
| `POST` | `/parking/reserve` | Book a parking spot for a truck driver. |

---

## 🧪 Testing

Run the test suite using the Maven wrapper:
```bash
./mvnw test
```
*(Note: Some integration test files are currently disabled or commented out for local development purposes).*