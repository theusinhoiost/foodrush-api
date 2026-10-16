package com.foodrush.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "menu_item_sizes")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class MenuItemSize {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "menu_item_id", nullable = false)
  private MenuItem menuItem;

  @Column(name = "name", length = 50, nullable = false)
  private String name;

  @Column(name = "price", precision = 10, scale = 2, nullable = false)
  private BigDecimal price;

  @Column(name = "stock_quantity", nullable = false)
  private Integer stockQuantity = 0;

  @Version
  @Column(name = "version", nullable = false)
  private Long version;

  @Column(name = "available", nullable = false)
  private Boolean available = true;

  public MenuItemSize() {}

  @Builder
  public MenuItemSize(
    UUID id,
    MenuItem menuItem,
    String name,
    BigDecimal price,
    Integer stockQuantity,
    Long version,
    Boolean available
  ) {
    this.id = id;
    this.menuItem = menuItem;
    this.name = name;
    this.price = price;
    this.stockQuantity = stockQuantity != null ? stockQuantity : 0;
    this.version = version;
    this.available = available != null ? available : true;
  }

  public static class MenuItemSizeBuilder {

    public MenuItemSizeBuilder() {}
  }
}
