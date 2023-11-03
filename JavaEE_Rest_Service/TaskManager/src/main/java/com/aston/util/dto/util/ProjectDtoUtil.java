package com.aston.util.dto.util;

import com.aston.entities.Project;
import com.aston.util.dto.ProjectDto;

public class ProjectDtoUtil {
    public static Project fromDto(ProjectDto projectDto) {
        Project projectEntity = new Project();
        projectEntity.setId(projectDto.getId());
        projectEntity.setName(projectDto.getName());
        projectEntity.setDescription(projectDto.getDescription());
        return projectEntity;
    }

    public static ProjectDto fromEntity(Project projectEntity) {
        ProjectDto projectDto = ProjectDto.builder()
                .id(projectEntity.getId())
                .name(projectEntity.getName())
                .description(projectEntity.getDescription())
                .build();
        return projectDto;
    }
}
