package com.foodrush.api.consumer;

import com.foodrush.api.config.RabbitMQConfig;
import com.foodrush.api.event.OrderStatusChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CourierDispatchConsumer {

    /**
     * Consome mensagens da fila de despacho dos entregadores (ex: order.ready).
     * O sistema busca entregadores disponíveis próximos ao restaurante para ofertar a corrida.
     */
    @RabbitListener(queues = RabbitMQConfig.COURIER_DISPATCH_QUEUE)
    public void consumeCourierDispatch(OrderStatusChangedEvent event) {
        log.info("[ENTREGADOR / DESPACHO] Pedido {} está pronto no Restaurante {}. Buscando entregadores mais próximos...",
                event.orderId(), event.restaurantId());

        // Aqui, executaria o algoritmo de proximidade geográfica (ex: Redis GEO) para notificar entregadores
    }
}

