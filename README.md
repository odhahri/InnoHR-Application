
# InnovHR Desktop Application

InnovHR is a human resources desktop application developed in Java using JavaFX and Maven. The project utilizes Hibernate as its Object-Relational Mapping (ORM) tool and PostgreSQL as the backend database.

## Prerequisites

To successfully run this project, ensure you have the following installed:

- Maven (recent version)
- JDK (preferably LTS version, e.g., JDK 17 or earlier)
- Docker

## Setting Up Database

InnovHR uses Docker to manage its PostgreSQL database. Follow these steps to set up and run the database:

1. Run the following command to start the database containers:
   ```
   docker-compose up
   ```

2. Once the containers are up, open your web browser and go to:
   ```
   http://localhost:5050/browser/
   ```

3. Register a new server with the following details:
    - Name: postgres
    - Hostname: postgres
    - Port: 54320
    - Username: postgres

## Running the Application

Before running the server application, execute the following command to install required dependencies:

```
mvn clean install
```

After dependencies are installed, you can proceed to run the InnovHR desktop application.

---

