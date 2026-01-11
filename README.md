# 📦 Order & Delivery Microservices System

A scalable, event-driven microservices architecture simulating a real-world e-commerce order fulfillment system. Built with **Spring Boot**, **Apache Kafka**, **Redis**, and **PostgreSQL**.

## 🚀 Key Features (Resume Highlights)
* **Event-Driven Architecture**: Decoupled `Order` and `Tracking` services using **Kafka** for asynchronous communication.
* **High-Performance Caching**: Implemented **Redis** to cache tracking status, reducing database load and latency.
* **Fault Tolerance**: Integrated **Resilience4j Circuit Breaker** to handle database downtimes gracefully.
* **Multithreading**: Used `@Async` for non-blocking logistics processing (simulating 3rd party API delays).
* **Eventual Consistency**: Implemented a feedback loop where the Tracking Service updates the Order Database via Kafka events.
* **Containerization**: Fully Dockerized environment (Apps + DBs + Broker) using **Docker Compose**.

---

## 🛠️ Tech Stack
* **Language:** Java 17
* **Framework:** Spring Boot 3.x (Web, Data JPA, Kafka)
* **Databases:** PostgreSQL (Persistent), Redis (Cache)
* **Message Broker:** Apache Kafka & Zookeeper
* **Resiliency:** Resilience4j
* **Tools:** Docker, Docker Compose, Postman, Lombok

---

## 🏗️ Architecture Flow
1.  **User** places an order via REST API → **Order Service**.
2.  **Order Service** saves "PENDING" state to **PostgreSQL** and publishes an event to **Kafka** (`order-topic`).
3.  **Tracking Service** listens to the topic, processes the order asynchronously (simulating shipping time).
4.  **Tracking Service** updates the status in **Redis** for real-time tracking checks.
5.  **Tracking Service** publishes an update event back to **Kafka** (`order-status-topic`).
6.  **Order Service** listens to the update and syncs the final status in **PostgreSQL**.

---

## ⚡ Getting Started

### Prerequisites
* Docker & Docker Compose installed
* Java 17+ (optional, if running locally without Docker)

### 1. Clone the Repository
```bash
git clone [https://github.com/YOUR_USERNAME/order-delivery-microservices.git](https://github.com/YOUR_USERNAME/order-delivery-microservices.git)
cd order-delivery-microservices

```
---
### 2. ⚡ Quick Start
1. `docker-compose up -d`
2. Run `OrderServiceApplication` and `TrackingServiceApplication`.

### 🔌 API Endpoints & Testing
1. Place an Order (POST)
URL: http://localhost:8080/api/orders

```Bash

curl -X POST http://localhost:8080/api/orders \
-H "Content-Type: application/json" \
-d '{"product": "MacBook Pro", "price": 2500.00}'
```
2. Track Order Status (GET)
URL: http://localhost:8081/api/tracking/{orderId} Immediately after ordering, this will return "PROCESSING". Wait 5 seconds, and it updates to "SHIPPED".

```Bash

curl http://localhost:8081/api/tracking/1
```
3. Get All Orders (GET)
URL: http://localhost:8080/api/orders Check the PostgreSQL records to see the status automatically updated via Kafka.
```

Bash

curl http://localhost:8080/api/orders
```
📂 Project Structure
Plaintext

order-delivery-system/
├── docker-compose.yml       # Infrastructure (Kafka, Postgres, Redis)
├── order-service/           # Producer | Write-Heavy | Postgres
│   ├── src/.../OrderService.java  # Circuit Breaker & Kafka Publisher
│   └── src/.../OrderStatusListener.java # Kafka Consumer (Feedback loop)
└── tracking-service/        # Consumer | Read-Heavy | Redis
    ├── src/.../TrackingService.java # @Async Processing & Redis Caching
    └── src/.../OrderEventListener.java # Kafka Consumer

### 🤝 Contributing
Feel free to fork this project and submit a pull request if you want to add features like Security (JWT) or Kubernetes deployment.
