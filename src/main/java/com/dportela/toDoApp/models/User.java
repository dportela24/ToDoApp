package com.dportela.toDoApp.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Username must not be empty")
    private String username;

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 6)
    private String password;

    @OneToMany(mappedBy="user",
               fetch=FetchType.LAZY,
               cascade=CascadeType.ALL)
    private List<Task> tasks;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void addTask(Task new_task) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }

        tasks.add(new_task);

        new_task.setUser(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
