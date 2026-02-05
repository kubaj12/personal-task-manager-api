package io.github.kubaj12.personal_task_manager_api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name="tasks")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //only Hibernate can use this class
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private Long user_id;

    @Column(nullable = false)
    private String title;
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    private OffsetDateTime deadline;
}