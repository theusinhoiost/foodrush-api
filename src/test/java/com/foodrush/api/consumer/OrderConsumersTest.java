package com.foodrush.api.consumer;

import com.foodrush.api.enums.OrderStatus;
import com.foodrush.api.event.OrderStatusChangedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderConsumersTest {

    private final RestaurantOrderConsumer restaurantConsumer = new RestaurantOrderConsumer();
    private final CourierDispatchConsumer courierConsumer = new CourierDispatchConsumer();
    private final CustomerTimelineConsumer customerConsumer = new CustomerTimelineConsumer();

    @Test
    @DisplayName("Deve consumir evento na fila do restaurante sem lançar exceções")
    void shouldConsumeRestaurantOrder() {
        OrderStatusChangedEvent event = new OrderStatusChangedEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                OrderStatus.CREATED,
                OrderStatus.PAID,
                OffsetDateTime.now()
        );

        assertDoesNotThrow(() -> restaurantConsumer.consumeRestaurantOrder(event));
    }

    @Test
    @DisplayName("Deve consumir evento na fila de despacho de entregadores sem lançar exceções")
    void shouldConsumeCourierDispatch() {
        OrderStatusChangedEvent event = new OrderStatusChangedEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                OrderStatus.PREPARING,
                OrderStatus.READY,
                OffsetDateTime.now()
        );

        assertDoesNotThrow(() -> courierConsumer.consumeCourierDispatch(event));
    }

    @Test
    @DisplayName("Deve consumir evento na fila de timeline do cliente sem lançar exceções")
    void shouldConsumeCustomerTimeline() {
        OrderStatusChangedEvent event = new OrderStatusChangedEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                OrderStatus.READY,
                OrderStatus.IN_ROUTE,
                OffsetDateTime.now()
        );

        assertDoesNotThrow(() -> customerConsumer.consumeCustomerTimeline(event));
    }
}

