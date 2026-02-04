package io.github.kubaj12.personal_task_manager_api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.kubaj12.personal_task_manager_api.entity.TaskPriority;
import io.github.kubaj12.personal_task_manager_api.entity.TaskStatus;
import lombok.Builder;
//import lombok.extern.jackson.Jacksonized;

import java.time.OffsetDateTime;

//@Jacksonized
@Builder
public record TaskDto(
    Long id,
    Long user_id,
    String title,
    String description,
    TaskStatus status,
    TaskPriority priority,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")//2026-01-10T12:00:00+02:00
    OffsetDateTime deadline) {}
