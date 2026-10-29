# Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/),
e este projeto adere ao [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [Unreleased]

### Added
- **Mensageria com RabbitMQ para Restaurante, Entregador e Cliente (`config/RabbitMQConfig`, `event/`, `producer/`, `consumer/`):**
  - Implementada Topic Exchange `foodrush.orders.exchange` para roteamento dinâmico de eventos do ciclo de vida dos pedidos.
  - Criadas 3 filas dedicadas:
    - `foodrush.restaurant.orders.queue`: consome `order.paid` e `order.canceled` para alertar a cozinha do restaurante.
    - `foodrush.courier.dispatch.queue`: consome `order.ready` para despachar entregadores geograficamente próximos.
    - `foodrush.customer.timeline.queue`: consome `order.*` para notificar o cliente sobre cada transição de status em tempo real.
  - Implementada Dead Letter Exchange (DLX) `foodrush.orders.dlx` e DLQ `foodrush.orders.dlq` para mensagens com falha persistente.
  - Criado o evento imutável `OrderStatusChangedEvent` e o produtor `OrderEventProducer`.
  - Criados os consumidores `RestaurantOrderConsumer`, `CourierDispatchConsumer` e `CustomerTimelineConsumer`.
  - Adicionado status `READY` ao enum `OrderStatus`.
  - Adicionados testes unitários para o produtor e consumidores.
- **Observabilidade e Métricas com Prometheus e Grafana (`docker/`, `pom.xml`, `application.yml`, `config/SecurityConfig`):**
  - Adicionadas dependências `spring-boot-starter-actuator` e `micrometer-registry-prometheus` no `pom.xml`.
  - Configurados endpoints do Actuator (`health`, `info`, `metrics`, `prometheus`) e histograma de percentis de requisições HTTP (`percentiles-histogram`) no `application.yml`.
  - Liberados os endpoints do Actuator (`/actuator/**`, `/api/v1/actuator/**`) no `SecurityConfig`.
  - Criado arquivo de configuração `docker/prometheus/prometheus.yml` configurado para coletar métricas da aplicação.
  - Criado provisionamento automático do datasource Prometheus para o Grafana em `docker/grafana/provisioning/datasources/prometheus.yml`.
  - Adicionados os serviços do `prometheus` (porta `9090`) e `grafana` (porta `3000`) com volumes persistentes no `docker-compose.yml`.
- **Endereços, Geolocalização Automática e Seed do Banco (`model/Address`, `service/GeocodingService`, `db/migration/`):**
  - Unificada a tabela `addresses` e chaves estrangeiras `address_id` (`users` e `restaurants`) diretamente em `V1__init_schema.sql`.
  - Criada migração `V2__seed_mock_data.sql` com massa de dados inicial (endereços, usuários de todos os perfis com senha `123456`, restaurantes, itens de cardápio, tamanhos, complementos e pedido de exemplo).
  - Implementada entidade `Address`, `AddressRepository`, DTOs `AddressRequestDTO` (recebendo apenas `cep`, `number` e `complement` opcional) e `AddressResponseDTO`.
  - Adicionado validador customizado `@ValidCEP` (`CepValidatorConstraint`).
  - Criado `GeocodingService` integrando via `RestClient` com a **BrasilAPI CEP v2** (e fallback para **OpenStreetMap Nominatim**) para preencher automaticamente rua, bairro, cidade, UF, `latitude` e `longitude` no back-end.
  - Atualizados `RestaurantService`, `UserService` e `AuthController` para resolverem endereços e coordenadas automaticamente.
- **Gerenciamento de Usuários (`service/UserService`, `dto/request/UserPatchRequestDTO`):**
  - Implementado `UserService` com injeção via construtor (`UserRepository` e `PasswordEncoder`), suporte a cache (`@Cacheable` e `@CacheEvict` em `users`) e métodos `create`, `findAll`, `findById`, `update`, `patch` e `toggleActiveStatus` com validações de unicidade de e-mail, telefone e CPF.
- **Documentação OpenAPI / Swagger (`config/OpenApiConfig`, `pom.xml`, `application.yml`):**
  - Adicionada dependência `springdoc-openapi-starter-webmvc-ui` (`2.8.5`).
  - Criado `OpenApiConfig` definindo metadados da API (`FoodRush API (Delivery Engine)`) e esquema de segurança HTTP Bearer JWT (`bearerAuth`).
  - Documentados os endpoints de `RestaurantController` com `@Tag`, `@Operation` e `@ApiResponses` (acessível em `http://localhost:8080/api/v1/swagger-ui/index.html#/restaurant-controller/create` e `/api/v1/docs`).
  - Liberadas as rotas do Swagger/OpenAPI (`/v3/api-docs/**`, `/swagger-ui/**`, `/swagger-ui.html`, `/api/v1/docs/**`, `/docs/**`) no `SecurityConfig`.
- **Gerenciamento de Restaurantes e Cache (`controller/`, `service/`, `dto/`):**
  - Criado `RestaurantController` com endpoints para criação (`POST`), listagem (`GET`), busca por ID (`GET /{id}`), atualização (`PUT /{id}`) e alternância de funcionamento (`PATCH /{id}/toggle-open`).
  - Criado `RestaurantService` com regras de negócio, validação de unicidade de CNPJ e telefone, verificação de proprietário (`User`) e integração de cache (`@Cacheable` e `@CacheEvict` no cache `restaurants`).
  - Habilitado cache na aplicação com `@EnableCaching` em `ApiApplication` e dependência `spring-boot-starter-cache`.
  - Adicionados DTOs `RestaurantRequestDTO`, `RestaurantPatchRequestDTO` e `RestaurantResponseDTO`.
  - Adicionada coluna `phone` na entidade `Restaurant` e na tabela `restaurants` (`V1__init_schema.sql`), além de método `existsByPhone` em `RestaurantRepository`.
- **Validações Customizadas (`validation/`):**
  - Implementadas anotações e constraints customizadas do Bean Validation para validação algorítmica de dígitos verificadores e formato: `@ValidCPF` (`CpfValidatorConstraint`), `@ValidCNPJ` (`CnpjValidatorConstraint`) e `@ValidPhone` (`PhoneValidatorConstraint`).
- **Autenticação e Segurança JWT (`config/`, `security/`, `controller/`):**
  - Adicionadas dependências JJWT `0.12.6` (`jjwt-api`, `jjwt-impl`, `jjwt-jackson`) no `pom.xml`.
  - Implementado `JwtTokenProvider` para geração e validação de tokens JWT (`accessToken` e `refreshToken`) com algoritmo `HS512`.
  - Implementado filtro `JwtAuthenticationFilter` e `UserDetailsServiceImpl` para autenticação stateless baseada no banco de dados.
  - Criado `SecurityConfig` com codificação de senhas via `BCryptPasswordEncoder` e controle de rotas públicas e autenticadas.
  - Criado `AuthController` com endpoints de registro (`POST /register`), login (`POST /login`) e renovação de token (`POST /refresh`).
- **DTOs de Autenticação e Usuário (`dto/`):**
  - Criados records imutáveis com validações Jakarta Bean Validation: `UserRequestDTO`, `LoginRequestDTO`, `RefreshTokenRequestDTO`, `AuthResponseDTO` e `UserResponseDTO`.
- **Tratamento Centralizado de Exceções (`exception/`):**
  - Criada `BusinessException` para violações de regras de negócio.
  - Implementado `GlobalExceptionHandler` (`@RestControllerAdvice`) retornando respostas padronizadas no formato **RFC 7807 (`ProblemDetail`)** para erros de negócio (`422`), erros de validação de campos (`400`) e erros internos (`500`).
- **Domínio, Persistência e Migrações (`enums/`, `model/`, `repository/`, `db/migration/`):**
  - Criada migração Flyway `V1__init_schema.sql` contendo as tabelas `users`, `restaurants`, `menu_items`, `menu_item_sizes`, `complement_groups`, `complement_items`, `orders`, `order_items` e `order_item_complements`, além de índices de performance.
  - Adicionados enums `RoleType` (`CUSTOMER`, `RESTAURANT_OWNER`, `DELIVERY_DRIVER`, `ADMIN`) e `OrderStatus` (`CREATED`, `PAID`, `PREPARING`, `READY`, `IN_ROUTE`, `DELIVERED`, `CANCELED`).
  - Implementadas entidades JPA: `User`, `Restaurant`, `MenuItem`, `MenuItemSize` (com `@Version` para lock otimista), `ComplementGroup`, `ComplementItem`, `Order` (com `idempotencyKey`), `OrderItem` e `OrderItemComplement`.
  - Implementados repositórios Spring Data JPA: `UserRepository`, `RestaurantRepository`, `MenuItemRepository`, `MenuItemSizeRepository` (com lock pessimista `PESSIMISTIC_WRITE`), `ComplementGroupRepository`, `ComplementItemRepository` e `OrderRepository`.
- **Infraestrutura e Documentação:**
  - Adicionado `docker-compose.yml` com containers e healthchecks para PostgreSQL 17 (`foodrush_db`), Redis 7, RabbitMQ 4 Management, Prometheus e Grafana.
  - Adicionados `README.md`, `CHANGELOG.md` e `AGENTS.md`.

### Fixed
- Corrigido `AuthControllerTest` com testes unitários para registro, duplicidades de dados, login e renovação de token.
- Ajustado mapeamento do relacionamento `@ManyToOne` de proprietário (`User owner`) em `Restaurant`, `RestaurantService` e `RestaurantResponseDTO`, removendo mapeamento duplicado da coluna `owner_id`.
- Corrigido `java.lang.VerifyError: Constructor must call super() or this() before return` na geração de construtores do Lombok com ECJ/Java 25 nas entidades JPA, além de substituir `@Data` por `@Getter` e `@Setter`.
- Configurado `maven.compiler.source` e `maven.compiler.target` no `pom.xml` para evitar falha `release version 25 not supported` em ambientes sem `ct.sym`.
- Ajustado `application.yml` com variáveis de ambiente, `spring.jpa.open-in-view: false` e remoção da propriedade depreciada `hibernate.dialect`.

## [0.0.1-SNAPSHOT] - 2026-10-16
- Initial release.
