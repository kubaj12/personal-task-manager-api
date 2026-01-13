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
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TasksStatus status;

    @Enumerated(EnumType.STRING)
    private TasksPriority priority;

    private OffsetDateTime deadline;
}

//public record Tasks(@Id Long id, Long user_id, String title, String description, TasksPriority priority, TasksStatus status, OffsetDateTime deadline) {
//    public Tasks {
//        java.util.Objects.requireNonNull(priority, "Status cannot be null");
//        java.util.Objects.requireNonNull(status, "Status cannot be null");
//    }
//}
