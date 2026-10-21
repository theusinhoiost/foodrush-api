package com.foodrush.api.service;

import com.foodrush.api.dto.request.RestaurantPatchRequestDTO;
import com.foodrush.api.dto.request.RestaurantRequestDTO;
import com.foodrush.api.dto.response.RestaurantResponseDTO;
import com.foodrush.api.exception.BusinessException;
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

  public RestaurantService(
    RestaurantRepository restaurantRepository,
    UserRepository userRepository
  ) {
    this.restaurantRepository = restaurantRepository;
    this.userRepository = userRepository;
  }
@CacheEvict (value = "restaurants", key = "#id")
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

    Restaurant restaurant = Restaurant.builder()
      .ownerId(owner.getId())
      .tradeName(dto.tradeName())
      .cnpj(dto.cnpj())
      .phone(dto.phone())
      .baseDeliveryFee(dto.baseDeliveryFee())
      .latitude(dto.latitude())
      .longitude(dto.longitude())
      .isOpen(false)
      .build();

    Restaurant savedRestaurant = restaurantRepository.save(restaurant);
    return RestaurantResponseDTO.fromEntity(savedRestaurant);
  }

@Cacheable(value = "restaurants", key = "#id")
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

  @CacheEvict(value = "restaurants", key = "#id")
  @Transactional
  public RestaurantResponseDTO toggleOpenStatus(UUID id) {
    Restaurant restaurant = restaurantRepository
      .findById(id)
      .orElseThrow(() -> new BusinessException("Restaurante não encontrado."));

    restaurant.setIsOpen(!restaurant.getIsOpen());
    Restaurant updated = restaurantRepository.save(restaurant);
    return RestaurantResponseDTO.fromEntity(updated);
  }

  @CacheEvict(value = "restaurants", key = "#id")
  @Transactional
  public RestaurantResponseDTO update(UUID id, RestaurantRequestDTO dto) {
    Restaurant restaurant = restaurantRepository
      .findById(id)
      .orElseThrow(() -> new BusinessException("Restaurante não encontrado."));

    // Se o CNPJ foi alterado, valida se já não pertence a outro restaurante
    if (
      !restaurant.getCnpj().equals(dto.cnpj()) &&
      restaurantRepository.existsByCnpj(dto.cnpj())
    ) {
      throw new BusinessException(
        "Já existe outro restaurante cadastrado com este CNPJ."
      );
    }

    // Valida se o novo proprietário existe
    User owner = userRepository
      .findById(dto.ownerId())
      .orElseThrow(() ->
        new BusinessException(
          "Proprietário (User) não encontrado com o ID informado."
        )
      );

    restaurant.setOwnerId(owner.getId());
    restaurant.setTradeName(dto.tradeName());
    restaurant.setCnpj(dto.cnpj());
    restaurant.setBaseDeliveryFee(dto.baseDeliveryFee());
    restaurant.setLatitude(dto.latitude());
    restaurant.setLongitude(dto.longitude());

    Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
    return RestaurantResponseDTO.fromEntity(updatedRestaurant);
  }
  @CacheEvict(value = "restaurants", key = "#id")
    @Transactional
    public RestaurantResponseDTO patch(UUID id, RestaurantPatchRequestDTO dto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Restaurante não encontrado."));


        if (dto.tradeName() != null) {
            restaurant.setTradeName(dto.tradeName());
        }

        if (dto.cnpj() != null) {
            if (!restaurant.getCnpj().equals(dto.cnpj()) && restaurantRepository.existsByCnpj(dto.cnpj())) {
                throw new BusinessException("Já existe outro restaurante cadastrado com este CNPJ.");
            }
            restaurant.setCnpj(dto.cnpj());
        }

        if (dto.ownerId() != null) {
            User owner = userRepository.findById(dto.ownerId())
                    .orElseThrow(() -> new BusinessException("Proprietário (User) não encontrado com o ID informado."));
            restaurant.setOwnerId(owner.getId());
        }

        if (dto.baseDeliveryFee() != null) {
            restaurant.setBaseDeliveryFee(dto.baseDeliveryFee());
        }

        if (dto.latitude() != null) {
            restaurant.setLatitude(dto.latitude());
        }

        if (dto.longitude() != null) {
            restaurant.setLongitude(dto.longitude());
        }

        if (dto.isOpen() != null) {
            restaurant.setIsOpen(dto.isOpen());
        }

        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
        return RestaurantResponseDTO.fromEntity(updatedRestaurant);
    }
}
