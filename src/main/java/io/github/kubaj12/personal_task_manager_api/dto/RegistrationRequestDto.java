package io.github.kubaj12.personal_task_manager_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationRequestDto(
    @NotBlank(message = "E-mail cannot be empty.") String email,
    @NotBlank(message = "Password cannot be empty") @Size(min = 12, max = 128) String rawPassword) {}
