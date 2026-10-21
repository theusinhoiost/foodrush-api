package com.foodrush.api.repository;

import com.foodrush.api.model.Restaurant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
  Optional<Restaurant> findByCnpj(String cnpj);

  boolean existsByCnpj(String cnpj);

  boolean existsByPhone(String phone);

  List<Restaurant> findAllByIsOpenTrue();

  List<Restaurant> findAllByOwnerId(UUID ownerId);
}
