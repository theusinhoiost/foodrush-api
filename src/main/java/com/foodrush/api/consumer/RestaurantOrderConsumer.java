package com.foodrush.api.consumer;

import com.foodrush.api.config.RabbitMQConfig;
import com.foodrush.api.event.OrderStatusChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RestaurantOrderConsumer {

    /**
     * Consome mensagens da fila do restaurante (ex: order.paid, order.canceled).
     * O restaurante recebe este alerta para iniciar o preparo ou cancelar na cozinha.
     */
    @RabbitListener(queues = RabbitMQConfig.RESTAURANT_ORDERS_QUEUE)
    public void consumeRestaurantOrder(OrderStatusChangedEvent event) {
        log.info("[RESTAURANTE] Pedido {} recebido pelo Restaurante {}. Status atual: {}",
                event.orderId(), event.restaurantId(), event.newStatus());

        // Aqui, em uma aplicação completa, emitiria um WebSocket (STOMP) para o painel da cozinha (KDS)
    }
}

