# Food Ordering System

## Requirements

- JDK 21
- Docker Desktop
- Python 3
- Windows PowerShell

Check Java before starting:

```powershell
java -version
```

The version should be `21.x`.

## Start the project

Run these commands from the repository root.

### 1. Start Kafka

```powershell
docker compose up -d
docker compose ps
```

Kafka runs on `localhost:9092`. Kafka UI is available at
`http://localhost:8085`.

### 2. Start Order Service

Open a new PowerShell window:

```powershell
cd order-service
.\mvnw.cmd spring-boot:run
```

Order Service runs on `http://localhost:8081`.

### 3. Start Payment Service

Open another PowerShell window:

```powershell
cd payment-service
.\mvnw.cmd spring-boot:run
```

Payment Service runs on `http://localhost:8082`.

### 4. Start Delivery Service

Open another PowerShell window:

```powershell
cd delivery-service
.\mvnw.cmd spring-boot:run
```

Delivery Service runs on `http://localhost:8083`.

### 5. Start the frontend

Open another PowerShell window:

```powershell
cd frontend
python -m http.server 5500
```

Open `http://localhost:5500` in a browser.

## Stop Kafka

From the repository root:

```powershell
docker compose down
```

## Sample Inputs

### Customer Account
Email: customer@test.com
Password: customer123

### Staff Account
Email: staff@test.com
Password: staff123

### Sample Customer Registration
Name: Test Customer
Email: testcustomer@test.com
Password: test123

### Sample Order
Food Item: Chicken Burger
Quantity: 2
Payment Method: CASH
Delivery Address: 10 Jalan Example
