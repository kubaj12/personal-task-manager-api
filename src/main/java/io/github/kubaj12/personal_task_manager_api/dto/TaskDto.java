package io.github.kubaj12.personal_task_manager_api.dto;

import io.github.kubaj12.personal_task_manager_api.entity.TaskPriority;
import io.github.kubaj12.personal_task_manager_api.entity.TaskStatus;

import java.time.OffsetDateTime;

record TaskDto(Long id, String title, String description, TaskStatus status, TaskPriority priority, OffsetDateTime deadline) {
}
