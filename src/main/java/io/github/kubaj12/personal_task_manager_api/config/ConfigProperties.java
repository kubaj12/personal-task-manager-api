package io.github.kubaj12.personal_task_manager_api.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ OtpConfigProperties.class })
public class ConfigProperties {
}
