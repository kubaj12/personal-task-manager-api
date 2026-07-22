package io.github.kubaj12.personal_task_manager_api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
public class HomeController {

  @Autowired
  JavaMailSender javaMailSender;

  @GetMapping("/")
  public String root(Principal principal) {

    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("test@test.com");
    message.setTo("test@reciver.com");
    message.setSubject("Wiadomosc testowa");
    message.setText("To jest wnetrze wiadomosci testowej");
    javaMailSender.send(message);

//    return principal.getName();
    return "Wiadomosc zostal wyslana.";
  }
}
