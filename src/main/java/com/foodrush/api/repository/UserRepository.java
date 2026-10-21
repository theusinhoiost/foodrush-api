package com.foodrush.api.repository;

import com.foodrush.api.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  Optional<User> findByPhone(String phone);

  Optional<User> findByCpf(String cpf);

  boolean existsByCpf(String cpf);

  boolean existsByPhone(String phone);
}
