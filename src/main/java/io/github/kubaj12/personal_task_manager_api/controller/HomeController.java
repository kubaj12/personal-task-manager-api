package io.github.kubaj12.personal_task_manager_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HomeController {
  @GetMapping("/")
  public String root(Principal principal) {
    return principal.getName();
  }
}
