package com.aston.util.dto;

import com.aston.entities.Project;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private Date deadline;
    private String status;
    private ProjectDto project;
    private Long projectId;
}
