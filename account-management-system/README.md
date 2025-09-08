# Account Management System - Microservices Architecture

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
### Note:
- Without the -DskipTests command, the project shows errors because the ports and configurations are set up only for Docker. If you want to run it without Docker, you need to update the ports (including PostgreSQL) in the application.yml.
## Build Docker Images
- First, open the Docker Desktop application and make sure it is running before you execute any Docker commands.
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





