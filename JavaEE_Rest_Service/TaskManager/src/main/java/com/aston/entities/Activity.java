package com.aston.entities;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taskmanager.taskmanager_sequence")
    @SequenceGenerator(name = "taskmanager.taskmanager_sequence", sequenceName = "taskmanager.taskmanager_sequence",allocationSize = 1)
    private Long id;
}
