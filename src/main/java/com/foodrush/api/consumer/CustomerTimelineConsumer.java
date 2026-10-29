package com.foodrush.api.consumer;

import com.foodrush.api.config.RabbitMQConfig;
import com.foodrush.api.event.OrderStatusChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomerTimelineConsumer {

    /**
     * Consome mensagens da fila do cliente. Como esta fila possui o binding "order.*",
     * o cliente recebe notificações para todas as mudanças de status (pago, preparando, pronto, a caminho, entregue).
     */
    @RabbitListener(queues = RabbitMQConfig.CUSTOMER_TIMELINE_QUEUE)
    public void consumeCustomerTimeline(OrderStatusChangedEvent event) {
        log.info("[CLIENTE] Notificação enviada para o Cliente {} sobre o Pedido {}: Status mudou de {} para {}",
                event.customerId(), event.orderId(), event.previousStatus(), event.newStatus());

        // Aqui, enviaria Push Notification (Firebase / APNS) ou WebSocket para o aplicativo móvel do cliente
    }
}

