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
