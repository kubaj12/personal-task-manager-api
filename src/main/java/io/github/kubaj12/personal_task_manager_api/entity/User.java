package io.github.kubaj12.personal_task_manager_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

import java.time.OffsetDateTime;

@Entity
@Table(name="users")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    OffsetDateTime created_at;
}
