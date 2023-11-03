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
import com.aston.util.dto.util.ProjectDtoUtil;
import com.aston.util.dto.TaskDto;
import com.aston.util.dto.util.TaskDtoUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.aston.util.dto.util.TaskDtoUtil.fromDto;
import static com.aston.util.dto.util.TaskDtoUtil.fromEntity;

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
    public Long createTask(TaskDto taskDtoSave) throws SQLException, TaskInvalidParameterException {
        Task taskEntity = fromDto(taskDtoSave);
        Long taskId;
        try {
            taskId = ValidationDto(taskEntity);
        } catch (SQLException e) {
             log.error("Cannot save task get exception {}", e.getMessage());
            throw e;
        }
        return taskId;
    }

    private Long ValidationDto(Task taskEntity) throws SQLException, TaskInvalidParameterException {
        Long taskId;
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
    public TaskDto getTaskById(Long taskId) throws SQLException, TaskNotFoundException{
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
    public List<TaskDto> getAllTasks(){
        List<TaskDto> taskDtoList = new ArrayList<>();
        try {
            taskDtoList = taskDao.getAllTasks().stream().map(TaskDtoUtil::fromEntity)
                    .collect(Collectors.toList());
        } catch (SQLException e) {

            log.error("Cannot get all task with exception {}",e.getMessage());
            e.printStackTrace();
        }
        return taskDtoList;
    }

    @Override
    public Set<TaskDto> getAllTasksByProject(Long projectId) throws ProjectNotFoundException {
        Set<TaskDto> taskDtoListByProject = new HashSet<>();
        try {
            if (projectService.getProjectById(projectId)==null) {
                taskDtoListByProject = projectService.getAllTasksByProject(projectId);
            }else {
                throw new ProjectNotFoundException(String.format("Project with id %s was not found",projectId));
            }
         } catch (SQLException e) {
             log.error("Cannot get all task by project with with exception {}",e.getMessage());
             e.printStackTrace();
        }
        return taskDtoListByProject;
    }



    @Override
    public Long updateTask(TaskDto taskDtoUpdate) throws SQLException, TaskInvalidParameterException {
        Task taskEntity = fromDto(taskDtoUpdate);
        Long taskId;
        try {
            taskId = ValidationDto(taskEntity);
        } catch (SQLException e) {
             log.error("Cannot update task get exception {}", e.getMessage());
            throw e;
        }
        return taskId;
    }

    @Override
    public Long deleteTask(Long taskId) throws SQLException, TaskNotFoundException {
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


}
