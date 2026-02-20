# Distributed Cloud-based Performance Testing Platform (Backend)

## 1. Project Overview

This project is a cloud-based distributed performance testing backend platform built on a microservices architecture.

It is designed to orchestrate automated performance testing tasks and process large-scale test result data through asynchronous message pipelines.

The platform focuses on:

- Distributed test execution scheduling  
- Decoupled service communication  
- Asynchronous result processing for concurrent test scenarios
- Centralized test data storage  

---

## 2. System Architecture

The platform adopts a microservices architecture and is currently divided into the following core modules:

| Module | Responsibility |
|--------|----------------|
| dcloud-engine | Performance test execution and scheduling (In Progress) |
| dcloud-data | Test result processing and persistence (In Progress)  |
| dcloud-gateway | Unified traffic entry (Planned) |
| dcloud-account | User and permission management (Planned) |
| dcloud-common | Shared utilities and configuration module (In Progress) |

---

## 3. Core Functionalities

### 3.1 Test Execution Engine

A self-developed performance testing execution engine based on Apache JMeter.

Key implementations:

- Decoupled core execution logic of JMeter  
- Encapsulated as a service-based backend execution component  
- Supports triggering performance test tasks via API  
- Validated hundred-to-thousand level concurrent execution scenarios in a single-node environment

---

### 3.2 Asynchronous Result Processing Pipeline

To prevent performance test results from directly impacting the database, Kafka is introduced to build an asynchronous processing pipeline.

Execution flow:

Engine → Kafka → Data → MySQL / MinIO


Implementation effects:

- Asynchronous transmission of test logs and reports  
- Peak load buffering for database writes  
- Improved system stability under high-load scenarios  
- Enhanced throughput for large-scale result processing  

---

### 3.3 Microservices Architecture Design

The system is built on the Spring Cloud Alibaba ecosystem.

Core components include:

- Nacos  
  - Service registration and discovery  
  - Configuration management  

- OpenFeign  
  - RPC communication between services  

Architecture characteristics:

- Service decoupling  
- Independent deployment of modules  
- Designed with horizontal scalability considerations

---

### 3.4 Test Artifact Storage Strategy

A layered storage design is implemented for handling large volumes of test data.

| Data Type | Storage Solution |
|-----------|------------------|
| Structured data | MySQL |
| Test reports / log files | MinIO |

Features:

- Centralized storage management  
- Efficient retrieval and download  
- Separation of structured and unstructured data  

---

## 4. QA-Oriented Platform Expansion (Planned)

Based on QA automation testing experience, the platform is being extended from API performance testing to an integrated testing platform.

Ongoing developments:

- Integration of Selenium UI automation execution  
- Extension of End-to-End (E2E) test orchestration capabilities  

Target:

- Unified platform combining API performance testing and UI automation testing  

---

## 5. Authentication and Gateway Design (Planned)

Planned capabilities include:

- API Gateway as unified traffic entry  
- Token-based authentication (Sa-Token)   

Designed to support:

- Multi-user isolation  
- Multi-tenant secure access  

---

## 6. Technology Stack

### Backend Framework

- Java 17  
- Spring Boot 3.x  
- MyBatis-Plus  

### Microservices

- Spring Cloud Alibaba  
- Nacos  
- OpenFeign  

### Testing Tools

- Apache JMeter  
- Selenium (Planned)

### Messaging

- Kafka  

### Data Storage

- MySQL  
- Redis (Planned)  
- MinIO  

### Infrastructure

- AWS EC2  
- Docker  

---

