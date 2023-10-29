package com.aston.util.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectDto {
    private int id;
    private String name;
    private String description;
}
