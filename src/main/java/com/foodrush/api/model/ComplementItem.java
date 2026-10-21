package com.foodrush.api.model;




import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "complement_items")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ComplementItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private ComplementGroup group;

    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "available", nullable = false)
    private Boolean available = true;

    public ComplementItem() {
    }

    @Builder
    public ComplementItem(UUID id, ComplementGroup group, String name, BigDecimal price, Boolean available) {
        this.id = id;
        this.group = group;
        this.name = name;
        this.price = price != null ? price : BigDecimal.ZERO;
        this.available = available != null ? available : true;
    }

    public static class ComplementItemBuilder {
        public ComplementItemBuilder() {
        }
    }
}