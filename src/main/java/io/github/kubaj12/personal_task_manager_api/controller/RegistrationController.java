package io.github.kubaj12.personal_task_manager_api.controller;

import io.github.kubaj12.personal_task_manager_api.service.UserRegistrationService;

import io.github.kubaj12.personal_task_manager_api.dto.RegistrationResponseDto;
import io.github.kubaj12.personal_task_manager_api.dto.RegistrationRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RegistrationController {

  private final UserRegistrationService userRegistartionService;

  @PostMapping("/sign-up")
  public ResponseEntity<RegistrationResponseDto> registerUser(
      @Valid @RequestBody RegistrationRequestDto registrationRequestDto) {

    RegistrationResponseDto registrationResponseDto =
        userRegistartionService.registerUser(registrationRequestDto);
    return ResponseEntity.ok(registrationResponseDto);
  }
}
