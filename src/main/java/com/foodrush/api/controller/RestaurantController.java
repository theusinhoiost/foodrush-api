package com.foodrush.api.controller;

import com.foodrush.api.dto.request.RestaurantPatchRequestDTO;
import com.foodrush.api.dto.request.RestaurantRequestDTO;
import com.foodrush.api.dto.response.RestaurantResponseDTO;
import com.foodrush.api.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
@Tag(
  name = "Restaurants",
  description = "Endpoints for managing restaurant profiles, automated postal code (CEP) geolocation, and operational status"
)
public class RestaurantController {

  private final RestaurantService restaurantService;

  public RestaurantController(RestaurantService restaurantService) {
    this.restaurantService = restaurantService;
  }

  @PostMapping
  @Operation(
    summary = "Register a new restaurant",
    description = "Creates a new restaurant profile, validates CNPJ and phone uniqueness, and automatically resolves full street address and geographic coordinates (latitude/longitude) from the provided CEP.",
    security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "201",
        description = "Restaurant successfully created",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = RestaurantResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Invalid request payload or validation failure",
        content = @Content(
          mediaType = "application/problem+json",
          schema = @Schema(implementation = ProblemDetail.class)
        )
      ),
      @ApiResponse(
        responseCode = "401",
        description = "Missing or invalid JWT Bearer token",
        content = @Content
      ),
      @ApiResponse(
        responseCode = "422",
        description = "Business rule violation (e.g., duplicate CNPJ/phone, owner not found, or invalid CEP)",
        content = @Content(
          mediaType = "application/problem+json",
          schema = @Schema(implementation = ProblemDetail.class)
        )
      ),
    }
  )
  public ResponseEntity<RestaurantResponseDTO> create(
    @RequestBody @Valid RestaurantRequestDTO dto
  ) {
    RestaurantResponseDTO createdRestaurant = restaurantService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdRestaurant);
  }

  @GetMapping
  @Operation(
    summary = "List all restaurants",
    description = "Retrieves a cached list of all registered restaurants including their resolved addresses and operational status."
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "List of restaurants retrieved successfully",
        content = @Content(
          mediaType = "application/json",
          array = @ArraySchema(
            schema = @Schema(implementation = RestaurantResponseDTO.class)
          )
        )
      ),
    }
  )
  public ResponseEntity<List<RestaurantResponseDTO>> findAll() {
    List<RestaurantResponseDTO> restaurants = restaurantService.findAll();
    return ResponseEntity.ok(restaurants);
  }

  @GetMapping("/{id}")
  @Operation(
    summary = "Get restaurant by ID",
    description = "Retrieves detailed information about a specific restaurant by its unique UUID."
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "Restaurant found and returned successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = RestaurantResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "422",
        description = "Restaurant not found with the specified ID",
        content = @Content(
          mediaType = "application/problem+json",
          schema = @Schema(implementation = ProblemDetail.class)
        )
      ),
    }
  )
  public ResponseEntity<RestaurantResponseDTO> findById(
    @Parameter(
      description = "Unique UUID of the restaurant",
      example = "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb1",
      required = true
    ) @PathVariable UUID id
  ) {
    RestaurantResponseDTO restaurant = restaurantService.findById(id);
    return ResponseEntity.ok(restaurant);
  }

  @PutMapping("/{id}")
  @Operation(
    summary = "Update a restaurant (full replacement)",
    description = "Updates all registration details, owner, delivery fee, and address/geolocation for an existing restaurant.",
    security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "Restaurant updated successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = RestaurantResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Invalid request payload or validation failure",
        content = @Content(
          mediaType = "application/problem+json",
          schema = @Schema(implementation = ProblemDetail.class)
        )
      ),
      @ApiResponse(
        responseCode = "401",
        description = "Missing or invalid JWT Bearer token",
        content = @Content
      ),
      @ApiResponse(
        responseCode = "422",
        description = "Restaurant not found or CNPJ already registered to another restaurant",
        content = @Content(
          mediaType = "application/problem+json",
          schema = @Schema(implementation = ProblemDetail.class)
        )
      ),
    }
  )
  public ResponseEntity<RestaurantResponseDTO> update(
    @Parameter(
      description = "Unique UUID of the restaurant to update",
      example = "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb1",
      required = true
    ) @PathVariable UUID id,
    @RequestBody @Valid RestaurantRequestDTO dto
  ) {
    RestaurantResponseDTO updatedRestaurant = restaurantService.update(id, dto);
    return ResponseEntity.ok(updatedRestaurant);
  }

  @PatchMapping("/{id}")
  @Operation(
    summary = "Partially update a restaurant",
    description = "Partially updates only the non-null fields provided in the request payload for an existing restaurant.",
    security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "Restaurant partially updated successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = RestaurantResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Validation error on provided fields",
        content = @Content(
          mediaType = "application/problem+json",
          schema = @Schema(implementation = ProblemDetail.class)
        )
      ),
      @ApiResponse(
        responseCode = "401",
        description = "Missing or invalid JWT Bearer token",
        content = @Content
      ),
      @ApiResponse(
        responseCode = "422",
        description = "Restaurant not found or business rule conflict",
        content = @Content(
          mediaType = "application/problem+json",
          schema = @Schema(implementation = ProblemDetail.class)
        )
      ),
    }
  )
  public ResponseEntity<RestaurantResponseDTO> patch(
    @Parameter(
      description = "Unique UUID of the restaurant to partially update",
      example = "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb1",
      required = true
    ) @PathVariable UUID id,
    @RequestBody @Valid RestaurantPatchRequestDTO dto
  ) {
    RestaurantResponseDTO updatedRestaurant = restaurantService.patch(id, dto);
    return ResponseEntity.ok(updatedRestaurant);
  }

  @PatchMapping("/{id}/toggle-open")
  @Operation(
    summary = "Toggle restaurant open/closed status",
    description = "Flips the operational status (`isOpen`) of the specified restaurant between open (`true`) and closed (`false`).",
    security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "Restaurant open status toggled successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = RestaurantResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "401",
        description = "Missing or invalid JWT Bearer token",
        content = @Content
      ),
      @ApiResponse(
        responseCode = "422",
        description = "Restaurant not found with the specified ID",
        content = @Content(
          mediaType = "application/problem+json",
          schema = @Schema(implementation = ProblemDetail.class)
        )
      ),
    }
  )
  public ResponseEntity<RestaurantResponseDTO> toggleOpenStatus(
    @Parameter(
      description = "Unique UUID of the restaurant",
      example = "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb1",
      required = true
    ) @PathVariable UUID id
  ) {
    RestaurantResponseDTO updated = restaurantService.toggleOpenStatus(id);
    return ResponseEntity.ok(updated);
  }
}
