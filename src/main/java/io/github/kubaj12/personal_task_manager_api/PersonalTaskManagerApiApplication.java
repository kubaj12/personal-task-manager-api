package io.github.kubaj12.personal_task_manager_api;

import io.github.kubaj12.personal_task_manager_api.config.RsaKeyProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class PersonalTaskManagerApiApplication {

  public static void main(String[] args) {

    SpringApplication.run(PersonalTaskManagerApiApplication.class, args);
  }
}
