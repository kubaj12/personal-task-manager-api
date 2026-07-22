package io.github.kubaj12.personal_task_manager_api.repository;

import io.github.kubaj12.personal_task_manager_api.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTestIT {
  @Autowired
  TestEntityManager entityManager;
  @Autowired
  UserRepository userRepository;

  @Test
  void shouldAddNewUser() {
    User user = User.builder().email("test@test.com.pl").passwordHash("hash").build();
    User afterSave = userRepository.save(user);
    assertNotNull(afterSave.getId());

    entityManager.flush();
    entityManager.clear();

    User found = entityManager.find(User.class, afterSave.getId());
    assertNotNull(found);
    assertEquals("test@test.com.pl", found.getEmail());
    assertEquals("hash", found.getPasswordHash());
  }

  @Test
  void shouldReturnUserByEmail() {
    User user = User.builder().email("test@test.com.pl").passwordHash("hash").build();
    entityManager.persist(user);

    entityManager.flush();
    entityManager.clear();

    Optional<User> optFound = userRepository.findByEmail("test@test.com.pl");
    assertTrue(optFound.isPresent());
    User found = optFound.get();
    assertEquals("test@test.com.pl", found.getEmail());
    assertEquals("hash", found.getPasswordHash());
  }

  @Test
  void shouldReturnEmptyWhenUserNotFound() {
    Optional<User> found = userRepository.findByEmail("thisemaildoesntexist");
    assertTrue(found.isEmpty());
  }

  @Test
  void shouldThrowExceptionWhenCreatingDuplicateEntity() {
    User user = User.builder().email("test@test.com.pl").passwordHash("hash").build();

    entityManager.persist(user);
    entityManager.flush();

    User dupliUser = User.builder().email("test@test.com.pl").passwordHash("hash").build();
    assertThrows(DataIntegrityViolationException.class, () -> {
      userRepository.saveAndFlush(dupliUser);
    });
  }

  @Test
  void shouldDeleteUser() {
    User user = User.builder().email("test@test.com.pl").passwordHash("hash").build();

    entityManager.persist(user);
    entityManager.flush();
    entityManager.clear();
    
    userRepository.delete(user);

    User find = entityManager.find(User.class, user.getId());

    assertNull(find);
  }
}
