package com.foodrush.api.security;

import com.foodrush.api.model.User;
import com.foodrush.api.repository.UserRepository;
import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email)
    throws UsernameNotFoundException {
    User user = userRepository
      .findByEmail(email)
      .orElseThrow(() ->
        new UsernameNotFoundException(
          "Usuário não encontrado com o e-mail: " + email
        )
      );

    // Converte a role do usuário (ex: CUSTOMER, RESTAURANT_OWNER) para uma GrantedAuthority do Spring Security
    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
      "ROLE_" + user.getRole().name()
    );

    return org.springframework.security.core.userdetails.User.builder()
      .username(user.getEmail())
      .password(user.getPassword())
      .authorities(Collections.singletonList(authority))
      .accountExpired(false)
      .accountLocked(false)
      .credentialsExpired(false)
      .disabled(!user.getActive())
      .build();
  }
}
