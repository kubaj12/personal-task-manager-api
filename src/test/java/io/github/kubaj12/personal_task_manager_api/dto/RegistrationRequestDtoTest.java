package io.github.kubaj12.personal_task_manager_api.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class RegistrationRequestDtoTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldValidateCorrectEmailAndPassword() {
        RegistrationRequestDto validDto = new RegistrationRequestDto("test@example.com", "password1234");
        Set<ConstraintViolation<RegistrationRequestDto>> violations = validator.validate(validDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldNotValidateEmptyEmail() {

        RegistrationRequestDto validDto = new RegistrationRequestDto("", "password1234");
        Set<ConstraintViolation<RegistrationRequestDto>> violations = validator.validate(validDto);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        assertThat(violations).extracting(v -> v.getMessage()).contains("E-mail cannot be empty");
    }

    @Test
    void shouldNotValidateIncorrectEmail() {
        RegistrationRequestDto validDto = new RegistrationRequestDto("test@example", "password1234");
        Set<ConstraintViolation<RegistrationRequestDto>> violations = validator.validate(validDto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        assertThat(violations).extracting(v -> v.getMessage()).contains("Email is incorrect");
    }

    @Test
    void shouldNotValidateEmptyPassword() {
        RegistrationRequestDto validDto = new RegistrationRequestDto("test@example.com", "");
        Set<ConstraintViolation<RegistrationRequestDto>> violations = validator.validate(validDto);

        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());

        assertThat(violations).extracting(v -> v.getMessage()).containsExactlyInAnyOrder("Password cannot be empty", "The password should be between 12 and 128 characters long");
    }

    @Test
    void shouldNotValidateTooShortPassword() {
        RegistrationRequestDto validDto = new RegistrationRequestDto("test@example.com", "q2L");
        Set<ConstraintViolation<RegistrationRequestDto>> violations = validator.validate(validDto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        assertThat(violations).extracting(v -> v.getMessage()).contains("The password should be between 12 and 128 characters long");
    }

    @Test
    void shouldNotValidateTooLongPassword() {
        String password = "a".repeat(150);
        RegistrationRequestDto validDto = new RegistrationRequestDto("test@example.com", password);
        Set<ConstraintViolation<RegistrationRequestDto>> violations = validator.validate(validDto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        assertThat(violations).extracting(v -> v.getMessage()).contains("The password should be between 12 and 128 characters long");
    }
}
