package io.github.kubaj12.personal_task_manager_api.config;

import java.time.Duration;

import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
@ConfigurationProperties(prefix = "otp.email-verification")
public record OtpConfigProperties(@NotNull @DurationMin(nanos = 0, inclusive = false, message = "TTL must be greater than zero") Duration ttl, @NotNull @Positive(message = "Length must be positive") Integer length) {}
