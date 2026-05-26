package io.github.kubaj12.personal_task_manager_api.repository;

import io.github.kubaj12.personal_task_manager_api.model.entity.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(UserRepositoryTest.class);

    @Test
    void shouldLoadDataSqlFile() {
        List<User> users = userRepository.findAll();
        logger.info("Tu jest logger: " + String.valueOf(users.size()));
        assertEquals(1, users.size());
    }
}
