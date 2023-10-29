package com.aston.entities;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class Task {
    private Long id;
    private String title;
    private String description;
    private Date deadline;
    private String status;
    private int projectId;
}
