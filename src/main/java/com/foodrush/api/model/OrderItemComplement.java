package com.foodrush.api.model;




import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_item_complements")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class OrderItemComplement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "complement_item_id", nullable = false)
    private ComplementItem complementItem;

    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    public OrderItemComplement() {
    }

    @Builder
    public OrderItemComplement(UUID id, OrderItem orderItem, ComplementItem complementItem, String name, BigDecimal price, Integer quantity) {
        this.id = id;
        this.orderItem = orderItem;
        this.complementItem = complementItem;
        this.name = name;
        this.price = price;
        this.quantity = quantity != null ? quantity : 1;
    }

    public static class OrderItemComplementBuilder {
        public OrderItemComplementBuilder() {
        }
    }
}