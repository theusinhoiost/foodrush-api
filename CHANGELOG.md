# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- **Docker Environment Configuration (`.env.example`, `.env`, `docker-compose.yml`, `.gitignore`):**
  - Added `.env.example` and local `.env` files with configurable database, Redis, RabbitMQ, Prometheus, and Grafana ports and credentials.
  - Parameterized `docker-compose.yml` with environment variables and sensible fallback defaults.
  - Added `.env` to `.gitignore` while tracking `.env.example`.
- **Event-Driven Messaging with RabbitMQ (`config/RabbitMQConfig`, `event/`, `producer/`, `consumer/`):**
  - Implemented Topic Exchange `foodrush.orders.exchange` for dynamic order lifecycle routing.
  - Created 3 dedicated queues:
    - `foodrush.restaurant.orders.queue`: consumes `order.paid` and `order.canceled` to alert the kitchen.
    - `foodrush.courier.dispatch.queue`: consumes `order.ready` to dispatch nearby couriers.
    - `foodrush.customer.timeline.queue`: consumes `order.*` to broadcast real-time status updates to customers.
  - Implemented Dead Letter Exchange (DLX) `foodrush.orders.dlx` and DLQ `foodrush.orders.dlq` for unprocessable messages.
  - Created immutable event record `OrderStatusChangedEvent` and `OrderEventProducer`.
  - Created consumers: `RestaurantOrderConsumer`, `CourierDispatchConsumer`, and `CustomerTimelineConsumer`.
  - Added `READY` status to `OrderStatus` enum.
  - Added unit test suites for producer and consumers (`OrderEventProducerTest`, `OrderConsumersTest`).
- **Observability and Metrics with Prometheus & Grafana (`docker/`, `pom.xml`, `application.yml`, `config/SecurityConfig`):**
  - Added `spring-boot-starter-actuator` and `micrometer-registry-prometheus` dependencies.
  - Configured Actuator endpoints (`health`, `info`, `metrics`, `prometheus`) and HTTP request latency percentiles histogram (`percentiles-histogram`).
  - Permitted Actuator endpoints (`/actuator/**`, `/api/v1/actuator/**`) in `SecurityConfig`.
  - Created Prometheus scraping configuration in `docker/prometheus/prometheus.yml`.
  - Added automated Prometheus datasource provisioning for Grafana in `docker/grafana/provisioning/datasources/prometheus.yml`.
  - Added `prometheus` (port `9090`) and `grafana` (port `3000`) containers with persistent volumes to `docker-compose.yml`.
- **Address Management, Automated Geocoding, and DB Seed (`model/Address`, `service/GeocodingService`, `db/migration/`):**
  - Unified `addresses` table and foreign keys `address_id` (`users` and `restaurants`) into `V1__init_schema.sql`.
  - Created Flyway migration `V2__seed_mock_data.sql` with initial seed data (addresses, users across all roles with password `123456`, restaurants, menu items, sizes, complements, and a sample order).
  - Implemented `Address` entity, `AddressRepository`, `AddressRequestDTO` (accepting `cep`, `number`, and optional `complement`), and `AddressResponseDTO`.
  - Added custom Bean Validation constraint `@ValidCEP` (`CepValidatorConstraint`).
  - Created `GeocodingService` integrating via `RestClient` with **BrasilAPI CEP v2** (with fallback to **OpenStreetMap Nominatim**) for automated address resolution and latitude/longitude coordinates on the back-end.
  - Updated `RestaurantService`, `UserService`, and `AuthController` to resolve addresses and geolocation coordinates.
- **User Management (`service/UserService`, `dto/request/UserPatchRequestDTO`):**
  - Implemented `UserService` with constructor injection, Spring Cache integration (`@Cacheable` and `@CacheEvict` on `users`), and methods `create`, `findAll`, `findById`, `update`, `patch`, and `toggleActiveStatus` with uniqueness validation for email, phone, and CPF.
- **OpenAPI / Swagger Documentation (`config/OpenApiConfig`, `pom.xml`, `application.yml`):**
  - Added `springdoc-openapi-starter-webmvc-ui` (`2.8.5`) dependency.
  - Created `OpenApiConfig` defining API metadata (`FoodRush API (Delivery Engine)`) and HTTP Bearer JWT security scheme (`bearerAuth`).
  - Documented endpoints with `@Tag`, `@Operation`, and `@ApiResponses` (accessible at `/api/v1/swagger-ui/index.html` and `/api/v1/docs`).
  - Permitted Swagger/OpenAPI routes in `SecurityConfig`.
- **Restaurant Management & Caching (`controller/`, `service/`, `dto/`):**
  - Created `RestaurantController` with endpoints for creation (`POST`), listing (`GET`), retrieval by ID (`GET /{id}`), updating (`PUT /{id}`), and operational toggle (`PATCH /{id}/toggle-open`).
  - Created `RestaurantService` with business rules, CNPJ/phone uniqueness validation, owner verification, and cache management (`@Cacheable` and `@CacheEvict` on `restaurants`).
  - Enabled caching with `@EnableCaching` on `ApiApplication` and added `spring-boot-starter-cache`.
  - Added DTOs: `RestaurantRequestDTO`, `RestaurantPatchRequestDTO`, and `RestaurantResponseDTO`.
  - Added `phone` column to `Restaurant` entity and database schema, and `existsByPhone` to `RestaurantRepository`.
- **Custom Bean Validation Constraints (`validation/`):**
  - Implemented custom constraint validators for checksum digits and format validation: `@ValidCPF` (`CpfValidatorConstraint`), `@ValidCNPJ` (`CnpjValidatorConstraint`), `@ValidPhone` (`PhoneValidatorConstraint`), and `@ValidCEP` (`CepValidatorConstraint`).
- **JWT Authentication & Security (`config/`, `security/`, `controller/`):**
  - Added JJWT `0.12.6` dependencies (`jjwt-api`, `jjwt-impl`, `jjwt-jackson`).
  - Implemented `JwtTokenProvider` for generating and verifying `HS512` signed tokens (`accessToken` and `refreshToken`).
  - Implemented `JwtAuthenticationFilter` and `UserDetailsServiceImpl` for stateless database-backed authentication.
  - Created `SecurityConfig` with `BCryptPasswordEncoder` and endpoint authorization rules.
  - Created `AuthController` with endpoints for registration (`POST /register`), authentication (`POST /login`), and token refresh (`POST /refresh`).
- **Authentication & User DTOs (`dto/`):**
  - Created immutable records with Jakarta Validation: `UserRequestDTO`, `LoginRequestDTO`, `RefreshTokenRequestDTO`, `AuthResponseDTO`, and `UserResponseDTO`.
- **Centralized Exception Handling (`exception/`):**
  - Created `BusinessException` for business rule violations.
  - Implemented `GlobalExceptionHandler` (`@RestControllerAdvice`) returning **RFC 7807 (`ProblemDetail`)** for business errors (`422`), field validation errors (`400`), and unexpected internal errors (`500`).
- **Domain, Persistence, and Migrations (`enums/`, `model/`, `repository/`, `db/migration/`):**
  - Created Flyway migration `V1__init_schema.sql` with tables: `addresses`, `users`, `restaurants`, `menu_items`, `menu_item_sizes`, `complement_groups`, `complement_items`, `orders`, `order_items`, and `order_item_complements`, along with performance indexes.
  - Added enums: `RoleType` (`CUSTOMER`, `RESTAURANT_OWNER`, `DELIVERY_DRIVER`, `ADMIN`) and `OrderStatus` (`CREATED`, `PAID`, `PREPARING`, `READY`, `IN_ROUTE`, `DELIVERED`, `CANCELED`).
  - Implemented JPA entities: `Address`, `User`, `Restaurant`, `MenuItem`, `MenuItemSize` (with `@Version` for optimistic concurrency), `ComplementGroup`, `ComplementItem`, `Order` (with `idempotencyKey`), `OrderItem`, and `OrderItemComplement`.
  - Implemented Spring Data JPA repositories: `AddressRepository`, `UserRepository`, `RestaurantRepository`, `MenuItemRepository`, `MenuItemSizeRepository` (with `PESSIMISTIC_WRITE` locking), `ComplementGroupRepository`, `ComplementItemRepository`, and `OrderRepository`.
- **Infrastructure & Project Documentation:**
  - Added `docker-compose.yml` with healthchecks for PostgreSQL 17 (`foodrush_db`), Redis 7, RabbitMQ 4 Management, Prometheus, and Grafana.
  - Added English documentation in `README.md`, `CHANGELOG.md`, and `AGENTS.md`.

### Fixed
- Fixed and updated `AuthControllerTest` to cover registration with address/CEP geocoding, business exceptions, credentials authentication, and token renewal.
- Fixed `@ManyToOne` relationship mapping for restaurant owner (`User owner`), removing duplicate column mapping on `owner_id`.
- Resolved `java.lang.VerifyError: Constructor must call super() or this() before return` with Lombok and ECJ/Java 25 by replacing `@Data` with `@Getter` and `@Setter`.
- Configured `maven.compiler.source` and `maven.compiler.target` to avoid compilation failures on systems without `ct.sym`.
- Optimized `application.yml` with environment variable interpolation, `spring.jpa.open-in-view: false`, and removed deprecated `hibernate.dialect`.

## [0.0.1-SNAPSHOT] - 2026-10-16
- Initial release.
