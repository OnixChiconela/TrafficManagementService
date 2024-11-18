**Traffic Management Service**
 - This project is a Traffic Management Service built with Spring Boot.It provides functionalities to manage and monitor traffic,
   include rate limiting and service health status checking.The service is designed to be scalable and easy to integrate with other systems

**Features**
 - **Rate Limiting**: Ensures that the number of request within a certain time frame does not exceed predefined limits. This helps
 - in protecting the service from abuse and maintaining performace.

 - **Service Health Status**: Provides endpoints to check the status and health of the service.

 - **In-Memory Database**: Uses H2 in-memory database for developmentt and testing purposes, making it easy to set up and run

**Technologies Used**
 - **Java 17**
 - **Spring Boot**
 - **Spring Web**
 - **Spring Boot Actuator**
 - **Resilience4j**: for rate limiting and circuit breaker patterns.
 - **Bucket4j**: For advanced rate liminting
 - **SQL Database**: structured data management, robust querying capabilities, for efficient application data handling.

**Prerequisites**
 - **JDK 11 or later*
 - **Maven 3.6.0 or later*
   
