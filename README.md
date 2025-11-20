# Book Rental System

Hệ thống quản lý cho thuê sách (Book Rental Management System) được xây dựng bằng Java Spring Boot.

## Công nghệ sử dụng

*   **Java**: 17
*   **Framework**: Spring Boot 3.x
*   **Database**: PostgreSQL (chạy trên Docker)
*   **Build Tool**: Maven

## Yêu cầu cài đặt

*   Java 17+
*   Docker & Docker Compose

## Hướng dẫn chạy dự án

### 1. Khởi tạo Database

Sử dụng Docker Compose để khởi tạo PostgreSQL database:

```bash
docker-compose up -d
```

Database sẽ chạy tại cổng `5433` (để tránh xung đột với cổng mặc định 5432).
*   **User**: `storius`
*   **Password**: `123456`
*   **Database**: `book_rental`

### 2. Chạy ứng dụng

Sử dụng Maven Wrapper để chạy ứng dụng:

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

### 3. Chạy Test

```bash
.\mvnw.cmd test
```
