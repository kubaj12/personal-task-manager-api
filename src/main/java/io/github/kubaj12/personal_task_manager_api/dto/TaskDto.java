package io.github.kubaj12.personal_task_manager_api.dto;

import io.github.kubaj12.personal_task_manager_api.entity.TasksPriority;
import io.github.kubaj12.personal_task_manager_api.entity.TasksStatus;

import java.time.OffsetDateTime;

record TaskDto(Long id, String title, String description, TasksStatus status, TasksPriority priority, OffsetDateTime deadline) {
}
