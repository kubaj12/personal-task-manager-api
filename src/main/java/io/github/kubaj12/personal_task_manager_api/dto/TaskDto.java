package io.github.kubaj12.personal_task_manager_api.dto;

import io.github.kubaj12.personal_task_manager_api.entity.TaskPriority;
import io.github.kubaj12.personal_task_manager_api.entity.TaskStatus;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record TaskDto(Long id, Long user_id, String title, String description, TaskStatus status, TaskPriority priority, OffsetDateTime deadline) {
}
