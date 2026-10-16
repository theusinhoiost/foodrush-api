# Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/),
e este projeto adere ao [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [Unreleased]

### Added
- Documentação inicial do projeto (`README.md`, `CHANGELOG.md` e `AGENTS.md`).

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

