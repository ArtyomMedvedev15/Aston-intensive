package com.aston.util.dto;

import com.aston.entities.Project;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectDto {
    private Long id;
    private String name;
    private String description;

}
