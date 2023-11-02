package com.aston.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taskmanager_sequence")
    @SequenceGenerator(name = "taskmanager_sequence", sequenceName = "taskmanager_sequence")
    private Long id;
    private String username;
    private String email;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_task",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private Set<Task>userTask = new HashSet<>();

}
