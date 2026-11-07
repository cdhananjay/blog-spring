# Blog

A REST API for a blog, built with Spring Boot.

## Tech stack

- Java 21 · Spring Boot 3.4 (Web, Data JPA, Security, Validation)
- PostgreSQL (via Docker Compose), H2 for tests
- Lombok · MapStruct · JWT (jjwt)

## Getting started

Prerequisites: JDK 21 and Docker.

```bash
# Start PostgreSQL (and Adminer on http://localhost:8888)
docker compose up -d

# Run the app on http://localhost:8080
./mvnw spring-boot:run

# Run tests
./mvnw test
```

## API

All endpoints are under `/api/v1`.

| Method | Path | Description |
| --- | --- | --- |
| POST | `/auth` | Log in and receive a JWT |
| GET | `/posts` | List posts |
| GET | `/posts/{id}` | Get a post |
| GET | `/posts/drafts` | List draft posts |
| POST | `/posts` | Create a post |
| PUT | `/posts/{id}` | Update a post |
| DELETE | `/posts/{id}` | Delete a post |
| GET | `/categories` | List categories |
| POST | `/categories` | Create a category |
| DELETE | `/categories/{id}` | Delete a category |
| GET | `/tags` | List tags |
| POST | `/tags` | Create a tag |
| DELETE | `/tags/{id}` | Delete a tag |

Send the JWT as `Authorization: Bearer <token>`.
