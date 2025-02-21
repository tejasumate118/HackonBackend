# HackonBackend

A Spring Boot backend application for Hackon project.

## Prerequisites

- Java JDK 17 or higher
- Maven 3.6.x or higher
- Postgrsql 16 or higher
- IDE (IntelliJ IDEA recommended)

## Setup

1. Clone the repository:
```bash
git https://github.com/anii-123/Hackon_FourLoop
cd HackonBackend
```

2. Configure the database:
   - Create a MySQL database
   - Update `src/main/resources/application.properties` with your database credentials

3. Build the project:
```bash
mvn clean install
```

## Running the Application

### Using IDE
1. Open the project in your IDE
2. Run the main class `HackonBackendApplication`

### Using Command Line
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Environment Variables

The following environment variables need to be set:
- `SPRING_DATASOURCE_URL`: Database URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
``
