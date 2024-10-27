# Distributed Notification System

This monorepo hosts a **Distributed Notification System** built with **Spring Boot**. The system allows distributed handling of notifications for multiple users, utilizing **Apache Kafka** for asynchronous communication and **MongoDB** for persisting notifications. The architecture is designed to be highly scalable, supporting multiple services such as **User Service** and **Order Service** that can trigger notifications across distributed environments.

## Features

- **Asynchronous Messaging**: Utilizes Kafka to handle inter-service messaging.
- **Scalable Notifications**: Scales to support multiple services triggering notifications independently.
- **MongoDB Persistence**: Stores notifications, supporting historical data retrieval.
- **Error Handling and Validation**: Provides logging and robust validation for incoming requests.

## Architecture Overview

The monorepo is organized as follows:

- **User Service**: Handles user operations and sends notifications when necessary.
- **Order Service**: Manages orders and dispatches messages to Kafka to trigger notifications.
- **Notification Service**: Listens to Kafka messages and creates notifications in MongoDB.
- **Kafka**: Acts as the message broker for asynchronous communication.
- **MongoDB**: Stores notifications, supporting CRUD operations.

## Technology Stack

- **Java 17**
- **Spring Boot 3.x**
- **Kafka 2.13-2.7.0**
- **MongoDB 4.x**
- **Docker & Docker Compose**

## Getting Started

### Prerequisites

- **Java 17**
- **Docker & Docker Compose**
- **Kafka**
- **MongoDB**

### Project Structure
distributed-notification-system/
`
├── user-service/
│   ├── src/
│   ├── Dockerfile
│   └── application.yml
├── order-service/
│   ├── src/
│   ├── Dockerfile
│   └── application.yml
├── notification-service/
│   ├── src/
│   ├── Dockerfile
│   └── application.yml
├── docker-compose.yml
└── README.md
`

### Contributing
  - Fork the Repository
  - Create a Feature Branch: git checkout -b feature-branch
  - Commit Changes: git commit -m 'Add new feature'
  - Push to Branch: git push origin feature-branch
  - Open a Pull Request

### License
Distributed Notification System is open-source software licensed under the MIT License
