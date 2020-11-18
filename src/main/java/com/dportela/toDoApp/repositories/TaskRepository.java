package com.dportela.toDoApp.repositories;

import com.dportela.toDoApp.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
