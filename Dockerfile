FROM eclipse-temurin:17-jdk
VOLUME /tmp
EXPOSE 5000
COPY target/be-code-challenge-barzilai-0.0.1-SNAPSHOT.jar barzilai-challenge.jar
ENTRYPOINT ["java","-jar","/barzilai-challenge.jar"]