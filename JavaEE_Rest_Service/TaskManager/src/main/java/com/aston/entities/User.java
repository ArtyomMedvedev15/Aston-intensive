package com.aston.entities;

import jakarta.persistence.*;

import lombok.Data;


import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users", schema = "taskmanager")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taskmanager.taskmanager_sequence")
    @SequenceGenerator(name = "taskmanager.taskmanager_sequence", sequenceName = "taskmanager.taskmanager_sequence",allocationSize = 1)
    private Long id;
    private String username;
    private String email;

    @OneToMany(mappedBy = "user")
    private Set<UserTask> userTasks = new HashSet<UserTask>();

}
