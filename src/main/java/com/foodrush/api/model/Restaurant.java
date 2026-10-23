package com.foodrush.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Restaurant {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "owner_id", nullable = false)
  private User owner;

  @Column(name = "trade_name", length = 120, nullable = false)
  private String tradeName;

  @Column(name = "cnpj", length = 14, nullable = false, unique = true)
  private String cnpj;

  @Column(name = "phone", length = 20, nullable = false)
  private String phone;

  @Column(
    name = "base_delivery_fee",
    precision = 10,
    scale = 2,
    nullable = false
  )
  private BigDecimal baseDeliveryFee;

  @Column(name = "latitude", precision = 9, scale = 6, nullable = false)
  private BigDecimal latitude;

  @Column(name = "longitude", precision = 9, scale = 6, nullable = false)
  private BigDecimal longitude;

  @Column(name = "is_open", nullable = false)
  private Boolean isOpen = false;

  @OneToMany(
    mappedBy = "restaurant",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<MenuItem> menuItems = new ArrayList<>();

  public Restaurant() {}

  @Builder
  public Restaurant(
    UUID id,
    User owner,
    String tradeName,
    String cnpj,
    BigDecimal baseDeliveryFee,
    BigDecimal latitude,
    BigDecimal longitude,
    Boolean isOpen,
    List<MenuItem> menuItems
  ) {
    this.id = id;
    this.owner = owner;
    this.tradeName = tradeName;
    this.cnpj = cnpj;
    this.baseDeliveryFee = baseDeliveryFee;
    this.latitude = latitude;
    this.longitude = longitude;
    this.isOpen = isOpen != null ? isOpen : false;
    this.menuItems = menuItems != null ? menuItems : new ArrayList<>();
  }

  public static class RestaurantBuilder {

    public RestaurantBuilder() {}
  }
}
