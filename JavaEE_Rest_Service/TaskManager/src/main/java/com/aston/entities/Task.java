package com.aston.entities;

import lombok.Data;

import java.sql.Date;

@Data
public class Task {
    private Long id;
    private String title;
    private String description;
    private Date deadline;
    private String status;
    private Long projectId;
}
