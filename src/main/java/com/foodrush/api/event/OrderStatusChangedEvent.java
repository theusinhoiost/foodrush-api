package com.foodrush.api.event;

import com.foodrush.api.enums.OrderStatus;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Evento imutável disparado sempre que o status de um pedido é atualizado.
 */
public record OrderStatusChangedEvent(
    UUID orderId,
    UUID customerId,
    UUID restaurantId,
    OrderStatus previousStatus,
    OrderStatus newStatus,
    OffsetDateTime occurredAt
) {}

