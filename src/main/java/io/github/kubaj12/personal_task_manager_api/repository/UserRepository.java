package io.github.kubaj12.personal_task_manager_api.repository;

import io.github.kubaj12.personal_task_manager_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
