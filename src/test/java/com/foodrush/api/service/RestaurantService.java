package com.foodrush.api.service;

import com.foodrush.api.dto.request.AddressRequestDTO;
import com.foodrush.api.dto.request.RestaurantRequestDTO;
import com.foodrush.api.dto.response.RestaurantResponseDTO;
import com.foodrush.api.model.Address;
import com.foodrush.api.model.Restaurant;
import com.foodrush.api.model.User;
import com.foodrush.api.repository.RestaurantRepository;
import com.foodrush.api.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GeocodingService geocodingService;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    @DisplayName("Deve cadastrar um restaurante com sucesso resolvendo endereço e coordenadas pelo CEP")
    void shouldCreateRestaurantSuccessfully() {
        // Arrange (Cenário)
        UUID ownerId = UUID.randomUUID();
        AddressRequestDTO addressDTO = new AddressRequestDTO(
                "01001-000",
                "100",
                "Loja 1"
        );
        RestaurantRequestDTO requestDTO = new RestaurantRequestDTO(
                ownerId,
                "Burger King - Centro",
                "48555407000133",
                "11999998888",
                new BigDecimal("5.00"),
                addressDTO
        );

        User ownerMock = new User();
        ownerMock.setId(ownerId);

        Address resolvedAddress = new Address();
        resolvedAddress.setId(UUID.randomUUID());
        resolvedAddress.setCep("01001000");
        resolvedAddress.setStreet("Praça da Sé");
        resolvedAddress.setNumber("100");
        resolvedAddress.setNeighborhood("Sé");
        resolvedAddress.setCity("São Paulo");
        resolvedAddress.setState("SP");
        resolvedAddress.setLatitude(new BigDecimal("-23.550520"));
        resolvedAddress.setLongitude(new BigDecimal("-46.633309"));

        Restaurant restaurantMock = new Restaurant();
        restaurantMock.setId(UUID.randomUUID());
        restaurantMock.setTradeName(requestDTO.tradeName());
        restaurantMock.setIsOpen(false);
        restaurantMock.setOwner(ownerMock);
        restaurantMock.setLatitude(resolvedAddress.getLatitude());
        restaurantMock.setLongitude(resolvedAddress.getLongitude());
        restaurantMock.setAddress(resolvedAddress);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(ownerMock));
        when(geocodingService.resolveAddress(addressDTO)).thenReturn(resolvedAddress);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurantMock);

        // Act (Ação)
        RestaurantResponseDTO response = restaurantService.create(requestDTO);

        // Assert (Validações)
        assertThat(response).isNotNull();
        assertThat(response.tradeName()).isEqualTo("Burger King - Centro");
        assertThat(response.address()).isNotNull();
        assertThat(response.address().street()).isEqualTo("Praça da Sé");
        assertThat(response.latitude()).isEqualByComparingTo("-23.550520");

        verify(userRepository, times(1)).findById(ownerId);
        verify(geocodingService, times(1)).resolveAddress(addressDTO);
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }
}