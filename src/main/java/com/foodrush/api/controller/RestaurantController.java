package com.foodrush.api.controller;

import com.foodrush.api.dto.request.RestaurantRequestDTO;
import com.foodrush.api.dto.response.RestaurantResponseDTO;
import com.foodrush.api.service.RestaurantService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

  private final RestaurantService restaurantService;

  public RestaurantController(RestaurantService restaurantService) {
    this.restaurantService = restaurantService;
  }

  @PostMapping
  public ResponseEntity<RestaurantResponseDTO> create(
    @RequestBody @Valid RestaurantRequestDTO dto
  ) {
    RestaurantResponseDTO createdRestaurant = restaurantService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdRestaurant);
  }

  @GetMapping
  public ResponseEntity<List<RestaurantResponseDTO>> findAll() {
    List<RestaurantResponseDTO> restaurants = restaurantService.findAll();
    return ResponseEntity.ok(restaurants);
  }

  @GetMapping("/{id}")
  public ResponseEntity<RestaurantResponseDTO> findById(@PathVariable UUID id) {
    RestaurantResponseDTO restaurant = restaurantService.findById(id);
    return ResponseEntity.ok(restaurant);
  }

  @PatchMapping("/{id}/toggle-open")
  public ResponseEntity<RestaurantResponseDTO> toggleOpenStatus(
    @PathVariable UUID id
  ) {
    RestaurantResponseDTO updated = restaurantService.toggleOpenStatus(id);
    return ResponseEntity.ok(updated);
  }

  @PutMapping("/{id}")
  public ResponseEntity<RestaurantResponseDTO> update(
    @PathVariable UUID id,
    @RequestBody @Valid RestaurantRequestDTO dto
  ) {
    RestaurantResponseDTO updatedRestaurant = restaurantService.update(id, dto);
    return ResponseEntity.ok(updatedRestaurant);
  }
}
