package com.foodrush.api.config;

import com.foodrush.api.security.JwtAuthenticationFilter;
import com.foodrush.api.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final JwtTokenProvider tokenProvider;
  private final UserDetailsService userDetailsService;

  public SecurityConfig(
    JwtTokenProvider tokenProvider,
    UserDetailsService userDetailsService
  ) {
    this.tokenProvider = tokenProvider;
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(tokenProvider, userDetailsService);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
    AuthenticationConfiguration authConfig
  ) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .authorizeHttpRequests(auth ->
        auth
          // Rotas públicas de autenticação (com e sem prefixo /api/v1 por conta do context-path)
          .requestMatchers("/auth/**", "/api/v1/auth/**", "/error")
          .permitAll()
          // Consulta pública de restaurantes (GET)
          .requestMatchers(
            HttpMethod.GET,
            "/restaurants/**",
            "/api/v1/restaurants/**"
          )
          .permitAll()
          // Liberações do Swagger / OpenAPI
          .requestMatchers(
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api/v1/docs/**",
            "/docs/**"
          )
          .permitAll()
          // Endpoints de métricas e saúde (Actuator / Prometheus)
          .requestMatchers("/actuator/**", "/api/v1/actuator/**")
          .permitAll()
          // Qualquer outra rota exige autenticação com Bearer Token
          .anyRequest()
          .authenticated()
      )
      .addFilterBefore(
        jwtAuthenticationFilter(),
        UsernamePasswordAuthenticationFilter.class
      );

    return http.build();
  }
}
