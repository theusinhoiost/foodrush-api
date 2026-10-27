package com.foodrush.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI foodRushOpenAPI() {
    final String securitySchemeName = "bearerAuth";

    return new OpenAPI()
      .info(
        new Info()
          .title("FoodRush API — Delivery Engine")
          .description(
            "Official RESTful API documentation for the FoodRush food delivery platform. " +
            "Built with Java 25 and Spring Boot 4, featuring high-concurrency order processing, " +
            "distributed Redis caching, automated CEP geocoding, and stateless JWT authentication."
          )
          .version("v0.0.1-SNAPSHOT")
          .contact(
            new Contact()
              .name("FoodRush Engineering Team")
              .url("https://github.com/theusinhoiost/foodrush-api")
          )
          .license(
            new License().name("Apache 2.0").url("https://springdoc.org")
          )
      )
      .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
      .components(
        new Components().addSecuritySchemes(
          securitySchemeName,
          new SecurityScheme()
            .name(securitySchemeName)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .description(
              "Provide the JWT access token obtained from `/api/v1/auth/login`."
            )
        )
      );
  }
}