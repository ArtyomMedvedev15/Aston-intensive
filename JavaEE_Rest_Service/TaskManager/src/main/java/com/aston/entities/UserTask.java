package com.aston.entities;


import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(schema = "taskmanager")
public class UserTask {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taskmanager.taskmanager_sequence")
    @SequenceGenerator(name = "taskmanager.taskmanager_sequence", sequenceName = "taskmanager.taskmanager_sequence",allocationSize = 1)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Task task;

}