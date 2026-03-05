package io.github.kubaj12.personal_task_manager_api.dto;

import lombok.Builder;

@Builder
public record UserDto(Long id, String email) {}
