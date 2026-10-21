package com.foodrush.api.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "complement_groups")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ComplementGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @Column(name = "required", nullable = false)
    private Boolean required = false;

    @Column(name = "min_quantity", nullable = false)
    private Integer minQuantity = 0;

    @Column(name = "max_quantity", nullable = false)
    private Integer maxQuantity = 1;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComplementItem> items = new ArrayList<>();

    public ComplementGroup() {
    }

    @Builder
    public ComplementGroup(UUID id, MenuItem menuItem, String name, Boolean required, Integer minQuantity, Integer maxQuantity, List<ComplementItem> items) {
        this.id = id;
        this.menuItem = menuItem;
        this.name = name;
        this.required = required != null ? required : false;
        this.minQuantity = minQuantity != null ? minQuantity : 0;
        this.maxQuantity = maxQuantity != null ? maxQuantity : 1;
        this.items = items != null ? items : new ArrayList<>();
    }

    public static class ComplementGroupBuilder {
        public ComplementGroupBuilder() {
        }
    }
}