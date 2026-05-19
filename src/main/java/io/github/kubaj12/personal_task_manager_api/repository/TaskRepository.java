package io.github.kubaj12.personal_task_manager_api.repository;

import io.github.kubaj12.personal_task_manager_api.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {}
