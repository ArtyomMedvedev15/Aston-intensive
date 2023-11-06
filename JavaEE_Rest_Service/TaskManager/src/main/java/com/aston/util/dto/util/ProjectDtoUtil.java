package com.aston.util.dto.util;

import com.aston.entities.Project;
import com.aston.util.dto.ProjectDto;
import com.aston.util.dto.ProjectUpdateDto;

public class ProjectDtoUtil {
    public static Project fromDto(ProjectDto projectDto) {
        Project projectEntity = new Project();
        projectEntity.setId(projectDto.getId());
        projectEntity.setId(projectDto.getId());
        projectEntity.setName(projectDto.getName());
        projectEntity.setDescription(projectDto.getDescription());
        return projectEntity;
    }



    public static Project fromDto(ProjectUpdateDto projectUpdateDto) {
        Project projectEntity = new Project();
        projectEntity.setId(projectUpdateDto.getId());
        projectEntity.setName(projectUpdateDto.getName());
        projectEntity.setDescription(projectUpdateDto.getDescription());
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

    public static ProjectDto fromEntityWithTask(Project projectEntity) {
        ProjectDto projectDto = ProjectDto.builder()
                .id(projectEntity.getId())
                .name(projectEntity.getName())
                .description(projectEntity.getDescription())
                .build();
        return projectDto;
    }
}
