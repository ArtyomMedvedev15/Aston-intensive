package com.aston.util.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class TaskUpdateDto {
    private Long id;
    private String title;
    private String description;
    private Date deadline;
    private String status;
    private ProjectDto project;
    private Long projectId;
}
