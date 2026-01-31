# TaskFlow Engine

A lightweight background task processing engine built with **Spring Boot**, **PostgreSQL**, and **REST API**.  
Designed to demonstrate clean architecture, test-driven development, and modern Java practices

- Fully tested with **Testcontainers** (real PostgreSQL in Docker)
- Follows Git workflow: feature branches → Pull Requests → squash merge
- Includes structured logging, validation, and error handling


## Features

- **Create tasks** via REST API with custom JSON payload
- **Track lifecycle**: `QUEUED` → `RUNNING` → `COMPLETED` / `FAILED`
- **List all tasks** with `GET /api/tasks`
- **Input validation**: `taskType` must not be blank
- **Structured logging** for debugging and monitoring
- **Database schema managed** by Flyway (optional)
- **Fully tested**:
  - Unit tests with Mockito
  - Integration tests with Testcontainers + PostgreSQL


## Tech Stack

- **Java 21**
- **Spring Boot 3.5** (Web, Data JPA, Validation, Scheduling)
- **Hibernate / JPA** (with UUID primary keys, JSONB support)
- **PostgreSQL** (via Testcontainers in tests)
- **Lombok** (reduces boilerplate)
- **Flyway** (schema migrations – disabled in tests)
- **Rancher Desktop** (Docker-compatible runtime on Windows + WSL2)
- **Maven**, **JUnit 5**, **AssertJ**, **Awaitility**, **Testcontainers**

## Local Setup (Windows + WSL2)
### Prerequisites
- Windows 10/11 (Build 19041+)
- WSL2 with Ubuntu
- [Rancher Desktop](https://rancherdesktop.io/) (configured with `dockerd` runtime)
Verify:
  - bash
wsl --list --verbose
docker --version

## Local Setup (Windows with WSL2)

### Prerequisites

- Windows 10/11 (Build 19041 or later)
- WSL2 enabled with Ubuntu distribution
- Rancher Desktop installed and configured with **`dockerd (moby)`** runtime

> Verify your setup:
> - powershell
> wsl --list --verbose
> docker --version

### Steps

1 git clone https://github.com/MyManuscripts/taskflow-engine.git
2 cd taskflow-engine
3 Start PostgreSQL container: docker-compose up -d
4 Run the application: ./mvnw spring-boot:run

The app starts on http://localhost:8080.

### Testing
- **Unit tests**: mock-based, fast, no database.
- **Integration tests**: use **Testcontainers** to spin up a **real PostgreSQL instance in Docker** — no H2, full fidelity.

> Note: The main application expects a local PostgreSQL server at `localhost:5432`. For development, you must run PostgreSQL separately (e.g., via Rancher Desktop or native install).

Running tests:
./mvnw clean test

### Status
Core functionality complete and tested
Not production-ready (no auth, retries, monitoring)

### Common Issues

Application fails to start with "role 'taskflow' does not exist"

This usually happens when:
- A local PostgreSQL instance is already running on port `5432`
- The application connects to the **local PostgreSQL** instead of the Docker container

**Solution**:
1. Stop any local PostgreSQL service:
    - powershell
   taskkill /PID <postgres-pid> /F

2. Ensure docker-compose up -d starts a clean container:
   docker-compose down -v
   docker-compose up -d
