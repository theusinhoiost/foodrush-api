package com.foodrush.api.controller;

import com.foodrush.api.dto.request.RestaurantRequestDTO;
import com.foodrush.api.dto.response.RestaurantResponseDTO;
import com.foodrush.api.service.RestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/restaurants")
@Tag(name = "Restaurantes", description = "Gerenciamento de perfil, geolocalização e status operacional dos restaurantes")
public class RestaurantController {

  private final RestaurantService restaurantService;

  public RestaurantController(RestaurantService restaurantService) {
    this.restaurantService = restaurantService;
  }

@PostMapping
    @Operation(
        summary = "Cadastrar um novo restaurante",
        description = "Cria um novo estabelecimento comercial informando dados cadastrais, CNPJ e coordenadas geográficas (latitude/longitude) para cálculo de frete.",
        security = @SecurityRequirement(name = "bearerAuth") // Se o endpoint exigir JWT
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso",
            content = @Content(schema = @Schema(implementation = RestaurantResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos enviados no payload"),
        @ApiResponse(responseCode = "409", description = "CNPJ já cadastrado no sistema")
    })
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
