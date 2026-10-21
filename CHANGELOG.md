# Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/),
e este projeto adere ao [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [Unreleased]

### Added
- **Autenticação e Segurança JWT (`config/`, `security/`, `controller/`):**
  - Adicionadas dependências JJWT `0.12.6` (`jjwt-api`, `jjwt-impl`, `jjwt-jackson`) no `pom.xml`.
  - Implementado `JwtTokenProvider` para geração e validação de tokens JWT (`accessToken` e `refreshToken`) com algoritmo `HS512`.
  - Implementado filtro `JwtAuthenticationFilter` e `UserDetailsServiceImpl` para autenticação stateless baseada no banco de dados.
  - Criado `SecurityConfig` com política de sessão `STATELESS`, codificação de senhas com `BCryptPasswordEncoder` e liberação de rotas públicas de autenticação e documentação.
  - Criado `AuthController` com endpoints de registro (`POST /register`), login (`POST /login`) e renovação de token (`POST /refresh`).
- **DTOs de Requisição e Resposta (`dto/`):**
  - Criados records imutáveis com validações Jakarta Bean Validation: `UserRequestDTO`, `LoginRequestDTO`, `RefreshTokenRequestDTO`, `AuthResponseDTO` e `UserResponseDTO`.
- **Tratamento Centralizado de Exceções (`exception/`):**
  - Criada `BusinessException` para violações de regras de negócio.
  - Implementado `GlobalExceptionHandler` (`@RestControllerAdvice`) retornando respostas padronizadas no formato **RFC 7807 (`ProblemDetail`)** para erros de negócio (`422`), erros de validação de campos (`400`) e erros internos (`500`).
- **Domínio, Persistência e Migrações (`enums/`, `model/`, `repository/`, `db/migration/`):**
  - Criada migração Flyway `V1__init_schema.sql` contendo as tabelas `users`, `restaurants`, `menu_items`, `menu_item_sizes`, `complement_groups`, `complement_items`, `orders`, `order_items` e `order_item_complements`, além de índices de performance.
  - Adicionados enums `RoleType` (`CUSTOMER`, `RESTAURANT_OWNER`, `DELIVERY_DRIVER`, `ADMIN`) e `OrderStatus` (`CREATED`, `CONFIRMED`, `IN_PREPARATION`, `READY_FOR_PICKUP`, `OUT_FOR_DELIVERY`, `DELIVERED`, `CANCELLED`).
  - Implementadas entidades JPA: `User`, `Restaurant`, `MenuItem`, `MenuItemSize` (com `@Version` para lock otimista), `ComplementGroup`, `ComplementItem`, `Order` (com `idempotencyKey`), `OrderItem` e `OrderItemComplement`.
  - Implementados repositórios Spring Data JPA: `UserRepository` (incluindo `existsByPhone` e `existsByCpf`), `RestaurantRepository`, `MenuItemRepository` (com `JOIN FETCH` de detalhes), `MenuItemSizeRepository` (com lock pessimista `PESSIMISTIC_WRITE`), `ComplementGroupRepository`, `ComplementItemRepository` e `OrderRepository`.
- **Infraestrutura e Documentação:**
  - Adicionado `docker-compose.yml` com containers e healthchecks para PostgreSQL 17 (`foodrush_db`), Redis 7 e RabbitMQ 4 Management.
  - Adicionados `README.md`, `CHANGELOG.md` e `AGENTS.md`.

### Fixed
- Corrigido `java.lang.VerifyError: Constructor must call super() or this() before return` na geração de construtores do Lombok com ECJ/Java 25 nas entidades JPA, além de substituir `@Data` por `@Getter` e `@Setter`.
- Configurado `maven.compiler.source` e `maven.compiler.target` no `pom.xml` para evitar falha `release version 25 not supported` em ambientes sem `ct.sym`.
- Ajustado `application.yml` com variáveis de ambiente, `spring.jpa.open-in-view: false` e remoção da propriedade depreciada `hibernate.dialect`.

## [0.0.1-SNAPSHOT] - 2026-10-16

### Added
- Inicialização da estrutura do projeto com Apache Maven e Maven Wrapper (`mvnw` / `mvnw.cmd`).
- Configuração base do **Spring Boot 4.1.1** com **Java 25** (`com.foodrush:api`).
- Dependências principais configuradas no `pom.xml`:
  - Spring Web MVC (`spring-boot-starter-webmvc`)
  - Spring Data JPA (`spring-boot-starter-data-jpa`) e driver PostgreSQL (`postgresql`)
  - Flyway para migrações de banco de dados (`spring-boot-starter-flyway`, `flyway-database-postgresql`)
  - Spring Data Redis (`spring-boot-starter-data-redis`)
  - Spring AMQP para mensageria com RabbitMQ (`spring-boot-starter-amqp`)
  - Spring Security (`spring-boot-starter-security`)
  - Bean Validation (`spring-boot-starter-validation`)
  - Lombok e Spring Boot Configuration Processor
- Classe principal `ApiApplication` e estrutura inicial de testes com `ApiApplicationTests`.
