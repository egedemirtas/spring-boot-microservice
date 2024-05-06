# Based on `restful-web-services` Project

## How our requests are handled?
- When ever we talk about Spring MVC, we talk about ***DispatcherServlet*** (Front Controller Pattern)
- All our requests, irrelevant to URL, first go to DispatcherServlet.
- When you start your spring boot app, you can see that DispatcherServlet is mapped to route URL: `Mapping servlets: dispatcherServlet urls=[/], h2Console urls=[/h2-console/*]`
- Once DispatcherServlet gets the request, it would map the request to correct controller
- DispatcherServlet is configured by ***DispatcherServletAutoConfiguration*** (Auto Configuration is one of the most important feature of spring boot): based on the classes that are on the class path, Spring boot automatically detect that we are building a web app, therefore it automatically configures a DispatcherServlet. In the logs you can see 

```
DispatcherServletAutoConfiguration matched:
      - @ConditionalOnClass found required class 'org.springframework.web.servlet.DispatcherServlet'
```

## How does `List<User>` converted to JSON?
- `@ResponseBody`: 
    - `@RestController` has this annotation inside.
    - We are telling that the bean should be returned as is. When we tell this, with Auto Configuration of Spring boot, by default `JacksonHttpMessageConverters` is used. (you can see this in logs as well)

## How does error mapping is handled?
- if you try `http://localhost:8080/users/1`, a HTML error page is returned to UI, but how??
- This is also done by Auto Configuration: `ErrorMvcAutoConfiguration` (you can find it in logs. Also if you open this class, you can find the HTML codes in there)

## How are all jars become available? (Spring, Spring MVC, Jackson, Tomcat)
- It is because of ***Starter Projects*** (define dependencies quickly for commonly used features)
    - ex: just using `spring-boot-starter-web` is enough to develop a REST API. It includes Spring, Spring MVC, Jackson, Tomcat
- With Auto Configuration, all these will be auto configured

## How to build generic exception handling for API?
- First look at how exception handling is implemented by Spring: `ResponseEntityExceptionHandler`
- This is the class that handles all Spring MVC raised exceptions
- Go to `CustomResponseEntityExceptionHandler` in the project for more info:
    - This class extends from `ResponseEntityExceptionHandler`
    - In here we specify the exception we want to handle: (`@ExceptionHandler(UserNotFoundException.class)`) and create custom methods for them

## How to add validation for the requests that are coming into your REST API?
- add `spring-boot-starter-validation`
1. Add `@Valid` to the object(in project it is `User`) you want to be validated: `UserController.createUser` in project
2. Add validation annotations to `User`: `@Size`, `@Past` ...
3. At this point validation will work but the response message will be very dirty/crowded almost like a error trace
    - add message to your validations: `@Size(min=2, message = "name length should be at least 2 characters")`
    - you can display this message by using `ex.getFieldError().getDefaultMessage()` in `CustomResponseEntityExceptionHandler`

## REST API Documentation
- Use swagger:
    - Ensure consistency of documentation
    - makes documentations upto date
    - automatic: generate documentation from code
- add the following dependency to your pom.xml: https://springdoc.org/
- go to http://localhost:8080/swagger-ui/index.html
- Swagger:
    - specification and UI to document REST API
- OpenAPI:
    - based on swagger
    - built as an standard specification for REST API
    - swagger tools still exists

## Content Negotiation
- Considering REST, each resource has a URI. BUT representation of a resource might change
- When you go to `http://localhost:8080/user-api/users/2`: consumer might want it in JSON or XML; English or Arabic..
- How can a consumer tell provider what they want? --> ***Content Negotiation***
    - Consumer can use "accept header" (MIME types - application/xml, application/json)
        - add this to your pom.xml: `com.fasterxml.jackson.dataformat`
        - the only thing you need to do is to go to Postman and change "Accept" in header to `application/xml`
    - Consumer can use "accept-language header" (en, nl, fr)
        - you need to create `messages.properties` for EN, `messages_nl.properties` for NL...
        - need to autowire `MessageSource` in `GreetingController`, implementation is in `GreetingController.helloWorldInter`
        - You need to add `Accept-Language=NL` to HTTP header in Postman

## Why Do We Need Versioning for REST API?
- Assume that you have many consumers. But you need to implement a breaking change: split `Person.name` to `Person.Name.firstName` and `User.Name.lastName`. You can not change the existing API because all of existing consumers will be effected, they will need to update their code immediately.
- This is why we have versioning; consumer can use v1 while making changes for v2, then they can switch to v2 REST API.
- How to achieve versioning:
    - URI versioning; directly include them in your URI: `localhost:8080/v1/person`
    - Use request parameter: `localhost:8080/person?version=1`
    - Headers versioning: add version to header of request: Ex: `X-API-VERSION=1` (URI doesnt change only request header changes)
    - Media type versioning: using ***Content Negotiation*** with "produces" (URI doesnt change only request header changes)
- Codes for each are in `VersioningController`
- Each approach has its own limitations:
    - Using URI versioning or request parameter versioning cause ***URI pollution***
    - Misuse of HTTP headers:HTTP headers are not meant to be used for versioning
    - Caching: typically caching is done by URL. URL doesn't change in header versioning or media type versioning; thus header versioning and media type versioning are not cachable
    - You can not execute the request on the browser with header versioning or media type versioning
    - API Documentation: typically API doc generation tools doesn't differentiate between headers but URL. So generating doc for header versioning or media type versioning is hard.

## HATEOAS
- Enhancing your REST API to tell customers how to perform subsequent actions(using links)
- Use **HAL (JSON Hypertext Application Language)**: simple format that provides hyperlink between resources in your API
- To be able to generate such responses, use ***Spring HATEOAS***: add to pom: `spring-boot-starter-hateoas`
- Implementation is in `UserController.getUserByIdHATEOAS`

## Customizing REST API Responses
- Serialization: 
    - convert object to stream
    - most popular JSON serialization in java: Jackson
1. Customizing field names in response: `@JSONProperty` (example code is in `User.name`)
2. Return only selected fields:
    - **Static filtering:** 
        - Use case: You might want to filter "password" filter for all your REST APIs
        - `@JsonIgnoreProperties`,`@JsonIgnore`
    - **Dynamic filtering:** 
        - Use case: You might want to filter a response field in one API but not in others
        -`@JsonFilter`
- Example code is in `FilteringController` and `Student` and `Teacher`

## Monitoring APIs with Actuator
- ***Spring boot Actuator:*** provides Spring Boot's production ready features: monitor and amange your app in production
- add to pom: ``
- Actuator provides:
    - **Beans:** complete list of all beans in your app
    - **Health:** app health info
    - **Metrics** 
    - **Mappings:** details around request mappings
- Visit `http://localhost:8080/actuator` to see all, remember that by default only **health** is shown. To include more add this to your `application.properties`: `management.endpoints.web.exposure.include=*`
- As an example on how to use fields in the actuator:
    1. Go to metrics section
    2. you will see `application.ready.time`
    3. copy and paste it to URL as: `http://localhost:8080/actuator/metrics/application.ready.time`

## How to Explore APIs with HAL Explorer
- HAL Explorer is an API explorer for REST APIs that are using HAL
- Enables non-tech teams to play with APIs
- add to your pom: `spring-data-rest-hal-explorer`
- go to `http://localhost:8080/` (it will automatically direct to HAL explorer)

## Implementing H2 DB
- H2 is in memory DB, thus it is very fast but shouldn't be used to persist data, as in-memory DB is volatile
- add this to `application.properties`: 
```properties
#to see the sql in logs
spring.jpa.show-sql=true

spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.enabled=true
```
- go to `http://localhost:8080/h2-console`, and set "JDBC URL" to `jdbc:h2:mem:testdb`
- in the current state of the project, H2 is disabled and MySQL is enabled

## Security
- add to pom: `spring-boot-starter-security`
- after you restart your app, in the logs you will see `Using generated security password: 1dc2a98e-a3dd-4697-8ffe-52b153f7bb47`
- add the password as authorization to your request headers, by default: username=user, password=1dc2a98e-a3dd-4697-8ffe-52b153f7bb47
- This password will be regenerated for each build, to prevent this, add these to `applciation.properties`:
```yaml
spring.security.user.name=ege
spring.security.user.password=password123
```
- At this state only GET requests will work, why?
    - Whenever you send a request, Spring security will intercept the request and execute series of filters: ***Filter Chains***
- ***Filter Chains***
    1. All requests are authenticated
    2. If a request is not authenticated, login web page is shown
    3. CSRF: this filter disables POST and PUT. This is why only GET works right now
- In order to solve 3rd issue and improve 2nd issue by making login as a popup: we create a configuration file to override the existing chain: visit `SpringSecurityConfig`
- In `SpringSecurityConfig`, CSRF is disabled inorder to enable POST/PUT. However this is not the best practice, CSRF is here to protect hacking.

___

## BONUS

### Docker
- to create mysql image (might be outdated): 
```
docker run --detach 
--env MYSQL_ROOT_PASSWORD=dummypassword 
--env MYSQL_USER=social-media-user 
--env MYSQL_PASSWORD=dummypassword 
--env MYSQL_DATABASE=social-media-database 
--name mysql 
--publish 3306:3306 mysql:8-oracle
```

### Switch from H2 to MySQL (in docker)
- delete H2 dependency from pom
- add to pom: 
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```
- `application.properties`: 
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/social-media-database
spring.datasource.username=social-media-user
spring.datasource.password=dummypassword
spring.jpa.hibernate.ddl-auto=update #(spring boot auto config will create tables for entities for H2, but won't create tables for MySQL, thus we need this)
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect (recommended if you use MySQL v5+)
```


### How to use `lombok` and `mapstruct` at the same time
- add these dependencies
```xml
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>1.5.5.Final</version> <!-- Use the latest version -->
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```
- add these plugins for annotation processing:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <annotationProcessorPaths>
            <path>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
                <version>1.5.5.Final</version>
            </path>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok-mapstruct-binding</artifactId>
                <version>0.2.0</version>
            </path>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>1.18.30</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```