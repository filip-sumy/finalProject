# Appliance Store

Spring Boot application for managing an appliance store: catalog, orders, role-based access for staff and clients. Includes a Thymeleaf web UI and a stateless JWT REST API.

## Quick start

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Open http://localhost:8080

Run tests:

```bash
mvn test
```

## Demo accounts

### Database users (email login)

| Role     | Username / email     | Password |
|----------|----------------------|----------|
| Admin    | `admin@test.com`     | `password` |
| Employee | `employee@test.com`  | `password` |
| Client   | `john@test.com`      | `password` |

### In-memory fallback (short username login)

Used when the username is **not found in the database**. These are separate accounts — not aliases for the emails above.

| Role     | Username   | Password       |
|----------|------------|----------------|
| Admin    | `admin`    | `Admin123!`    |
| Employee | `employee` | `Employee123!` |

**Web login:** enter the username or email in the **Username** field on `/login`.

**API login:** JSON field `username` accepts either an email or a short in-memory username.

> Do not use `employee@test.com` with `Employee123!` — that email exists in the database with password `password`. In-memory credentials only work with usernames `admin` / `employee`.

## User flows

### Client
1. Sign in as a client
2. **Shop** — browse products and add items to the cart
3. **Cart** — review items and place an order
4. **My Orders** — order history; edit or delete orders until they are approved

### Staff (employee / admin)
- CRUD for clients, manufacturers, and appliances
- Create orders on behalf of clients
- **Approve** orders (deducts stock)
- Admin additionally manages employees

## REST API (JWT)

The web UI uses form login and sessions. The API under `/api/v1/**` is stateless and uses Bearer tokens.

### Obtain a token

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "john@test.com",
  "password": "password"
}
```

In-memory example:

```json
{
  "username": "employee",
  "password": "Employee123!"
}
```

Response:

```json
{
  "token": "eyJ...",
  "email": "john@test.com",
  "roles": ["ROLE_CLIENT"]
}
```

### Example requests

```http
GET /api/v1/appliances
Authorization: Bearer <token>

GET /api/v1/orders?page=0&size=10
Authorization: Bearer <token>

POST /api/v1/orders
Authorization: Bearer <token>
Content-Type: application/json

POST /api/v1/orders/{id}/approve
Authorization: Bearer <token>
```

- Clients see only their own orders (list and get-by-id)
- Order approval is restricted to `ADMIN` / `EMPLOYEE`
- Invalid credentials return **401**; invalid JWT returns **401**

## Tech stack

- Java 17, Spring Boot 4, Spring Data JPA, Spring Security
- Thymeleaf + i18n (English / Ukrainian)
- H2 (dev), PostgreSQL (prod)
- JWT (jjwt), BCrypt, ModelMapper, Lombok, AOP logging

## Profiles

| Profile | Database        | Purpose        |
|---------|-----------------|----------------|
| `dev`   | H2 in-memory    | local dev, tests |
| `prod`  | PostgreSQL      | production     |

Production:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

Required environment variable in production:

- `JWT_SECRET` — Base64-encoded key (minimum 256 bits)

## Project structure

```
controller/              MVC controllers (Thymeleaf)
api/controller/          REST controllers
api/security/            JWT filter, JwtService, ApiSecurityConfig
service/                 Business logic
service/cart/            Session-scoped shopping cart
repository/              JPA repositories + custom @Query
dto/                     Form and display models
api/dto/request|response API DTOs
entity/                  JPA entities
mapper/                  Entity ↔ DTO mapping
security/                Form login, RBAC, OrderSecurity, in-memory users
exception/               Custom exceptions + i18n resolution
resources/sql/           Seed data (no CommandLineRunner)
```

## Security

- **Dual authentication:** database (email) + in-memory fallback (`admin`, `employee`)
- **RBAC:** `ADMIN`, `EMPLOYEE`, `CLIENT`
- **URL rules** + `@PreAuthorize` + order ownership checks (`OrderSecurity`)
- **Separate filter chains:** JWT for `/api/**`, form login for the web UI
- **BCrypt** password hashing and strong password policy for new users
- **CSRF** enabled for web forms (automatic via Thymeleaf `th:action`); disabled for API
- Stack traces and internal error details hidden from users
- Logging: login / logout / access denied + business events in services

## Internationalization

Language switcher in the navbar: `?lang=en` / `?lang=uk`.

Validation messages and business exceptions are resolved from `messages_en.properties` and `messages_uk.properties`.

## Tests

- Service unit tests (Mockito)
- `@WebMvcTest` for MVC and API controllers
- `CompositeUserDetailsServiceTest` for in-memory fallback
- Cart unit tests

## Assignment checklist

| Requirement | Status |
|-------------|--------|
| Spring Data JPA + custom queries | Done |
| Spring Security (in-memory + DB, RBAC, method/URL security) | Done |
| DTOs, SQL scripts, validation, exceptions | Done |
| i18n EN + UK | Done |
| Logging (AOP + business + security events) | Done |
| H2 dev / PostgreSQL prod | Done |
| Unit tests (services + controllers) | Done |
| Order management | Done |
| Search, pagination, sorting | Done |
| JWT API | Done |
| BCrypt | Done |

## Notes

- **H2 Console (dev):** http://localhost:8080/h2-console  
  JDBC URL: `jdbc:h2:mem:appliancedb`, user: `sa`, password: *(empty)*
- Default active profile is `dev` in `application.properties`; override with `-Dspring-boot.run.profiles=prod` for production deployments.
