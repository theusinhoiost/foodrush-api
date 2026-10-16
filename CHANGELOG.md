# Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/),
e este projeto adere ao [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [Unreleased]

### Added
- Documentação inicial do projeto (`README.md`, `CHANGELOG.md` e `AGENTS.md`).
- Arquivo `docker-compose.yml` para provisionamento local de PostgreSQL (`foodrush_db`), Redis e RabbitMQ.
- Migração Flyway `V1__init_schema.sql` com criação das tabelas (`users`, `restaurants`, `menu_items`, `menu_item_sizes`, `complement_groups`, `complement_items`, `orders`, `order_items`, `order_item_complements`) e índices de performance.
- Enums de domínio (`OrderStatus`, `RoleType`), entidades JPA (`model/`) e repositórios Spring Data JPA (`repository/`).

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

