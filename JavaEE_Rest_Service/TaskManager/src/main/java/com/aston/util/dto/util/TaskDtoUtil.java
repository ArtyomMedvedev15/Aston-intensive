package com.aston.util.dto.util;

import com.aston.entities.Project;
import com.aston.entities.Task;
import com.aston.util.dto.ProjectDto;
import com.aston.util.dto.TaskDto;
import com.aston.util.dto.util.ProjectDtoUtil;

public class TaskDtoUtil {
    public static Task fromDto(TaskDto taskDto) {
        Task taskEntity = new Task();
        taskEntity.setId(taskDto.getId());
        taskEntity.setTitle(taskDto.getTitle());
        taskEntity.setDescription(taskDto.getDescription());
        taskEntity.setDeadline(taskDto.getDeadline());
        taskEntity.setStatus(taskDto.getStatus());
        taskEntity.setProject(ProjectDtoUtil.fromDto(taskDto.getProject()));
        return taskEntity;
    }

    public static Task fromDto(TaskDto taskDto, ProjectDto projectDto) {
        Task taskEntity = new Task();
        taskEntity.setTitle(taskDto.getTitle());
        taskEntity.setDescription(taskDto.getDescription());
        taskEntity.setDeadline(taskDto.getDeadline());
        taskEntity.setStatus(taskDto.getStatus());
        taskEntity.setProject(ProjectDtoUtil.fromDto(projectDto));
        return taskEntity;
    }

    public static TaskDto fromEntity(Task taskEntity) {
        TaskDto taskDto = TaskDto.builder()
                .id(taskEntity.getId())
                .title(taskEntity.getTitle())
                .description(taskEntity.getDescription())
                .deadline(taskEntity.getDeadline())
                .status(taskEntity.getStatus())
                .project(ProjectDtoUtil.fromEntity(taskEntity.getProject()))
                .projectId(taskEntity.getProject().getId())
                .build();
        return taskDto;
    }
}
