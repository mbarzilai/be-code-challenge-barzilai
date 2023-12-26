# be-code-challenge-barzilai

## Pre-requisites
- JDK 17
- maven 3
- docker CLI
- (optional) postgresql

## Building the application
To build the jar file, run the following command
```bash
# package the jar file using maven
mvn clean package -DskipTests
```

## Running the application
### Run with Docker
To run the application in docker, including starting up a new postgresql container:
1. Build the application jar (see [Building the application](#building-the-application))
2. Run the application and the postgresql database using docker compose
    ```bash
    docker compose up
    ```
3. Access the application at http://localhost:5000

### Run locally with maven
1. Start a postgresql server hosted locally on port 5432
2. If running locally for the first time, create a database cluster called `spothero_rates`.
   - There are multiple ways to do this. Using the psql command would look like:
   ```bash
   psql
   create database spothero_rates;
   ```
3. Run the application using the maven command
    ```bash
   mvn spring-boot:run
    ```
4. Access the application at http://localhost:5000

## Testing the application
Docker must be running for tests to work.  
To run the test specs, use the following command:
```bash
mvn verify
```

## Documentation
The endpoints are documented using the OpenAPI Specification.   
- The complete api docs json file can be found in [src/main/resources/static/openapi.json](src/main/resources/static/openapi.json).
- Additionally, the api docs can be viewed while the application is running at http://localhost:5000/v3/api-docs
- The Swagger UI can also be used to view the api documentation and test out the endpoints at http://localhost:5000/swagger-ui/index.html