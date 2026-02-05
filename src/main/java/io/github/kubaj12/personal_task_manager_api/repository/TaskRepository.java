package io.github.kubaj12.personal_task_manager_api.repository;

import io.github.kubaj12.personal_task_manager_api.entity.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TaskRepository extends CrudRepository<Task, Long>, PagingAndSortingRepository<Task, Long> {}
