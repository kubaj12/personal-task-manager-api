package io.github.kubaj12.personal_task_manager_api.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

public class OtpConfigPropertiesTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner().withUserConfiguration(TestConfig.class);

    @Configuration(proxyBeanMethods = false)
    @EnableConfigurationProperties(OtpConfigProperties.class)
    static class TestConfig {}
    

    @Test
    void shouldValidateIfPropertiesAreValid() {
        contextRunner
                .withPropertyValues(
                        "otp.email-verification.ttl: 6m",
                        "otp.email-verification.length: 6"
                )
                .run(context -> {
                    assertThat(context).hasNotFailed();
                    OtpConfigProperties prop = context.getBean(OtpConfigProperties.class);
                    assertThat(prop.length()).isEqualTo(6);
                    assertThat(prop.ttl()).isEqualTo(Duration.ofMinutes(6));
                });
    }

    @Test
    void shouldNotValidateIfTtlIsEqualToZero() {
        contextRunner
                .withPropertyValues(
                        "otp.email-verification.ttl: 0m",
                        "otp.email-verification.length: 6"
                )
                .run(context -> {
                    assertThat(context).hasFailed();
                    assertThat(context).getFailure().hasRootCauseInstanceOf(org.springframework.boot.context.properties.bind.validation.BindValidationException.class);
                });
    }

    @Test
    void shouldNotValidateIfLengthIsEqualToZero() {
        contextRunner
                .withPropertyValues(
                        "otp.email-verification.ttl: 6m",
                        "otp.email-verification.length: 0"
                )
                .run(context -> {
                    assertThat(context).hasFailed();
                    assertThat(context).getFailure().hasRootCauseInstanceOf(org.springframework.boot.context.properties.bind.validation.BindValidationException.class);
                });
    }
}
