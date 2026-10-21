package com.foodrush.api.dto.response;

import com.foodrush.api.model.Restaurant;

import java.math.BigDecimal;
import java.util.UUID;

public record RestaurantResponseDTO(
        UUID id,
        UUID ownerId,
        String tradeName,
        String cnpj,
        BigDecimal baseDeliveryFee,
        BigDecimal latitude,
        BigDecimal longitude,
        Boolean isOpen
) {
    public static RestaurantResponseDTO fromEntity(Restaurant restaurant) {
        return new RestaurantResponseDTO(
                restaurant.getId(),
                restaurant.getOwnerId(),
                restaurant.getTradeName(),
                restaurant.getCnpj(),
                restaurant.getBaseDeliveryFee(),
                restaurant.getLatitude(),
                restaurant.getLongitude(),
                restaurant.getIsOpen()
        );
    }
}