package com.foodrush.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "menu_items")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class MenuItem {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "restaurant_id", nullable = false)
  private Restaurant restaurant;

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "base_price", precision = 10, scale = 2, nullable = false)
  private BigDecimal basePrice = BigDecimal.ZERO;

  @Column(name = "available", nullable = false)
  private Boolean available = true;

  @OneToMany(
    mappedBy = "menuItem",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<MenuItemSize> sizes = new ArrayList<>();

  @OneToMany(
    mappedBy = "menuItem",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<ComplementGroup> complementGroups = new ArrayList<>();

  public MenuItem() {}

  @Builder
  public MenuItem(
    UUID id,
    Restaurant restaurant,
    String name,
    String description,
    BigDecimal basePrice,
    Boolean available,
    List<MenuItemSize> sizes,
    List<ComplementGroup> complementGroups
  ) {
    this.id = id;
    this.restaurant = restaurant;
    this.name = name;
    this.description = description;
    this.basePrice = basePrice != null ? basePrice : BigDecimal.ZERO;
    this.available = available != null ? available : true;
    this.sizes = sizes != null ? sizes : new ArrayList<>();
    this.complementGroups =
      complementGroups != null ? complementGroups : new ArrayList<>();
  }

  public static class MenuItemBuilder {

    public MenuItemBuilder() {}
  }
}
