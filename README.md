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
- **Mensageria & Filas:** Spring AMQP / RabbitMQ (`spring-boot-starter-amqp`) com Topic Exchange, Dead Letter Exchange (DLX/DLQ) e mensagens em JSON
- **Observabilidade & Métricas:** Spring Boot Actuator (`spring-boot-starter-actuator`), Micrometer Prometheus (`micrometer-registry-prometheus`), Prometheus e Grafana
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
├── docker/
│   ├── grafana/
│   │   └── provisioning/
│   │       └── datasources/
│   │           └── prometheus.yml
│   └── prometheus/
│       └── prometheus.yml
├── src/
│   ├── main/
│   │   ├── java/com/foodrush/api/
│   │   │   ├── config/
│   │   │   │   ├── OpenApiConfig.java
│   │   │   │   ├── RabbitMQConfig.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── consumer/
│   │   │   │   ├── CourierDispatchConsumer.java
│   │   │   │   ├── CustomerTimelineConsumer.java
│   │   │   │   └── RestaurantOrderConsumer.java
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
│   │   │   ├── event/
│   │   │   │   └── OrderStatusChangedEvent.java
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
│   │   │   ├── producer/
│   │   │   │   └── OrderEventProducer.java
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
│           ├── consumer/
│           │   └── OrderConsumersTest.java
│           ├── controller/
│           │   └── AuthControllerTest.java
│           ├── producer/
│           │   └── OrderEventProducerTest.java
│           ├── service/
│           │   └── RestaurantService.java
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

2. **Suba os serviços de infraestrutura (PostgreSQL, Redis, RabbitMQ, Prometheus e Grafana):**
   ```bash
   docker compose up -d
   ```
   Isso iniciará:
   - **PostgreSQL 17:** `localhost:5432`
   - **Redis 7:** `localhost:6379`
   - **RabbitMQ 4 Management:** `localhost:5672` (Painel Web: `http://localhost:15672` — `guest`/`guest`)
   - **Prometheus:** `http://localhost:9090`
   - **Grafana:** `http://localhost:3000` (Login: `admin` / `admin`)

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

## Mensageria Orientada a Eventos (RabbitMQ)

A aplicação utiliza uma **Topic Exchange** (`foodrush.orders.exchange`) para notificar os diferentes atores da plataforma de forma assíncrona e desacoplada conforme o ciclo de vida do pedido:

- **Exchange:** `foodrush.orders.exchange` (Topic Exchange)
- **Dead Letter Exchange (DLX):** `foodrush.orders.dlx` / **DLQ:** `foodrush.orders.dlq`
- **Filas e Atores:**
  - **Restaurante (`foodrush.restaurant.orders.queue`):**
    - Bindings: `order.paid`, `order.canceled`
    - Notifica a cozinha/KDS sobre novos pedidos para preparo ou cancelamentos.
  - **Entregador / Despacho (`foodrush.courier.dispatch.queue`):**
    - Binding: `order.ready`
    - Aciona o motor de despacho e busca por entregadores geograficamente próximos assim que o prato está pronto.
  - **Cliente (`foodrush.customer.timeline.queue`):**
    - Binding: `order.*`
    - Recebe todas as transições de status do pedido para envio de notificações Push/SMS e atualização do app em tempo real.

## Observabilidade e Monitoramento (Prometheus & Grafana)

A API integra métricas completas via **Spring Boot Actuator** e exportador **Micrometer Prometheus**:

- **Endpoints do Actuator:**
  - Healthcheck: [http://localhost:8080/api/v1/actuator/health](http://localhost:8080/api/v1/actuator/health) (exibe saúde de DB, Redis, RabbitMQ e disco)
  - Métricas Prometheus: [http://localhost:8080/api/v1/actuator/prometheus](http://localhost:8080/api/v1/actuator/prometheus)
  - Métricas gerais: [http://localhost:8080/api/v1/actuator/metrics](http://localhost:8080/api/v1/actuator/metrics)
- **Prometheus:** [http://localhost:9090](http://localhost:9090)
  - Coleta automática a cada 5s configurada em `docker/prometheus/prometheus.yml`.
- **Grafana:** [http://localhost:3000](http://localhost:3000) (Usuário: `admin` / Senha: `admin`)
  - Datasource do Prometheus já provisionado automaticamente em `docker/grafana/provisioning/datasources/prometheus.yml`.
  - Recomendado importar o dashboard oficial da comunidade **JVM (Micrometer)** através do ID **`4701`** para visualização gráfica de uso de CPU, memória heap, latência HTTP (p95, p99), pools de conexão HikariCP e filas.

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
