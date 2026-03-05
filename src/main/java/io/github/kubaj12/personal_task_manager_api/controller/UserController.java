package io.github.kubaj12.personal_task_manager_api.controller;

import io.github.kubaj12.personal_task_manager_api.entity.User;
import io.github.kubaj12.personal_task_manager_api.repository.TaskRepository;
import io.github.kubaj12.personal_task_manager_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final UserRepository userRepository;
  private final TaskRepository taskRepository;

  @GetMapping
  //    public ResponseEntity<List<User>> getAllUsers() {

  public ResponseEntity<Void> getAllUsers() {
//    User user = new User();

    List<User> users = userRepository.findAll();

    log.info(users.size() + " users found");

    return ResponseEntity.status(201).build();
    //        return ResponseEntity.ok(userRepository.findAll());
  }
}
