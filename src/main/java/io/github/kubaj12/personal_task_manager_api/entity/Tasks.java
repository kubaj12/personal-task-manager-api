package io.github.kubaj12.personal_task_manager_api.entity;

import org.springframework.data.annotation.Id;
import java.time.OffsetDateTime;

public record Tasks(@Id Long id, Long user_id, String title, String description, TasksPriority priority, TasksStatus status, OffsetDateTime deadline) {
    public Tasks {
        java.util.Objects.requireNonNull(priority, "Status cannot be null");
        java.util.Objects.requireNonNull(status, "Status cannot be null");
    }
}
