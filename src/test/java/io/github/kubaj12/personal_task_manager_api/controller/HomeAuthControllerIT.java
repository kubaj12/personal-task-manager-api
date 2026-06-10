package io.github.kubaj12.personal_task_manager_api.controller;

import io.github.kubaj12.personal_task_manager_api.config.SecurityConfig;
import io.github.kubaj12.personal_task_manager_api.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({HomeController.class, AuthController.class})
@Import({SecurityConfig.class, TokenService.class})
public class HomeAuthControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    void whenRootUnauthenticatedThen401() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().isUnauthorized());
    }

    @Test
    void whenRootAuthenticatedThenSaysHelloUser() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/token").with(httpBasic("Test","test"))).andExpect(status().isOk()).andReturn();

        String token = result.getResponse().getContentAsString();

        this.mockMvc.perform(get("/").header("Authorization", "Bearer " + token)).andExpect(content().string("Test"));
    }
}
