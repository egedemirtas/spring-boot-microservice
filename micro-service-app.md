# Building of Example Microservice App

## Building a Service That Takes Configs From Spring Config Server
- full structure is: Limits service --> config server --> git repo
- Limits service will use the values present in git repo's `application.properties`

### Creating Limits Service
- Select Spring Web, DevTools, Actuator, Config Client from spring initializr
- Limits service will use the configs from config server
- How to use the variables from config server? (at this stage config server is not present)
    1. Create controller class: `LimitsController`, 
    2. create a GET method, this should return `Limit`
    3. Create a config class: `Configuration`, autowire this in `LimitsController`
    4. `Configuration` should have `@ConfigurationProperties("limits-service")` to use the values from git repo's `limits-service.properties`
        1. Where does `"limits-service"` come from in the annotation? Notice that in limits-service->`application.properties` a line is automatically added `spring.application.name=limits-service` when we created the project from spring initializr

### How to Connect Limits Service to Spring Cloud Config Server 
- add to `application.properties`:
```properties
spring.config.import=optional:configserver:http://localhost:8888
```
- right now we don't have config server, so you should add `optional:`
- Our config server will be on port 8888

### Creating Config Server
- Select DevTools, Config Server from spring initializr
- add to `application.properties`:
```properties
server.port=8888
```
- add `@EnableConfigServer` in main

### Creating Git Repo
- Our config server will be connected to a git repo
- Git repo's name is **spring-boot-ms-git-repo**
- Create a file called `limits-service.properties`, and name the configs that you want to use:
```properties
limits-service.minimum=2
limits-service.maximum=88
```
- Notice that:
    - our file's name matches our microservice name: `limits-service`
    - you have to specify the application's name before the config
- Bonus: what happens if we have the same config in limits-service->`application.properties`?
    - Spring cloud config server's values take precedence, our app will use cloud config's values
- push the repo

### How to Connect to Spring Cloud Config Server to Git Repo
- Go to config server and add to `application.properties`:
```properties
spring.cloud.config.server.git.uri=https://github.com/egedemirtas/spring-boot-ms-git-repo

# For local development:
#spring.cloud.config.server.git.uri=file:///Users/egedemirtas/Desktop/projects/spring-boot-ms-git-repo
```
- Try it out: `http://localhost:8888/limits-service/default`

### Adding Profiles to Git Repo
- Notice that we can get the configs with this url `http://localhost:8888/limits-service/default`, we don't want "default"
- You would want to use different configs in different environments. Thus, create new files in git repo for each environment:
    - `limits-service-dev.properties`
    - `limits-service-uat.properties`
    - `limits-service-live.properties`
- Now you can try: `http://localhost:8888/limits-service/live`: notice that you get live and default at the same time. You should keep in mind that, for this URI live takes precedence over default's configs. Same goes for dev/uat URIs
- How to tell limits-service to get config from dev/uat/live? Add to limits-service->`application.properties`:
```properties
spring.profiles.active=dev
```

___

# Building Currency Conversion App
- Currency Conversion MS --> Currency Exchange MS --> DB
- Currency Exchange Microservice: This service provides us the exchange rate of one currency to another
- Currency Conversion Microservice: This service converts one currency to another using exchange microservice

## Setting Up Currency Exchange Microservice
- What we need: dev tools, actuator, web, config client, jpa, H2
- We will use port:8000, this should be set in properties file
- Simple GET and response example:
```json
// http://localhost:8000/currency-exchange/USD-INR

{
   "id":10001,
   "source":"USD",
   "target":"INR",
   "rate":65.00,
   "environment":"8000 instance-id"
}
```
- In the above code there is a field called `"environment"`. This field should return the current instance of Currency Exchange Service and its port. remember that there may be multiple instances of this service, depending on the load.

### How to Set and Return Current Port
- Spring provides `Environment` class you can autowire it: `CurrencyExchangeController`
- Can get current port with: `environment.getProperty("local.server.port")`
- How to create multiple instances of app in local machine:
    - In intellij you need to create 2 application configs
    - You can run one of them directly. In our example we set our port to 8000 in `application.properties`
    - For the other you need to override the port in `application.properties` using ***VM options*** as `-Dserver.port=8001`
    - Now you can go to `http://localhost:8000/currency-exchange/USD-TRY` or `http://localhost:8001/currency-exchange/USD-TRY`

___

# Building Currency Exchange App
- Created with the same settings as in currency-exchange-service and also **spring cloud's open feign** (to be able to call conversion exchange service)
- Port: 8100
- Simple GET and response example:
```json
// http://localhost:8100/currency-conversion/USD-INR/10

{
  "id": 10001,
  "source": "USD",
  "target": "INR",
  "rate": 65.00,
  "amount": 10,
  "calculatedamount": 650.00,
  "environment": "8000 instance-id"
}
```

## How to call Currency Exchange Service?
- make sure that openfiegn dependency is added to pom
- mark your main with `@EnableFeignClients`
- Create a proxy class as such:
```java
// we use the application name of the service we want to use
@FeignClient(name = "currency-exchange-service", url="localhost:8000")
public interface CurrencyExchangeProxy {
    @GetMapping("/currency-exchange/{source}-{target}")
    public CurrencyConversionDto getExchangeRate(@PathVariable String source, @PathVariable String target);
}
```
- Notice that this method is direct copy from currency-exchange-service->`CurrencyExchangeController`. However, its response type is different:`CurrencyConversionDto`. Automatic mapping is done from `CurrencyExchangeDto` to `CurrencyConversionDto`. In order to achieve this field names must match.
- By autowiring `CurrencyExchangeProxy`, you can use `getExchangeRate` (make sure that currency-exchange-service is running in port:8000)
- notice that port number is not dynamic

___

# Setting Up Eureka Naming Server
- Created with dev tools, actuator, eureka server
- add to main: `@EnableEurekaServer`
- port is 8761
- add this to `application.properties`:
```properties
# prevent naming server to register itself
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```
___

# Registering Currency Exchange and Conversion Services with Naming Server
- Add these to their pom:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```
- Optinal: you can specify the eureka url that you want your service to register with
```properties
# just to be safe specify the eureka url
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka
```
- Now run naming server then your services. you will see that they will be registered in `http://localhost:8761`

# Implementing Load Balancing
- Go to currency-coversion-service -> `CurrencyExchangeProxy` and change the feign annotation to `@FeignClient(name = "currency-exchange-service")`
- Now we should change from h2 to mysql:
    1. For docker:
        1. `pull mysql:latest`: image name will be `mysql` and tagged with `latest`
        2. ```docker
            docker run 
            --name curr-mysql-db #create a container named mysql-new
            -e MYSQL_ROOT_PASSWORD=dummypassword 
            -e MYSQL_USER=currency-user 
            -e MYSQL_PASSWORD=dummypassword 
            -e MYSQL_DATABASE=currency-database
            -p 3306:3306 #should be published in port 3306
            -d mysql:latest #Container should be based on mysql:latest
            ``` 
            `docker run --name curr-mysql-db -e MYSQL_ROOT_PASSWORD=dummypassword -e MYSQL_USER=currency-user -e MYSQL_PASSWORD=dummypassword -e MYSQL_DATABASE=currency-database -p 3306:3306 -d mysql:latest`
    2. In currency-exchange-service delete H2 dependencies and properties. Add mysql dependency and property:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/currency-database
    spring.datasource.username=currency-user
    spring.datasource.password=dummypassword
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
    ```
___

# Creating API Gateway
- Provides cross-cutting concerns to those APIs such as security, monitoring/metrics, and resiliency. We need these common features for all of our microservicess
- We need: DevTools, actuator, eureka client, gateway
- Port is 8765
- Modify gateway dependency as:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```
- Add these to properties:
```properties
# to enable gateway
spring.cloud.gateway.discovery.locator.enabled=true
# to enable lower case access uri
spring.cloud.gateway.discovery.locator.lower-case-service-id=true
```
- We can access to the services with following format: `http://localhost:8765/service_name_in_eureka/api_uri`
- you can now call every microservice that has been registered with Eureka through api gateway.
- If you want to implement authentication, you can do it so in api-gateway. Only authenticated requests will go through gateway

## Creating Custom Routes in Gateway
- Look at api-gateway->`ApiGatewayConfig`
- Delete/disable these properites:
```properties
#spring.cloud.gateway.discovery.locator.enabled=true
#spring.cloud.gateway.discovery.locator.lower-case-service-id=true
```
- Now you can reach to: `http://localhost:8765/currency-conversion/TRY-USD/10`

## Adding Loging Filter in Gateway
- Look at api-gateway->`LoggingFilter`

___

# Implementing Circuit Breaker Mechanism With Resilience4j
- Assume a system: MS1->MS2->MS3: If one service is down/slow it will impact entire chain
- We need to be able to:
    1. return a fallback response if a service is down
    2. Implement Circuit Breaker Pattern to reduce load (if a microservice is slow, instead of repeatedly calling it and causing t to go down, we can return default message without calling repeatedly)
    3. Retry request incase of temporary failures
    4. Implement rate limiting: limiting calls to a microservice in a period of time
- Need to add few dependencies to currency-exchange-service: `https://resilience4j.readme.io/docs/getting-started-3`
- Go to currency-exchange-service-> `RetryController`, `CircuitBreakerController`, `RateLimiterController`, `BulkheadController` for more info
- How circuit breaker works: `https://resilience4j.readme.io/docs/circuitbreaker`, but typical flow is:
    1. CircuitBreaker starts at ***Closed state***
    2. If request failure is above threshold, CircuitBreaker switches to ***Open state***
    3. CircuitBreaker waits at ***Open state*** for some time
    4. CircuitBreaker switches to ***Half-open state***
    5. At ***Half-open state***, CircuitBreaker will send only a percentage of requests to the api:
        1. If successfull response is returned, CircuitBreaker switches to ***Closed state***
        2. If failure response is returned, CircuitBreaker switches to ***Open state***
___

# Distributed Tracing
- Microservices have complex chain and it becomes hard to debug problems and trace request. ***Distributed tracing*** solves this
- All the microservices involved would send info to a single distributed tracing server. This server sotres everything to a DB
- Docker command for Zipkin: `docker run -p 9411:9411 openzipkin/zipkin:latest`
- Zipkin access: `http://localhost:9411/zipkin/`

## OpenTelemetry
- Collection of tools, APIs, SDKs to instrument, generate, collect & export telemetry data (metrics, logs, traces)
- One standard for metrics, logs, traces

## Linking Currency Exchange/Conversion Service And API Gateway to Zipkin Server
- Add to pom:
```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-observation</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing-bridge-otel</artifactId>
</dependency>
<dependency>
    <groupId>io.opentelemetry</groupId>
    <artifactId>opentelemetry-exporter-zipkin</artifactId>
</dependency>
```
- For performance reasons you would want to limit the logs (1.0 is max):
```properties
management.tracing.sampling.probability=0.05
```
- For log formatting:
```properties
logging.pattern.level=%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]
```
- Go to `http://localhost:8765/currency-conversion/TRY-USD/10` (using API gateway) and on Zipkin you will see the flow of your request from API gateway to currency conversion service
- You will notice that the GET request of currency conversion service won't log the GET request of currency exchange service. This is because currency echange service is being used as feign. You need this dependency in currency conversion service (or in any service that you use `@FeignClient`):
```xml
<dependency>
    <groupId>io.github.openfeign</groupId>
    <artifactId>feign-micrometer</artifactId>
</dependency>
```
___

# Dockerizing Services
1. You need to create latest versions of your application (.jar) with `mvn clean install`
2. Create `Dockerfile` in each project's path. 
    - `Dockerfile`: It is the place where we config the model of our docker container. By using dockerfile we can create docker image.
    - `Dockerfile` should be like:
        ```docker
        FROM maven:3.8.5-openjdk-17 AS build
        COPY src /home/app/src
        COPY pom.xml /home/app
        RUN mvn -f /home/app/pom.xml clean package
        #EXPOSE 8080
        ENTRYPOINT ["java","-jar","/home/app/target/naming-server-0.0.1-SNAPSHOT.jar"]
        ```
        - `FROM`: Fetching latest version of Java image with maven. This pre define docker image exists on docker hub.
        - `COPY`: Copying Project src folder to openjdk-17 container’s root directory /home/app/src.Copy again pom.xml file to /home/app/.
        - `RUN`: Execute the mavean command to build the .jar file accoring to given pom.xml file.
        - `EXPOSE`: Specify that expose server port
        - `ENTRYPOINT`: Execute command for run the .jar file. We can use CMD instead of ENTRYPOINT. If we use CMD we can provide arguments to image when build it.
3. Create images with commands: 
    ```docker
    docker build -t currency-exchange:0.0.1-SNAPSHOT .
    docker build -t currency-naming-server:0.0.1-SNAPSHOT .
    docker build -t currency-conversion:0.0.1-SNAPSHOT .
    docker build -t currency-api-gateway:0.0.1-SNAPSHOT .
    ```
    - The `-t` flag tags your image. Think of this as a human-readable name for the final image. Since you named the image getting-started, you can refer to that image when you run a container.
    - The `.` at the end of the docker build command tells Docker that it should look for the Dockerfile in the current directory.
4. Need to create `docker-compose.yaml`; if your container is in relationship with other containers.
    - currency-exchange-service needs mysql
    - All of our services need name-server
    - `Docker Compose` : Docker compose is a tool which helps us to easily handle multiple containers at once.
        - `version`: Version of Docker Compose file format.
        - `services`: My application has two services: app (Spring Boot) and mysqldb (MySQL database image).
        - `build`: Configuration options that are applied at build time that we defined in the Dockerfile with relative path
        - `image`: Official Docker image from docker hub
        - `volumes`: Named volumes that keeps our data alive after restart.
        - `network`: The two services should be belong to one network.
        - `depends_on`: Dependency order, mysqldb is started before app
        - Look at `docker-compose.yaml` file in project directory, run it wih `docker compose up`

___

# Setting Up Your App in Kubernetes

1. Follow `kubernetes.md` to create cluster
2. Install `gcloud`
3. Install `gke-gcloud-auth-plugin`
4. Install `kubectl`
5. Connect to your cluster (you can find the command from google cloud: `gcloud container clusters get-credentials autopilot-cluster-1 --region us-central1 --project hopeful-sound-421912`)
6. `kubectl version`: should show client and server version

## Setting Up Currency Exchange Service for Kubernetes
- Remove all spring cloud related components: Eureka, Api Gateway. Kubernetes provides you these for free

gcloud container clusters get-credentials autopilot-cluster-1 \
    --region=us-central1

___

# BONUS

## Inheritance in Entity
- Suppose you want to have `StudentEntity` and `TeacherEntity` which extends from `BaseEntity`
- You want seperate tables for `StudentEntity` and `TeacherEntity` but no table for `BaseEntity`
- Solutions:
    1. Make `BaseEntity` an entity and add this to `BaseEntity`: `@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)`
    2. Don't make `BaseEntity` an entity but annotate it as `@MappedSuperclass`
- Example is in currency-exchange-service->`BaseEntity`

## Auditing Entities
- `StudentEntity` extends from `BaseEntity`; which has common columns for auditing: createdDate, createdBy, updatedDate, updatedBy etc
- For createdDate and updatedDate:
    - add annotation to `BaseEntity`; `@EntityListeners(AuditingEntityListener.class)`
    - You can now use `@PrePersist` and `@PreUpdate` etc:
    ```java
    @PrePersist
    private void prePersistCreateAt(){
        this.createdAt = LocalDateTime.now();
    }
    ```
- for createdBy and updatedBy you need Spring security: `https://www.baeldung.com/database-auditing-jpa`
