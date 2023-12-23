## Running the application
### Run with Docker
```bash
# package the jar file using maven
mvn clean package -DskipTests
# run app with postgres database using docker compose
docker compose up
```