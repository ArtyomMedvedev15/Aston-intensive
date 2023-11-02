package com.aston.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taskmanager_sequence")
    @SequenceGenerator(name = "taskmanager_sequence", sequenceName = "taskmanager_sequence")
    private Long id;
    private String title;
    private String description;
    private Date deadline;
    private String status;
    private int projectId;
    @ManyToMany(mappedBy = "userTask",fetch = FetchType.LAZY)
    private Set<User> taskUser = new HashSet<>();

}
