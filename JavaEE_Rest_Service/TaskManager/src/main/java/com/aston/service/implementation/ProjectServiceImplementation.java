package com.aston.service.implementation;

import com.aston.dao.api.ProjectDaoApi;
import com.aston.dao.api.TaskDaoApi;
import com.aston.dao.api.TransactionManager;
import com.aston.dao.api.UserTaskDaoApi;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.entities.Project;
import com.aston.entities.Task;
import com.aston.service.api.ProjectServiceApi;
import com.aston.util.dto.ProjectDto;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ProjectServiceImplementation implements ProjectServiceApi {

    private final ProjectDaoApi projectDaoApi;
    private TransactionManager transactionManager;
    private final ConnectionManager connectionManager;

    private final TaskDaoApi taskDaoApi;
    private final UserTaskDaoApi userTaskDaoApi;
    public ProjectServiceImplementation(ProjectDaoApi projectDaoApi, ConnectionManager connectionManager, TaskDaoApi taskDaoApi, UserTaskDaoApi userTaskDaoApi) {
        this.connectionManager = connectionManager;
        this.transactionManager = connectionManager.getTransactionManager();
        this.projectDaoApi = projectDaoApi;
        this.taskDaoApi = taskDaoApi;
        this.userTaskDaoApi = userTaskDaoApi;
    }

    @Override
    public int createProject(ProjectDto projectDtoSave) throws SQLException {
        Project projectEntity = fromDto(projectDtoSave);
        int projectId = 0;
        try {
            transactionManager.beginTransaction();
            projectId = projectDaoApi.createProject(projectEntity);
            transactionManager.commitTransaction();
        } catch (SQLException e) {
            transactionManager.rollbackTransaction();
            log.error("Cannot save project get exception {}", e.getMessage());
            throw e;
        }
        return projectId;
    }

    @Override
    public int updateProject(ProjectDto projectDtoSave) throws SQLException {
        Project projectEntity = fromDto(projectDtoSave);
        int projectId = 0;
        try {
            transactionManager.beginTransaction();
            projectId = projectDaoApi.updateProject(projectEntity);
            transactionManager.commitTransaction();
        } catch (SQLException e) {
            transactionManager.rollbackTransaction();
            log.error("Cannot update project get exception {}", e.getMessage());
            throw e;
        }
        return projectId;
    }

    @Override
    public int deleteProject(int projectId) throws SQLException {
        try {
            transactionManager.beginTransaction();
            projectId = projectDaoApi.deleteProject(projectId);
            transactionManager.commitTransaction();
        } catch (SQLException e) {
            transactionManager.rollbackTransaction();
            log.error("Cannot delete project get exception {}", e.getMessage());
            throw e;
        }
        return projectId;
    }
    @Override
    public ProjectDto getProjectById(int projectId) throws SQLException {
        ProjectDto projectDto;
        try {
            transactionManager.beginTransaction();
            projectDto = fromEntity(projectDaoApi.getProjectById(projectId));
            transactionManager.commitTransaction();
        } catch (SQLException e) {
            transactionManager.rollbackTransaction();
            log.error("Cannot get project by id with exception {}",e.getMessage());
            throw e;
        }
        return projectDto;
    }

    @Override
    public List<ProjectDto> getProjectByName(String name) throws SQLException {
        List<ProjectDto> projectDtoList = new ArrayList<>();
        try {
            transactionManager.beginTransaction();
            if(!name.equals("")) {
                projectDtoList = projectDaoApi.getProjectByName(name).stream().map(this::fromEntity)
                        .collect(Collectors.toList());
            }else{
                projectDtoList = projectDaoApi.getAllProject().stream().map(this::fromEntity)
                        .collect(Collectors.toList());
            }
            transactionManager.commitTransaction();
        } catch (SQLException e) {
            transactionManager.rollbackTransaction();
            log.error("Cannot get all project by name with exception {}",e.getMessage());
            e.printStackTrace();
        }
        return projectDtoList;
    }

    @Override
    public List<ProjectDto> getAllProject() throws SQLException {
        List<ProjectDto> projectDtoList;
        try {
            transactionManager.beginTransaction();
            projectDtoList = projectDaoApi.getAllProject().stream().map(this::fromEntity)
                    .collect(Collectors.toList());
            transactionManager.commitTransaction();
        } catch (SQLException e) {
            transactionManager.rollbackTransaction();
            log.error("Cannot get all project with exception {}",e.getMessage());
            throw e;
        }
        return projectDtoList;
    }



    private Project fromDto(ProjectDto projectDto) {
        Project projectEntity = Project.builder()
                .id(projectDto.getId())
                .name(projectDto.getName())
                .description(projectDto.getDescription())
                .build();
        return projectEntity;
    }

    private ProjectDto fromEntity(Project projectEntity) {
        ProjectDto projectDto = ProjectDto.builder()
                .id(projectEntity.getId())
                .name(projectEntity.getName())
                .description(projectEntity.getDescription())
                .build();
        return projectDto;
    }
}
