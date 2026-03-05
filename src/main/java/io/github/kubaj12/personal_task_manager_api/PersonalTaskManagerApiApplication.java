package io.github.kubaj12.personal_task_manager_api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@SpringBootApplication
public class PersonalTaskManagerApiApplication {

  public static void main(String[] args) {

    SpringApplication.run(PersonalTaskManagerApiApplication.class, args);
  }
}
