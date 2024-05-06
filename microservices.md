# Based on `microservices` Project

## What is a microservice?
- A possible definiton of microservices: 
    - Developing a single application based on **small autonomous** services that **work together**.
    - Each service **built around business capabilities**, thus each ***running in its own process and communicating with API or queue***
    - Each service **independently deployable** by fully automated deployment machinery
    - microservices **can use different languages and data storage tech**
- Not a rule but most commonly, REST and Cloud(to be able to dynamically adjust instances) is used for microservices

## Challenges with microservices
1. ***Bounded Context:*** 
    - The bounded context concept originated in ***Domain-Driven Design***: set boundaries within which specific terms have explicit meanings(Belirli terimlerin açık anlamlara sahip olduğu sınırları belirlemek). 
    - ***Just as words might have different meanings in different languages or cultures, software entities can assume different definitions in different contexts.***
    - In Microservices, the Bounded Context ensures that each microservice has its own distinct Context where all terms and entities have a clear, unambiguous meaning.  This autonomy avoids confusion and potential conflicts, ensuring each microservice performs its designated function without disruption.
    - Since bounded context defines clear boundaries, it ensures that services remain ***decoupled/independent***
    - ***Modularity***: each microservice adheres to its bounded Context and that individual modules can be updated, replaced, or scaled without impacting the entire system
    - ***Data isolation/encapsulation***: In a microservices architecture, bounded contexts aid in controlling data consistency and isolation. Every bounded context has a database schema and data model that are tailored to meet its unique needs.
    - ***Service autonomy***: 
2. ***Configuration management:***
    - Multiple microservices, each have multiple instances, each instance present in different environments
    - Very hard to do config for each instance at each environment
3. ***Dynamic scale up/down:***
    - Loads on different microservice will be different at a given time
    - Depending on the load at a particular microservice, new instances should be bring up or down automatically
3. ***Visibility:***
    - How can you find a bug in a certain functionality. But this functionality is distributed among multiple microservices. How are you going to trace/track the bug without centralized log?
    - How can I track the health of microservices?
4. ***Fault Tolerance***
    - Badly designed microservices might become a ***pack of cards:***
        - Imagine: MS1-->MS2-->MS3-->MS4
        - If MS3 is very crucial/fundemental but it goes down, entire app might go down 
        - thus it is very important to have fault tolerance in your microservices

more: https://www.sayonetech.com/blog/bounded-context-microservices/

## Advantages and Disadvantages of Microservices
### Advantages
- **Scalability**: Microservices allow for independent scaling of different services based on their specific needs, enabling efficient resource utilization.
- **Flexibility**: Services can be developed and deployed independently, allowing teams to work in parallel and adopt different technologies or frameworks as needed.
- **Fault Isolation**: If one service fails, it doesn't affect the entire application. The fault is isolated to that specific service, reducing the impact on the overall system.
- **Continuous Deployment**: Microservices support continuous integration and deployment practices, enabling faster delivery of new features and updates.

### Disadvantages
- **Increased Complexity**: Managing a distributed system of interconnected services introduces additional complexity, requiring robust service discovery, communication, and coordination mechanisms.
- **Operational Overhead**: Deploying and monitoring multiple services can be more challenging than managing a monolithic application, requiring effective infrastructure and orchestration tools.
- **Network Communication**: Inter-service communication introduces network latency and potential failure points, which must be carefully addressed.
- **Data Management**: Maintaining data consistency across different services can be complex, often requiring the implementation of distributed transactions 

## Advantages and Disadvantages of Monolithic
### Advantages
- **Simplicity**: The development process is straightforward, as the application is built as a single entity.
- **Easier deployment**: Deploying a monolithic application involves deploying a single artifact, simplifying the deployment process.
- **Simpler debugging**: Debugging is generally easier in monolithic applications since all code components are within the same codebase.

### Disadvantages
- **Limited Scalability**: Scaling a monolithic application can be challenging as the entire application needs to be scaled together, even if only specific components require additional resources.
- **Flexibility**: Making changes or introducing new technologies may be difficult, as modifications often impact the entire application.
- **Maintenance overhead**: Monolithic architectures can become complex and challenging to maintain, especially as the application grows in size and complexity.
- **Limited fault isolation**: Issues in one component can impact the entire application, leading to potential system-wide failures.

more: https://www.sayonetech.com/blog/microservices-vs-monolithic-which-choose/

## Single Responsibility Principle
- The ‘Single Responsibility Principle’ is the foundation of microservices architecture. It advocates that each microservice should have a single responsibility, focusing on a-  specific business capability. By adhering to ‘SRP,’ microservices become more ‘maintainable,’ ‘testable,’ and ‘scalable.’

more: https://www.sayonetech.com/blog/microservices-architecture-java/

## Spring cloud
- Used for quickly building common patterns in distributed systems
- Spring Cloud is not a single project, there are many projects under it: most important Spring Cloud Netflix
- Solves:
    - **config management**: 
        - Spring Cloud Config Server: centralized config management
        - store all config of all microservices in a centralized repository
        - this repo is exposed to all microservices
    - **dynamic scale up/down**
        - **naming server**: Eureka
            - service registration: all microservices register with other microservices
            - service discovery: provides available instances' URL to Ribbon
        - **load balancing**: Ribbon or Spring Cloud LoadBalancer
        - **writing simple REST clients***: Feign
            - use `@FeignClient(name="nameofMicroServiceYouWantToUse", url="urlOfMicroserviceYouWantToUse")` at class level to define a proxy
                - you need to use `@FeignClient(name="nameofMicroService"` and `RibbonClient(name="nameofMicroService")` if you want ribbon enabled (for load balancing).
            - define the method of the microservice you would like to communicate (you can get the method defition from the microservice's controller)
            - you can use this proxy class to call the other microsservice
    - **visiblity and monitoring**: implementing logging for each microservice with Netflix API Gateway
    - **fault tolerance**: help us to config a default response if microservice is down: Hystrix or Resilience4j

## Naming server
- if you create a new instance for a microservice you want this to be seen by Ribbon
- All microservices register at Eureka
- whenever a microservice want to talk to another microservice:
1. Microservice1: "I want to talk with microservice2" -> Eureka
2. Eureka searches for active instances of microservice2 ***(service discovery)***
3. Eureka returns the URLs of microservice2 instances
4. Ribbon, in client side, picks the instance URL based on availability
- You need to define a new repo for Eureka and annotate main class with `@EnableEurekaServer`. This will be your eureka server
- For each microservice you need to add `@EnableDicoveryClient` at main class and then define your eureka url at applicaiton.properties