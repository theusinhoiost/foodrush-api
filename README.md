# FoodRush API

API RESTful de delivery de comida (**FoodRush**), construída com **Java 25** e **Spring Boot 4.1.1**, focada em alta concorrência, consistência transacional, cache distribuído com Redis, segurança stateless via JWT e arquitetura orientada a eventos.

## Stack Tecnológica

- **Linguagem:** Java 25
- **Framework:** Spring Boot 4.1.1 (`spring-boot-starter-webmvc`)
- **Persistência & Banco de Dados:**
  - Spring Data JPA / Hibernate (`spring-boot-starter-data-jpa`)
  - PostgreSQL 17 (`org.postgresql:postgresql`)
  - Flyway Migrations (`spring-boot-starter-flyway`, `flyway-database-postgresql`)
- **Cache & NoSQL:** Spring Cache (`spring-boot-starter-cache`) & Spring Data Redis (`spring-boot-starter-data-redis`)
- **Mensageria:** Spring AMQP / RabbitMQ (`spring-boot-starter-amqp`)
- **Segurança & Autenticação:**
  - Spring Security (`spring-boot-starter-security`)
  - JSON Web Token — JJWT `0.12.6` (`jjwt-api`, `jjwt-impl`, `jjwt-jackson`) com assinatura `HS512` e senhas criptografadas via `BCrypt`
- **Documentação da API (OpenAPI / Swagger):**
  - SpringDoc OpenAPI `2.8.5` (`springdoc-openapi-starter-webmvc-ui`) com suporte a autenticação Bearer JWT
- **Validação & Tratamento de Erros:**
  - Jakarta Bean Validation (`spring-boot-starter-validation`) + validadores customizados (`@ValidCPF`, `@ValidCNPJ`, `@ValidPhone`)
  - Respostas padronizadas com **RFC 7807 (`ProblemDetail`)** via `GlobalExceptionHandler`
- **Produtividade & Build:** Lombok, Spring Boot Configuration Processor e Apache Maven (`./mvnw`)

## Pré-requisitos

- **JDK 25** instalado
- **Docker** e **Docker Compose** (para subir PostgreSQL, Redis e RabbitMQ localmente)

## Estrutura do Projeto

```text
foodrush-api/
├── src/
│   ├── main/
│   │   ├── java/com/foodrush/api/
│   │   │   ├── config/
│   │   │   │   ├── OpenApiConfig.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   └── RestaurantController.java
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   │   ├── LoginRequestDTO.java
│   │   │   │   │   ├── RefreshTokenRequestDTO.java
│   │   │   │   │   ├── RestaurantPatchRequestDTO.java
│   │   │   │   │   ├── RestaurantRequestDTO.java
│   │   │   │   │   └── UserRequestDTO.java
│   │   │   │   └── response/
│   │   │   │       ├── AuthResponseDTO.java
│   │   │   │       ├── RestaurantResponseDTO.java
│   │   │   │       └── UserResponseDTO.java
│   │   │   ├── enums/
│   │   │   │   ├── OrderStatus.java
│   │   │   │   └── RoleType.java
│   │   │   ├── exception/
│   │   │   │   ├── BusinessException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── model/
│   │   │   │   ├── ComplementGroup.java
│   │   │   │   ├── ComplementItem.java
│   │   │   │   ├── MenuItem.java
│   │   │   │   ├── MenuItemSize.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── OrderItem.java
│   │   │   │   ├── OrderItemComplement.java
│   │   │   │   ├── Restaurant.java
│   │   │   │   └── User.java
│   │   │   ├── repository/
│   │   │   │   ├── ComplementGroupRepository.java
│   │   │   │   ├── ComplementItemRepository.java
│   │   │   │   ├── MenuItemRepository.java
│   │   │   │   ├── MenuItemSizeRepository.java
│   │   │   │   ├── OrderRepository.java
│   │   │   │   ├── RestaurantRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── security/
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   └── UserDetailsServiceImpl.java
│   │   │   ├── service/
│   │   │   │   └── RestaurantService.java
│   │   │   ├── validation/
│   │   │   │   ├── CnpjValidatorConstraint.java
│   │   │   │   ├── CpfValidatorConstraint.java
│   │   │   │   ├── PhoneValidatorConstraint.java
│   │   │   │   ├── ValidCNPJ.java
│   │   │   │   ├── ValidCPF.java
│   │   │   │   └── ValidPhone.java
│   │   │   └── ApiApplication.java
│   │   └── resources/
│   │       ├── db/migration/
│   │       │   └── V1__init_schema.sql
│   │       └── application.yml
│   └── test/
│       └── java/com/foodrush/api/
│           └── ApiApplicationTests.java
├── docker-compose.yml
├── pom.xml
├── mvnw
└── mvnw.cmd
```

## Modelo de Dados (Flyway `V1__init_schema.sql`)

- **`users`**: Credenciais, identificadores únicos (`email`, `phone`, `cpf`) e perfis de acesso (`CUSTOMER`, `RESTAURANT_OWNER`, `DELIVERY_DRIVER`, `ADMIN`).
- **`restaurants`**: Dados do restaurante (`trade_name`, `cnpj`, `phone`), vínculo com o proprietário (`owner_id`), geolocalização (`latitude`, `longitude`), taxa base de entrega (`base_delivery_fee`) e status de funcionamento (`is_open`).
- **`menu_items` & `menu_item_sizes`**: Itens do cardápio e variações de tamanho/porção com controle de estoque e concorrência (`version` para lock otimista e query com `PESSIMISTIC_WRITE` para evitar *overselling*).
- **`complement_groups` & `complement_items`**: Grupos de complementos/adicionais com regras de quantidade mínima/máxima e obrigatoriedade.
- **`orders`, `order_items` & `order_item_complements`**: Pedidos com chave de idempotência (`idempotency_key`), valores consolidados (`subtotal`, `delivery_fee`, `total`), itens e snapshot histórico de complementos.

## Configuração e Execução

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/theusinhoiost/foodrush-api.git
   cd foodrush-api
   ```

2. **Suba os serviços de infraestrutura (PostgreSQL, Redis e RabbitMQ):**
   ```bash
   docker compose up -d
   ```

3. **Variáveis de ambiente (opcional):**
   O arquivo `src/main/resources/application.yml` já possui valores padrão para desenvolvimento local, podendo ser sobrescritos via variáveis de ambiente:
   | Variável | Padrão Local | Descrição |
   | :--- | :--- | :--- |
   | `DB_URL` | `jdbc:postgresql://localhost:5432/foodrush_db` | URL JDBC do PostgreSQL |
   | `DB_USERNAME` | `postgres` | Usuário do banco de dados |
   | `DB_PASSWORD` | `postgres` | Senha do banco de dados |
   | `REDIS_HOST` | `localhost` | Host do Redis |
   | `REDIS_PORT` | `6379` | Porta do Redis |
   | `RABBITMQ_HOST` | `localhost` | Host do RabbitMQ |
   | `RABBITMQ_PORT` | `5672` | Porta AMQP do RabbitMQ |
   | `RABBITMQ_USERNAME` | `guest` | Usuário do RabbitMQ |
   | `RABBITMQ_PASSWORD` | `guest` | Senha do RabbitMQ |

4. **Compile e execute a aplicação:**
   ```bash
   ./mvnw clean compile
   ./mvnw spring-boot:run
   ```
   Ou execute diretamente a classe `com.foodrush.api.ApiApplication` pela IDE.

## Documentação Interativa (Swagger / OpenAPI)

Com a aplicação em execução, acesse a interface do **Swagger UI** em:

- **Swagger UI (Restaurantes / Create):** [http://localhost:8080/api/v1/swagger-ui/index.html#/restaurant-controller/create](http://localhost:8080/api/v1/swagger-ui/index.html#/restaurant-controller/create)
- **Atalho Swagger UI:** `http://localhost:8080/api/v1/docs`
- **OpenAPI JSON Spec:** `http://localhost:8080/api/v1/docs/json`

## Endpoints da API

### Autenticação (`/api/v1/auth`)

| Método | Rota | Descrição | Autenticação |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/register` | Cadastro de novo usuário (`UserRequestDTO` com validação `@ValidCPF` e `@ValidPhone`) | Pública |
| `POST` | `/api/v1/auth/login` | Autenticação e emissão de `accessToken` + `refreshToken` (`LoginRequestDTO`) | Pública |
| `POST` | `/api/v1/auth/refresh` | Renovação do `accessToken` via `refreshToken` (`RefreshTokenRequestDTO`) | Pública |

### Restaurantes (`/api/v1/restaurants`)

| Método | Rota | Descrição | Autenticação |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/restaurants` | Cadastra um novo restaurante (`RestaurantRequestDTO` com `@ValidCNPJ` e `@ValidPhone`) | Bearer JWT |
| `GET` | `/api/v1/restaurants` | Lista todos os restaurantes cadastrados (com suporte a cache `restaurants`) | Bearer JWT |
| `GET` | `/api/v1/restaurants/{id}` | Busca os detalhes de um restaurante por ID (com suporte a cache `restaurants`) | Bearer JWT |
| `PUT` | `/api/v1/restaurants/{id}` | Atualiza os dados cadastrais e geolocalização de um restaurante | Bearer JWT |
| `PATCH` | `/api/v1/restaurants/{id}/toggle-open` | Alterna o status operacional (`isOpen`) do restaurante (aberto/fechado) | Bearer JWT |

## Executando os Testes

```bash
./mvnw test
```

## Empacotamento / Build

Para gerar o artefato `.jar` de produção:

```bash
./mvnw clean package
```

Para construir uma imagem OCI/Docker utilizando o plugin do Spring Boot:

```bash
./mvnw spring-boot:build-image
```
