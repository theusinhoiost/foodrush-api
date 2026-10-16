# FoodRush API

API backend do **FoodRush**, construída com **Java 25** e **Spring Boot 4.1.1**.

## Stack Tecnológica

- **Linguagem:** Java 25
- **Framework:** Spring Boot 4.1.1 (`spring-boot-starter-webmvc`)
- **Persistência & Banco de Dados:**
  - Spring Data JPA (`spring-boot-starter-data-jpa`)
  - PostgreSQL (`org.postgresql:postgresql`)
  - Flyway Migrations (`spring-boot-starter-flyway`, `flyway-database-postgresql`)
- **Cache & NoSQL:** Spring Data Redis (`spring-boot-starter-data-redis`)
- **Mensageria:** Spring AMQP / RabbitMQ (`spring-boot-starter-amqp`)
- **Segurança:** Spring Security (`spring-boot-starter-security`)
- **Validação:** Jakarta Bean Validation (`spring-boot-starter-validation`)
- **Produtividade:** Lombok & Spring Boot Configuration Processor
- **Gerenciador de Build:** Apache Maven (com Maven Wrapper `./mvnw`)

## Pré-requisitos

- **JDK 25** instalado e configurado no `JAVA_HOME`
- **PostgreSQL** em execução (para ambiente local/desenvolvimento)
- **Redis** em execução
- **RabbitMQ** em execução

## Estrutura do Projeto

```text
foodrush-api/
├── src/
│   ├── main/
│   │   ├── java/com/foodrush/api/
│   │   │   └── ApiApplication.java
│   │   └── resources/
│   │       ├── db/migration/        # Scripts de migração do Flyway
│   │       └── application.properties
│   └── test/
│       └── java/com/foodrush/api/
│           └── ApiApplicationTests.java
├── pom.xml
├── mvnw
└── mvnw.cmd
```

## Configuração e Execução

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/theusinhoiost/foodrush-api.git
   cd foodrush-api
   ```

2. **Configure as variáveis de ambiente / `application.properties`:**
   Ajuste as conexões de banco de dados (PostgreSQL), Redis e RabbitMQ em `src/main/resources/application.properties` (ou via variáveis de ambiente).

3. **Compile o projeto:**
   ```bash
   ./mvnw clean compile
   ```

4. **Execute a aplicação:**
   ```bash
   ./mvnw spring-boot:run
   ```

## Executando os Testes

Para rodar a suíte de testes automatizados:

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

