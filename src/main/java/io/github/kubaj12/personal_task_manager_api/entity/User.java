package io.github.kubaj12.personal_task_manager_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "users")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String passwordHash;

  @Column(nullable = false, updatable = false)
  OffsetDateTime createdAt;

  @Column(nullable = false)
  Boolean emailVerified;

  @PrePersist
  public void prePersist() {
    this.createdAt = OffsetDateTime.now();
  }
}
