# Web Service

- **Definiton:** Software system designed to support ***interoperable*** ***machine-to-machine*** interaction over a ***network***. So there are 3 key features of web service:
    - ***interoperable***: platform independent: .NET web service should communicate with Java web service
    - ***machine-to-machine***: or app-to-app
    - interaction over a ***network***

- ***Web application:*** software program or application that is accessed and interacted with through a web browser over a network. It typically involves a user interface (UI)

## Interoperability
- Data exchange between web services are done by request-response
- To achieve interoperability; request-response should be platfrom independent, they should be in specific format:
    - XML
    - JSON
- How does an app A know the format of request-reponse, what to send as request, where to send the request to app B?
    - Solution is the ***Service Definiton***: every web service offers this
    - Service defition provides:
        - Request-response format
        - Request structure
        - Response structure
        - Endpoint

## Key Terminologies
- Service provider / Server
- Service consumer / Client
- Service definition: contract between provider and consumer
- Transport: how a web service is called
    - MQ
    - HTTP

## SOAP vs REST
- REST: defines an architectural approach
- SOAP: imposes restrictions on the format of request/response XML

## SOAP
- Defines a format of XML for response and request
- Needs this structure: SOAP-ENV: Envelop [ SOAP-ENV: Header  |  SOAP-ENV: Body ]
- No restriction on transport
- Service definiton is done by WSDL:
    - Endpoint
    - All operations
    - Request-reponse structure

## REST
- It wants to make best use of HTTP: RESTful web services try to define web services using the existing concepts in HTTP
- HTTP is an application layer protocol designed to transfer information between networked devices and runs on top of other layers of the network protocol stack.
- HTTP methods: GET, PUT, POST..
- HTTP status codes: 200, 404..
- REST does not define a definite format of request/response
- Only use HTTP as transport
- Doesn't have definite service definiton but Swagger is commonly used
- Key abstraction: ***resource***
    - anything that you want to expose to the world
    - each resource has an URI
    - You perform actions on the resource using HTTP concepts:
        - Ex: create a user: POST /users
