package com.dportela.toDoApp.controllers;

import com.dportela.toDoApp.models.Task;
import com.dportela.toDoApp.models.User;
import com.dportela.toDoApp.models.UserDetailsImpl;
import com.dportela.toDoApp.repositories.TaskRepository;
import com.dportela.toDoApp.repositories.UserRepository;
import com.dportela.toDoApp.responses.TaskSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @GetMapping
    public ResponseEntity getTasks() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Could not found user..."));

        List<Task> tasks = user.getTasks();
        List<TaskSummary> taskSummaries = tasks.stream()
                            .map(task -> new TaskSummary(task.getId(), task.getTitle(), task.isCompleted()))
                            .collect(Collectors.toList());

        return ResponseEntity.ok(taskSummaries);
    }

    @GetMapping("/{task_id}")
    public ResponseEntity getTask(@PathVariable int task_id) {
        int user_id = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Optional<Task> task = taskRepository.findById(task_id);

        if (task.isEmpty() || task.get().getUser().getId() != user_id){
            return new ResponseEntity("Task not found", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity addTask(@Valid @RequestBody Task task) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Could not found user..."));

        task.setId(0);

        user.addTask(task);
        taskRepository.save(task);

        return ResponseEntity.ok(task);
    }

    @PutMapping("/{task_id}")
    public ResponseEntity updateTask(@Valid @RequestBody Task updated_task, @PathVariable int task_id) {
        int user_id = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Optional<Task> old_task_optional = taskRepository.findById(task_id);

        if (old_task_optional.isEmpty() || old_task_optional.get().getUser().getId() != user_id) {
            return new ResponseEntity("Task not found", HttpStatus.NOT_FOUND);
        }

        Task task = old_task_optional.get();

        task.setTitle(updated_task.getTitle());
        task.setDescription(updated_task.getDescription());
        task.setCompleted(updated_task.isCompleted());

        taskRepository.save(task);

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{task_id}")
    public ResponseEntity removeTask(@PathVariable int task_id) {
        int user_id = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Optional<Task> task = taskRepository.findById(task_id);

        if (task.isEmpty() || task.get().getUser().getId() != user_id) {
            return new ResponseEntity("Task not found", HttpStatus.NOT_FOUND);
        }

        taskRepository.deleteById(task_id);

        return ResponseEntity.ok(task.get());
    }
}
