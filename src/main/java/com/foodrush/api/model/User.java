package com.foodrush.api.model;

import com.foodrush.api.enums.RoleType;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "email", length = 100, nullable = false, unique = true)
  private String email;

  @Column(name = "password", length = 255, nullable = false)
  private String password;

  @Column(name = "phone", length = 20, nullable = false)
  private String phone;

  @Column(name = "cpf", length = 11, unique = true)
  private String cpf;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", length = 20, nullable = false)
  private RoleType role;

  @Column(name = "active", nullable = false)
  private Boolean active = true;

  @OneToOne(
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  @JoinColumn(name = "address_id")
  private Address address;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  public User() {}

  @Builder
  public User(
    UUID id,
    String name,
    String email,
    String password,
    String phone,
    String cpf,
    RoleType role,
    Boolean active,
    Address address,
    OffsetDateTime createdAt
  ) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.phone = phone;
    this.cpf = cpf;
    this.role = role;
    this.active = active != null ? active : true;
    this.address = address;
    this.createdAt = createdAt;
  }

  public static class UserBuilder {

    public UserBuilder() {}
  }
}
