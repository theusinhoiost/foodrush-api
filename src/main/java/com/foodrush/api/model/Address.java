package com.foodrush.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "cep", length = 8, nullable = false)
  private String cep;

  @Column(name = "street", length = 150, nullable = false)
  private String street;

  @Column(name = "number", length = 20, nullable = false)
  private String number;

  @Column(name = "complement", length = 100)
  private String complement;

  @Column(name = "neighborhood", length = 100, nullable = false)
  private String neighborhood;

  @Column(name = "city", length = 100, nullable = false)
  private String city;

  @Column(name = "state", length = 2, nullable = false)
  private String state;

  @Column(name = "latitude", precision = 9, scale = 6)
  private BigDecimal latitude;

  @Column(name = "longitude", precision = 9, scale = 6)
  private BigDecimal longitude;

  public Address() {}

  @Builder
  public Address(
    UUID id,
    String cep,
    String street,
    String number,
    String complement,
    String neighborhood,
    String city,
    String state,
    BigDecimal latitude,
    BigDecimal longitude
  ) {
    this.id = id;
    this.cep = cep;
    this.street = street;
    this.number = number;
    this.complement = complement;
    this.neighborhood = neighborhood;
    this.city = city;
    this.state = state;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public static class AddressBuilder {

    public AddressBuilder() {}
  }
}

