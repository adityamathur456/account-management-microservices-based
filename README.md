# Account Management System - Frontend + Mcrioservice Backend

This is the **frontend client application** for the **Account Management System**, built with **Next.js**.  
It connects to the backend microservices implemented in **Spring Boot**, providing a seamless interface to manage bank accounts and transactions.

---

## Features

- **Login** – Secure authentication via `auth-service`.
- **Register** – Create new users and register customer profiles.
- **Account Registration** – Open new bank accounts through `account-service`.
- **Dashboard** – View account details, balance, and transaction history.
- **Transactions** – Transfer funds to active accounts using `transaction-service`.

The frontend communicates with the backend microservices through the **gateway-service** and relies on the **Spring Boot microservices** for all data operations.

---

## Screenshots

### Login Page
![Login Page](./account-management-client/loginPage.png)

### Register Page
![Register Page](./account-management-client/register.png)

### Account Registration Page
![Account Registration](./account-management-client/account-register.png)

### Dashboard
![Dashboard](./account-management-client/dashboard.png)

> Replace the paths with actual images of your application.

## Getting Started

### Install Dependencies

```bash
npm install
```

## Run Development Server
```bash
npm run dev
```

- Open http://localhost:3000
- in your browser to view the frontend.

## Notes

- The frontend requires the backend microservices which is insides account-management-system to be running via Docker.

- Ensure the gateway-service is accessible at the expected API endpoints for full functionality.

- All transactions and account operations are performed through the backend microservices.


# Account Management System - Backend Microservices Architecture

This project is a **microservices-based Account Management System** built using **Spring Boot**. The system includes the following services:

- **account-service** – Manages bank accounts.
- **auth-service** – Handles authentication and authorization.
- **gateway-service** – API gateway routing requests to microservices.
- **customer-service** – Manages customer information.
- **transaction-service** – Handles banking transactions.
- **discovery-service** – Service discovery for all microservices.

The project is designed to run **entirely via Docker** using a single `docker-compose.yml` file. Each microservice is containerized and orchestrated with Docker.

---

## ⚠️ Important Note

> This project is **designed to run only in Docker**. Running microservices individually without Docker is not supported. Ensure you have **Docker Desktop installed** before proceeding.

---

## Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop)
- Java 21+

---

## Project Structure
account-management-system/
- ├─ account-service/
- ├─ auth-service/
- ├─ gateway-service/
- ├─ customer-service/
- ├─ transaction-service/
- ├─ discovery-service/
- ├─ docker-compose.yml
- └─ README.md


---

## Steps to Build and Run

### 1. Build Spring Boot JARs
### ensure you have maven install in your system
- From the root folder, run:
```bash
# Build all Spring Boot services
mvn clean install -DskipTests
```
### Meaning:
- -DskipTests = tells Maven to compile code and build the JAR but skip running tests. run part is skip
- The code is still compiled, but test cases are not executed.
- The final .jar will be generated in the target/ folder.

## Build Docker Images
- From the root folder, run:
```bash
docker-compose build
```
- This command will build Docker images for all microservices.

## Run All Services via Docker
### Start all microservices at once:
```bash
docker-compose up -d
```
### To stop all services:
```bash
docker-compose down
```

## Accessing Services

### Gateway API – http://localhost:8085

### Discovery Service – http://localhost:8761

### Other microservices are accessed via gateway routes.


## Notes

### Ensure no other applications are running on ports 8085 or 8761 to avoid conflicts.

### All microservices are automatically registered with discovery-service.

### Docker must be running before starting the project.





