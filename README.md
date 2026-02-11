# Distributed Cloud Load Testing Platform (Backend)

A cloud-native distributed load testing backend platform designed to orchestrate automated performance testing through microservices architecture and API-driven execution.

---

## Self-developed Load Testing Execution Engine

Decoupled and refactored JMeter’s core execution logic and integrated it into backend services, building an API-driven load testing engine capable of automated and concurrent test execution.

---

## Microservices Architecture Design

Implemented a microservices architecture using:

- **Nacos** for service discovery and configuration management  
- **OpenFeign** for inter-service communication  

This design improves service decoupling, modular deployment, and system scalability.

---

## Test Artifact Storage Solution

Designed an object storage system using **MinIO** to manage performance test artifacts such as reports, logs, and execution outputs, enabling centralized storage and efficient retrieval.

---

## QA-driven Platform Design *(In progress)*

Leveraging hands-on QA experience, the platform is designed beyond traditional HTTP interface load testing.

Integration with **Selenium** is currently in progress to support UI automation testing and expand end-to-end testing capabilities.

---


## Tech Stack

**Backend**

- Java 17  
- Spring Boot 3.x  
- MyBatis-Plus  

**Microservices**

- Spring Cloud Alibaba  
- Nacos  
- OpenFeign  

**Testing**

- Apache JMeter  
- Selenium *(In progress)*  

**Storage**

- MySQL  
- Redis  
- MinIO

**Message**

- Kafka  

**Infrastructure**

- AWS (EC2)  
- Docker  

---
