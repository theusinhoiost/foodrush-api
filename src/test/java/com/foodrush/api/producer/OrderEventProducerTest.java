package com.foodrush.api.producer;

import com.foodrush.api.config.RabbitMQConfig;
import com.foodrush.api.enums.OrderStatus;
import com.foodrush.api.event.OrderStatusChangedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderEventProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrderEventProducer orderEventProducer;

    @Test
    @DisplayName("Deve publicar evento de pedido com a routing key correta no Topic Exchange")
    void shouldPublishOrderStatusChangedEvent() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        OrderStatusChangedEvent event = new OrderStatusChangedEvent(
                orderId,
                customerId,
                restaurantId,
                OrderStatus.CREATED,
                OrderStatus.PAID,
                OffsetDateTime.now()
        );

        orderEventProducer.publishStatusChanged(event);

        verify(rabbitTemplate, times(1)).convertAndSend(
                RabbitMQConfig.ORDERS_EXCHANGE,
                "order.paid",
                event
        );
    }
}

