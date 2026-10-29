package com.foodrush.api.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange Principal
    public static final String ORDERS_EXCHANGE = "foodrush.orders.exchange";

    // Dead Letter Exchange e Fila para mensagens com falha persistente
    public static final String ORDERS_DLX = "foodrush.orders.dlx";
    public static final String ORDERS_DLQ = "foodrush.orders.dlq";

    // Filas dos Três Atores
    public static final String RESTAURANT_ORDERS_QUEUE = "foodrush.restaurant.orders.queue";
    public static final String COURIER_DISPATCH_QUEUE = "foodrush.courier.dispatch.queue";
    public static final String CUSTOMER_TIMELINE_QUEUE = "foodrush.customer.timeline.queue";

    // 1. Topic Exchange Principal
    @Bean
    public TopicExchange ordersExchange() {
        return new TopicExchange(ORDERS_EXCHANGE);
    }

    // 2. Dead Letter Exchange (DLX) do tipo Fanout
    @Bean
    public FanoutExchange ordersDlx() {
        return new FanoutExchange(ORDERS_DLX);
    }

    @Bean
    public Queue ordersDlq() {
        return QueueBuilder.durable(ORDERS_DLQ).build();
    }

    @Bean
    public Binding bindingDlq(Queue ordersDlq, FanoutExchange ordersDlx) {
        return BindingBuilder.bind(ordersDlq).to(ordersDlx);
    }

    // 3. Fila do Restaurante (Recebe pedidos pagos ou cancelados)
    @Bean
    public Queue restaurantOrdersQueue() {
        return QueueBuilder.durable(RESTAURANT_ORDERS_QUEUE)
                .deadLetterExchange(ORDERS_DLX)
                .build();
    }

    @Bean
    public Binding bindingRestaurantPaid(Queue restaurantOrdersQueue, TopicExchange ordersExchange) {
        return BindingBuilder.bind(restaurantOrdersQueue)
                .to(ordersExchange)
                .with("order.paid");
    }

    @Bean
    public Binding bindingRestaurantCanceled(Queue restaurantOrdersQueue, TopicExchange ordersExchange) {
        return BindingBuilder.bind(restaurantOrdersQueue)
                .to(ordersExchange)
                .with("order.canceled");
    }

    // 4. Fila de Despacho de Entregadores (Recebe pedido quando o restaurante finaliza o preparo)
    @Bean
    public Queue courierDispatchQueue() {
        return QueueBuilder.durable(COURIER_DISPATCH_QUEUE)
                .deadLetterExchange(ORDERS_DLX)
                .build();
    }

    @Bean
    public Binding bindingCourierReady(Queue courierDispatchQueue, TopicExchange ordersExchange) {
        return BindingBuilder.bind(courierDispatchQueue)
                .to(ordersExchange)
                .with("order.ready");
    }

    // 5. Fila do Cliente (Recebe qualquer atualização de status do pedido: order.*)
    @Bean
    public Queue customerTimelineQueue() {
        return QueueBuilder.durable(CUSTOMER_TIMELINE_QUEUE)
                .deadLetterExchange(ORDERS_DLX)
                .build();
    }

    @Bean
    public Binding bindingCustomerAllUpdates(Queue customerTimelineQueue, TopicExchange ordersExchange) {
        // "order.*" captura order.created, order.paid, order.preparing, order.ready, order.in_route, etc.
        return BindingBuilder.bind(customerTimelineQueue)
                .to(ordersExchange)
                .with("order.*");
    }

    // 6. Conversor para trafegar mensagens automaticamente em formato JSON
    @Bean
    public MessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jacksonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jacksonMessageConverter);
        return template;
    }
}

