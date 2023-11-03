package com.aston.service.implementation;

import com.aston.dao.api.TaskDaoApi;
import com.aston.dao.api.TransactionManager;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.entities.Task;
import com.aston.service.api.ProjectServiceApi;
import com.aston.service.api.TaskServiceApi;
import com.aston.util.ProjectNotFoundException;
import com.aston.util.TaskInvalidParameterException;
import com.aston.util.TaskNotFoundException;
import com.aston.util.dto.ProjectDtoUtil;
import com.aston.util.dto.TaskDto;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TaskServiceImplementation implements TaskServiceApi {

    private final TaskDaoApi taskDao;
    private TransactionManager transactionManager;
    private final ProjectServiceApi projectService;
    private final ConnectionManager connectionManager;

    public TaskServiceImplementation(TaskDaoApi taskDao, ProjectServiceApi projectService, ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.transactionManager = connectionManager.getTransactionManager();
        this.taskDao = taskDao;
        this.projectService = projectService;
     }

    @Override
    public int createTask(TaskDto taskDtoSave) throws SQLException, TaskInvalidParameterException {
        Task taskEntity = fromDto(taskDtoSave);
        int taskId = 0;
        try {
            taskId = ValidationDto(taskEntity);
        } catch (SQLException e) {
             log.error("Cannot save task get exception {}", e.getMessage());
            throw e;
        }
        return taskId;
    }

    private int ValidationDto(Task taskEntity) throws SQLException, TaskInvalidParameterException {
        int taskId;
        if((taskEntity.getTitle().length()>5 && taskEntity.getTitle().length()<256) &&
               (taskEntity.getDescription().length()>10 && taskEntity.getDescription().length()<512)&&
               (taskEntity.getStatus().length()>3 && taskEntity.getStatus().length()<50)){
           taskId = taskDao.createTask(taskEntity);
       }else{
           throw new TaskInvalidParameterException("Task parameter is invalid, try yet");
       }
        return taskId;
    }

    @Override
    public TaskDto getTaskById(int taskId) throws SQLException, TaskNotFoundException, ProjectNotFoundException {
        TaskDto taskDto;
        try {
            Task taskById = taskDao.getTaskById(taskId);
            if(taskById!=null) {
                taskDto = fromEntity(taskById);
                taskDto.setProject(ProjectDtoUtil.fromEntity(taskById.getProject()));
            }else{
                throw new TaskNotFoundException(String.format("Task with id %s was not found",taskId));
            }
        } catch (SQLException e) {
            log.error("Cannot get project by id with exception {}", e.getMessage());
            throw e;
        }
        return taskDto;
    }

    @Override
    public List<TaskDto> getAllTasks() throws SQLException {
        List<TaskDto> taskDtoList = new ArrayList<>();
        try {
            taskDtoList = taskDao.getAllTasks().stream().map(this::fromEntity)
                    .collect(Collectors.toList());
        } catch (SQLException e) {

            log.error("Cannot get all task with exception {}",e.getMessage());
            e.printStackTrace();
        }
        return taskDtoList;
    }

    @Override
    public List<TaskDto> getAllTasksByProject(int projectId) throws SQLException {
        List<TaskDto> taskDtoListByProject = new ArrayList<>();
        try {
             taskDtoListByProject = taskDao.getAllTasksByProject(projectId).stream().map(this::fromEntity)
                    .collect(Collectors.toList());
         } catch (SQLException e) {
             log.error("Cannot get all task by project with with exception {}",e.getMessage());
            e.printStackTrace();
        }
        return taskDtoListByProject;
    }



    @Override
    public int updateTask(TaskDto taskDtoUpdate) throws SQLException, TaskInvalidParameterException {
        Task taskEntity = fromDto(taskDtoUpdate);
        int taskId = 0;
        try {
            taskId = ValidationDto(taskEntity);
        } catch (SQLException e) {
             log.error("Cannot update task get exception {}", e.getMessage());
            throw e;
        }
        return taskId;
    }

    @Override
    public int deleteTask(int taskId) throws SQLException, TaskNotFoundException {
        try {
             Task taskById = taskDao.getTaskById(taskId);
            if(taskById!=null) {
                taskId = taskDao.deleteTask(taskId);
            }else{
                throw new TaskNotFoundException(String.format("Task with id %s was not found",taskId));
            }
         } catch (SQLException e) {
             log.error("Cannot delete task get exception {}", e.getMessage());
            throw e;
        }
        return taskId;
    }

    private Task fromDto(TaskDto taskDto) {
        Task taskEntity = new Task();
                taskEntity.setId(taskDto.getId());
                taskEntity.setTitle(taskDto.getTitle());
                taskEntity.setDescription(taskDto.getDescription());
                taskEntity.setDeadline(taskDto.getDeadline());
                taskEntity.setStatus(taskDto.getStatus());
                taskEntity.setProject(ProjectDtoUtil.fromDto(taskDto.getProject()));
        return taskEntity;
    }

    private TaskDto fromEntity(Task taskEntity) {
        TaskDto taskDto = TaskDto.builder()
                .id(taskEntity.getId())
                .title(taskEntity.getTitle())
                .description(taskEntity.getDescription())
                .deadline(taskEntity.getDeadline())
                .status(taskEntity.getStatus())
                .project(ProjectDtoUtil.fromEntity(taskEntity.getProject()))
                .build();
        return taskDto;
    }
}
