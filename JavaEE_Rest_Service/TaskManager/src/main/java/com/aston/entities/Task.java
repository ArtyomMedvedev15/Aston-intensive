package com.aston.entities;

import jakarta.persistence.*;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString
@Entity
@Table(name = "task", schema = "taskmanager")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taskmanager.taskmanager_sequence")
    @SequenceGenerator(name = "taskmanager.taskmanager_sequence", sequenceName = "taskmanager.taskmanager_sequence",allocationSize = 1)
    private Long id;
    private String title;
    private String description;
    private Date deadline;
    private String status;

    @ManyToOne
    @JoinColumn(name = "project_id",referencedColumnName = "id")
    private Project project;

    @ManyToMany(mappedBy = "userTask",cascade = CascadeType.ALL)
    private Set<User> taskUser = new HashSet<>();

}
