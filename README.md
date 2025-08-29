# CutHair - Barbershop Appointment Management System

A modern Spring Boot application for managing gi appointments with a RESTful API.

## Features

- ✅ Create, read, update, and delete appointments
- ✅ Search appointments by name, phone number, or date
- ✅ Conflict detection (prevents overlapping appointments)
- ✅ Business hours validation (9 AM - 6 PM)
- ✅ Comprehensive input validation
- ✅ Audit trail (created_at, updated_at timestamps)
- ✅ RESTful API with proper HTTP status codes
- ✅ Cross-origin resource sharing (CORS) enabled
- ✅ Structured logging
- ✅ Database migrations with Flyway

## Technology Stack

- **Java 21**
- **Spring Boot 3.4.3**
- **Spring Data JPA**
- **PostgreSQL**
- **Flyway** (Database migrations)
- **Lombok** (Reduces boilerplate code)
- **Jakarta Validation**

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- PostgreSQL 12+

## Setup Instructions

### 1. Database Setup

1. Install and start PostgreSQL
2. Create a database named `postgres` (or update the configuration)
3. Update database credentials in `src/main/resources/application.properties` if needed

### 2. Application Setup

1. Clone the repository
2. Navigate to the project directory
3. Run the application:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 3. Database Migrations

The application uses Flyway for database migrations. Migrations will run automatically on startup.

## API Documentation

### Base URL
```
http://localhost:8080/api/v1/appointments
```

### Endpoints

#### 1. Get All Appointments
```http
GET /api/v1/appointments
```

**Response:**
```json
{
  "success": true,
  "message": "Appointments retrieved successfully",
  "data": [
    {
      "id": "uuid-here",
      "name": "John Doe",
      "phoneNumber": "1234567890",
      "dateTime": "2024-01-15T14:30:00",
      "createdAt": "2024-01-15T10:00:00",
      "updatedAt": "2024-01-15T10:00:00"
    }
  ],
  "timestamp": "2024-01-15T10:00:00"
}
```

#### 2. Get Appointment by ID
```http
GET /api/v1/appointments/{id}
```

#### 3. Search Appointments
```http
GET /api/v1/appointments/search?name=John
GET /api/v1/appointments/search?phone=1234567890
GET /api/v1/appointments/search?date=2024-01-15
GET /api/v1/appointments/search?startDate=2024-01-15T00:00:00&endDate=2024-01-15T23:59:59
```

#### 4. Create Appointment
```http
POST /api/v1/appointments
Content-Type: application/json

{
  "name": "John Doe",
  "phoneNumber": "1234567890",
  "dateTime": "2024-01-15T14:30:00"
}
```

**Validation Rules:**
- Name: 2-100 characters, required
- Phone Number: Exactly 10 digits, required, unique
- Date Time: Must be in the future, within business hours (9 AM - 6 PM)

#### 5. Update Appointment
```http
PUT /api/v1/appointments/{id}
Content-Type: application/json

{
  "name": "John Doe Updated",
  "phoneNumber": "1234567890",
  "dateTime": "2024-01-15T15:30:00"
}
```

#### 6. Delete Appointment
```http
DELETE /api/v1/appointments/{id}
```

### Error Responses

All endpoints return consistent error responses:

```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "timestamp": "2024-01-15T10:00:00"
}
```

## Business Rules

1. **Appointment Conflicts**: No two appointments can be scheduled within 30 minutes of each other
2. **Business Hours**: Appointments must be between 9:00 AM and 6:00 PM
3. **Phone Number Uniqueness**: Each phone number can only have one active appointment
4. **Future Dates Only**: Appointments cannot be scheduled in the past

## Development

### Running Tests
```bash
mvn test
```

### Building the Application
```bash
mvn clean package
```

### Running Database Migrations
```bash
mvn flyway:migrate
```

## Project Structure

```
src/main/java/com/gi/cuthair/
├── CutHairApplication.java              # Main application class
└── appointment/
    ├── controller/  
    │   └── AppointmentController.java   # REST controller
    ├── exception/                       # Custom exceptions
    │   └── AppointmentException.java
    ├── model/                           # Data Transfer Objects
    │   ├── ApiResponse.java
    │   ├── Appointment.java             # Entity class
    │   └── AppointmentRequest.java
    ├── repository/
    │   └── AppointmentRepository.java   # Data access layer
    └── service/    
        └── AppointmentService.java      # Business logic
        
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE.txt file for details. 