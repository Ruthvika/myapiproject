# My API Project

A simple Spring Boot REST API project built with Java and Maven.

## Project Structure

```
my-api-project/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/api/
│   │   │       ├── ApiApplication.java
│   │   │       └── controller/
│   │   │           └── HelloController.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
└── pom.xml
```

## Prerequisites

- Java 17 or later
- Apache Maven 3.6+

## Building the Project

1. **Install Maven** (if not already installed):
   - Download from: https://maven.apache.org/download.cgi
   - Add Maven bin directory to your system PATH

2. **Build the project**:
   ```bash
   mvn clean compile
   ```

3. **Package the project**:
   ```bash
   mvn clean package
   ```

## Running the Application

After building, run the application using:

```bash
mvn spring-boot:run
```

Or run the JAR file directly:

```bash
java -jar target/my-api-project-1.0.0.jar
```

The application will start on `http://localhost:8080`

## API Endpoints

### 1. Hello Endpoint
- **URL**: `GET /api/hello`
- **Query Parameter**: `name` (optional, defaults to "World")
- **Example**: `http://localhost:8080/api/hello?name=Spring`
- **Response**: `Hello, Spring!`

### 2. Status Endpoint
- **URL**: `GET /api/status`
- **Response**: `{"status": "API is running"}`

## Project Details

- **Java Version**: 17
- **Spring Boot Version**: 3.2.0
- **Build Tool**: Maven
- **Packaging**: JAR

## Dependencies

- Spring Boot Starter Web
- Spring Boot DevTools
- Spring Boot Starter Test

## Development

The project uses Spring Boot DevTools for automatic restart during development. Simply run with `mvn spring-boot:run` and changes to source files will trigger an automatic restart.

## Next Steps

1. Install Maven on your system
2. Open the project in VS Code
3. Build using: `mvn clean compile`
4. Run using: `mvn spring-boot:run`
5. Test the endpoints using a REST client (Postman, curl, etc.)
