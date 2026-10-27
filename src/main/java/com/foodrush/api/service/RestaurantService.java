package com.foodrush.api.service;

import com.foodrush.api.dto.request.RestaurantPatchRequestDTO;
import com.foodrush.api.dto.request.RestaurantRequestDTO;
import com.foodrush.api.dto.response.RestaurantResponseDTO;
import com.foodrush.api.exception.BusinessException;
import com.foodrush.api.model.Address;
import com.foodrush.api.model.Restaurant;
import com.foodrush.api.model.User;
import com.foodrush.api.repository.RestaurantRepository;
import com.foodrush.api.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RestaurantService {

  private final RestaurantRepository restaurantRepository;
  private final UserRepository userRepository;
  private final GeocodingService geocodingService;

  public RestaurantService(
    RestaurantRepository restaurantRepository,
    UserRepository userRepository,
    GeocodingService geocodingService
  ) {
    this.restaurantRepository = restaurantRepository;
    this.userRepository = userRepository;
    this.geocodingService = geocodingService;
  }

  @CacheEvict(value = "restaurants", allEntries = true)
  @Transactional
  public RestaurantResponseDTO create(RestaurantRequestDTO dto) {
    if (restaurantRepository.existsByCnpj(dto.cnpj())) {
      throw new BusinessException(
        "Já existe um restaurante cadastrado com este CNPJ."
      );
    }
    if (restaurantRepository.existsByPhone(dto.phone())) {
      throw new BusinessException(
        "Já existe um restaurante cadastrado com este telefone."
      );
    }
    User owner = userRepository
      .findById(dto.ownerId())
      .orElseThrow(() ->
        new BusinessException(
          "Proprietário (User) não encontrado com o ID informado."
        )
      );

    Address resolvedAddress = geocodingService.resolveAddress(dto.address());

    Restaurant restaurant = Restaurant.builder()
      .owner(owner)
      .tradeName(dto.tradeName())
      .cnpj(dto.cnpj())
      .phone(dto.phone())
      .baseDeliveryFee(dto.baseDeliveryFee())
      .latitude(resolvedAddress.getLatitude())
      .longitude(resolvedAddress.getLongitude())
      .address(resolvedAddress)
      .isOpen(false)
      .build();

    Restaurant savedRestaurant = restaurantRepository.save(restaurant);
    return RestaurantResponseDTO.fromEntity(savedRestaurant);
  }

  @Cacheable(value = "restaurants")
  @Transactional(readOnly = true)
  public List<RestaurantResponseDTO> findAll() {
    return restaurantRepository
      .findAll()
      .stream()
      .map(RestaurantResponseDTO::fromEntity)
      .collect(Collectors.toList());
  }

  @Cacheable(value = "restaurants", key = "#id")
  @Transactional(readOnly = true)
  public RestaurantResponseDTO findById(UUID id) {
    Restaurant restaurant = restaurantRepository
      .findById(id)
      .orElseThrow(() -> new BusinessException("Restaurante não encontrado."));
    return RestaurantResponseDTO.fromEntity(restaurant);
  }

  @CacheEvict(value = "restaurants", allEntries = true)
  @Transactional
  public RestaurantResponseDTO toggleOpenStatus(UUID id) {
    Restaurant restaurant = restaurantRepository
      .findById(id)
      .orElseThrow(() -> new BusinessException("Restaurante não encontrado."));

    restaurant.setIsOpen(!restaurant.getIsOpen());
    Restaurant updated = restaurantRepository.save(restaurant);
    return RestaurantResponseDTO.fromEntity(updated);
  }

  @CacheEvict(value = "restaurants", allEntries = true)
  @Transactional
  public RestaurantResponseDTO update(UUID id, RestaurantRequestDTO dto) {
    Restaurant restaurant = restaurantRepository
      .findById(id)
      .orElseThrow(() -> new BusinessException("Restaurante não encontrado."));

    if (
      !restaurant.getCnpj().equals(dto.cnpj()) &&
      restaurantRepository.existsByCnpj(dto.cnpj())
    ) {
      throw new BusinessException(
        "Já existe outro restaurante cadastrado com este CNPJ."
      );
    }

    User owner = userRepository
      .findById(dto.ownerId())
      .orElseThrow(() ->
        new BusinessException(
          "Proprietário (User) não encontrado com o ID informado."
        )
      );

    Address resolvedAddress = geocodingService.resolveAddress(dto.address());

    restaurant.setOwner(owner);
    restaurant.setTradeName(dto.tradeName());
    restaurant.setCnpj(dto.cnpj());
    restaurant.setPhone(dto.phone());
    restaurant.setBaseDeliveryFee(dto.baseDeliveryFee());
    restaurant.setLatitude(resolvedAddress.getLatitude());
    restaurant.setLongitude(resolvedAddress.getLongitude());
    restaurant.setAddress(resolvedAddress);

    Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
    return RestaurantResponseDTO.fromEntity(updatedRestaurant);
  }

  @CacheEvict(value = "restaurants", allEntries = true)
  @Transactional
  public RestaurantResponseDTO patch(UUID id, RestaurantPatchRequestDTO dto) {
    Restaurant restaurant = restaurantRepository
      .findById(id)
      .orElseThrow(() -> new BusinessException("Restaurante não encontrado."));

    if (dto.tradeName() != null) {
      restaurant.setTradeName(dto.tradeName());
    }

    if (dto.cnpj() != null) {
      if (
        !restaurant.getCnpj().equals(dto.cnpj()) &&
        restaurantRepository.existsByCnpj(dto.cnpj())
      ) {
        throw new BusinessException(
          "Já existe outro restaurante cadastrado com este CNPJ."
        );
      }
      restaurant.setCnpj(dto.cnpj());
    }

    if (dto.phone() != null) {
      if (
        !restaurant.getPhone().equals(dto.phone()) &&
        restaurantRepository.existsByPhone(dto.phone())
      ) {
        throw new BusinessException(
          "Já existe outro restaurante cadastrado com este telefone."
        );
      }
      restaurant.setPhone(dto.phone());
    }

    if (dto.ownerId() != null) {
      User owner = userRepository
        .findById(dto.ownerId())
        .orElseThrow(() ->
          new BusinessException(
            "Proprietário (User) não encontrado com o ID informado."
          )
        );
      restaurant.setOwner(owner);
    }

    if (dto.baseDeliveryFee() != null) {
      restaurant.setBaseDeliveryFee(dto.baseDeliveryFee());
    }

    if (dto.address() != null) {
      Address resolvedAddress = geocodingService.resolveAddress(dto.address());
      restaurant.setLatitude(resolvedAddress.getLatitude());
      restaurant.setLongitude(resolvedAddress.getLongitude());
      restaurant.setAddress(resolvedAddress);
    }

    if (dto.isOpen() != null) {
      restaurant.setIsOpen(dto.isOpen());
    }

    Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
    return RestaurantResponseDTO.fromEntity(updatedRestaurant);
  }
}
