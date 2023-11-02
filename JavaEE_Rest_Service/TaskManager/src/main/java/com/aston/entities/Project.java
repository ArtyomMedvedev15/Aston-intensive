package com.aston.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taskmanager_sequence")
    @SequenceGenerator(name = "taskmanager_sequence", sequenceName = "taskmanager_sequence")
    private Long id;
    private String name;
    private String description;
}
