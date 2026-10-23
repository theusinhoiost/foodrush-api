package com.foodrush.api.config;

import com.foodrush.api.security.JwtAuthenticationFilter;
import com.foodrush.api.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


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
      // Se a sua API for puramente stateless (JWT), geralmente o CSRF é desabilitado:
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth ->
        auth
          // Rotas públicas da sua aplicação (Auth, etc)
          .requestMatchers("/api/v1/auth/**")
          .permitAll()
          // LIBERAÇÕES DO SWAGGER / OPENAPI (Caminhos essenciais)
          .requestMatchers(
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api/v1/docs/**",
            "/docs/**"
          )
          .permitAll()
          // Qualquer outra rota exige autenticação com Bearer Token
          .anyRequest()
          .authenticated()
      );

    return http.build();
  }
}
