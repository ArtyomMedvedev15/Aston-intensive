package com.aston.service.implementation;

import com.aston.dao.api.ProjectDaoApi;
import com.aston.dao.api.TaskDaoApi;
import com.aston.dao.api.TransactionManager;
import com.aston.dao.api.UserTaskDaoApi;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.entities.Project;
import com.aston.entities.Task;
import com.aston.service.api.ProjectServiceApi;
import com.aston.util.ProjectInvalidParameterException;
import com.aston.util.ProjectNotFoundException;
import com.aston.util.TaskNotFoundException;
import com.aston.util.dto.ProjectDto;
import com.aston.util.dto.ProjectDtoUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.aston.util.dto.ProjectDtoUtil.fromDto;
import static com.aston.util.dto.ProjectDtoUtil.fromEntity;

@Slf4j
public class ProjectServiceImplementation implements ProjectServiceApi {

    private final ProjectDaoApi projectDaoApi;
    private TransactionManager transactionManager;
    private ConnectionManager connectionManager;

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
    public Long createProject(ProjectDto projectDtoSave) throws SQLException, ProjectInvalidParameterException {
        Project projectEntity = fromDto(projectDtoSave);
        Long projectId;
        try {
            projectId = validationDto(projectEntity);
        } catch (SQLException e) {
            log.error("Cannot save project get exception {}", e.getMessage());
            throw e;
        }
        return projectId;
    }

    private Long validationDto(Project projectEntity) throws SQLException, ProjectInvalidParameterException {
        Long projectId;
        if((projectEntity.getName().length()>4 && projectEntity.getName().length()<256)
                &&(projectEntity.getDescription().length()>10&& projectEntity.getDescription().length()<512)) {
            projectId = projectDaoApi.createProject(projectEntity);
        }else {
            throw new ProjectInvalidParameterException("Project parameter is invalid, try yet");
        }
        return projectId;
    }

    @Override
    public Long updateProject(ProjectDto projectDtoSave) throws SQLException, ProjectInvalidParameterException {
        Project projectEntity = fromDto(projectDtoSave);
        Long projectId;
        try {
            transactionManager.beginTransaction();
            projectId = validationDto(projectEntity);
            transactionManager.commitTransaction();
        } catch (SQLException e) {
            transactionManager.rollbackTransaction();
            log.error("Cannot update project get exception {}", e.getMessage());
            throw e;
        }
        return projectId;
    }

    @Override
    public Long deleteProject(Long projectId) throws ProjectNotFoundException {
        Project projectByID = projectDaoApi.getProjectById(projectId);
        if(projectByID!=null) {
            projectId = projectDaoApi.deleteProject(projectId);
        }else{
            throw new ProjectNotFoundException(String.format("Project with id %s was not found",projectId));
        }
        projectId = projectDaoApi.deleteProject(projectId);
        return projectId;
    }
    @Override
    public ProjectDto getProjectById(Long projectId) throws ProjectNotFoundException {
        ProjectDto projectDto;
        Project projectByID = projectDaoApi.getProjectById(projectId);
        if(projectByID!=null) {
            projectDto = fromEntity(projectByID);
        }else{
            throw new ProjectNotFoundException(String.format("Project with id %s was not found",projectId));
        }
        return projectDto;
    }

    @Override
    public List<ProjectDto> getProjectByName(String name) throws SQLException {
        List<ProjectDto> projectDtoList = new ArrayList<>();
        try {
            transactionManager.beginTransaction();
            if(!name.equals("")) {
                projectDtoList = projectDaoApi.getProjectByName(name).stream().map(ProjectDtoUtil::fromEntity)
                        .collect(Collectors.toList());
            }else{
                projectDtoList = projectDaoApi.getAllProject().stream().map(ProjectDtoUtil::fromEntity)
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
            projectDtoList = projectDaoApi.getAllProject().stream().map(ProjectDtoUtil::fromEntity)
                    .collect(Collectors.toList());
            transactionManager.commitTransaction();
        } catch (SQLException e) {
            transactionManager.rollbackTransaction();
            log.error("Cannot get all project with exception {}",e.getMessage());
            throw e;
        }
        return projectDtoList;
    }


}
