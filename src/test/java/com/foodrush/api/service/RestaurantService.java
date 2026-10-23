package com.foodrush.api.service;

import com.foodrush.api.dto.request.RestaurantRequestDTO;
import com.foodrush.api.dto.response.RestaurantResponseDTO;
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
    private UserRepository userRepository; // <-- Adicionado o mock do repositório de usuários

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    @DisplayName("Deve cadastrar um restaurante com sucesso quando os dados forem válidos")
    void shouldCreateRestaurantSuccessfully() {
        // Arrange (Cenário)
        UUID ownerId = UUID.randomUUID();
        RestaurantRequestDTO requestDTO = new RestaurantRequestDTO(
                ownerId,
                "Burger King - Centro",
                "48555407000133",
                "11999998888",
                new BigDecimal("5.00"),
                new BigDecimal("-22.400000"),
                new BigDecimal("-47.550000")
        );

        User ownerMock = new User();
        ownerMock.setId(ownerId);

        Restaurant restaurantMock = new Restaurant();
        restaurantMock.setId(UUID.randomUUID());
        restaurantMock.setTradeName(requestDTO.tradeName());
        restaurantMock.setIsOpen(false);
        restaurantMock.setOwner(ownerMock);

        // Simulando que o usuário proprietário existe no banco
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(ownerMock));

        // Simulando o comportamento do salvamento do restaurante
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurantMock);

        // Act (Ação)
        RestaurantResponseDTO response = restaurantService.create(requestDTO);

        // Assert (Validações)
        assertThat(response).isNotNull();
        assertThat(response.tradeName()).isEqualTo("Burger King - Centro");
        
        // Garante que ambos os repositórios foram chamados corretamente
        verify(userRepository, times(1)).findById(ownerId);
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }
}