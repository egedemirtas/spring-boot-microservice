FROM maven:3.8.5-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package
ENTRYPOINT ["java","-jar","/home/app/target/api-gateway-0.0.1-SNAPSHOT.jar"]