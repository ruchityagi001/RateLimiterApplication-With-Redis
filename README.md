# RateLimiterApplication-With-Redis

### Distributed Rate Limiter (Spring Boot & Redis)

A high-performance, distributed rate-limiting solution built with Spring Boot and Redis. It prevents API abuse by limiting requests per user using the Token Bucket Algorithm.

### Tech Stack
Java 17 / Spring Boot 3

Redis (Upstash): Distributed state management.

Bucket4j: Rate limiting library.

Lettuce: Scalable Redis client.

### Core Architecture
This implementation uses Redis to synchronize request counts across multiple server instances.
1. Interceptor: Intercepts incoming API requests.

2. Redis Config: Connects to the remote Upstash instance.

3. Logic: Uses Token Bucket. It allows for "burstiness" (filling a bucket with tokens) while maintaining a steady average rate. Checks the "bucket" in Redis; if tokens are available, the request proceeds; otherwise, it returns 429 Too Many Requests.

#### Why Redis?
In a cloud environment with multiple instances, Redis ensures that a user’s limit is respected globally, regardless of which server instance they hit.

### Environment Setup
To run this project securely:
1. Get Credentials: Create a free Redis instance on Upstash.
2. Set Variables: Instead of hardcoding, it is best practice to set your URL as an environment variable:
      export SPRING_REDIS_URL=rediss://default:your_password@your_url
3. Run: Use ./mvnw spring-boot:run.

### API Documentation & Response
The limiter protects all endpoints via a HandlerInterceptor.

| Feature            |         Deatils         |
|--------------------|:-----------------------:|
| Protected Endpoint |  GET /api/v1/resource   |
|  Success Response  | 200 OK (Token consumed) |
|    Limit Exceeded | 429 Too Many Requests   |

