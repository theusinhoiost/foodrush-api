package com.foodrush.api.producer;

import com.foodrush.api.config.RabbitMQConfig;
import com.foodrush.api.event.OrderStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Publica o evento no RabbitMQ utilizando a Topic Exchange.
     * A routing key é gerada dinamicamente com base no novo status: "order.<status_em_minusculo>"
     * Exemplo: order.paid, order.ready, order.in_route
     */
    public void publishStatusChanged(OrderStatusChangedEvent event) {
        String routingKey = "order." + event.newStatus().name().toLowerCase();
        log.info("Publicando evento no RabbitMQ [Exchange: {}, RoutingKey: {}]: {}",
                RabbitMQConfig.ORDERS_EXCHANGE, routingKey, event);

        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDERS_EXCHANGE, routingKey, event);
    }
}

