package com.aston.service.implementation;

import com.aston.dao.api.TaskDaoApi;
import com.aston.dao.api.TransactionManager;
import com.aston.entities.Task;
import com.aston.service.api.ProjectServiceApi;
import com.aston.service.api.TaskServiceApi;
import com.aston.util.dto.TaskDto;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TaskServiceImplementation implements TaskServiceApi {

    private final TaskDaoApi taskDao;
    private final TransactionManager transactionManager;
    private final ProjectServiceApi projectService;

    public TaskServiceImplementation(TaskDaoApi taskDao, TransactionManager transactionManager, ProjectServiceApi projectService) {
        this.taskDao = taskDao;
        this.transactionManager = transactionManager;
        this.projectService = projectService;
    }

    @Override
    public int createTask(TaskDto taskDtoSave) throws SQLException {
        Task taskEntity = fromDto(taskDtoSave);
        int taskId = 0;
        try {
            taskId = taskDao.createTask(taskEntity);
        } catch (SQLException e) {
            log.error("Cannot save task get exception {}", e.getMessage());
            throw e;
        }
        return taskId;
    }

    @Override
    public TaskDto getTaskById(int taskId) throws SQLException {
        TaskDto taskDto;
        try {
            Task taskById = taskDao.getTaskById(taskId);
            taskDto = fromEntity(taskById);
            taskDto.setProject(projectService.getProjectById(taskById.getProjectId()));
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
            taskDtoList.forEach(o1-> {
                try {
                    o1.setProject(projectService.getProjectById(o1.getProjectId()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
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
            setProjectDto(taskDtoListByProject);
        } catch (SQLException e) {
            log.error("Cannot get all task by project with with exception {}",e.getMessage());
            e.printStackTrace();
        }
        return taskDtoListByProject;
    }



    @Override
    public int updateTask(TaskDto taskDtoUpdate) throws SQLException {
        Task taskEntity = fromDto(taskDtoUpdate);
        int taskId = 0;
        try {
            taskId = taskDao.updateTask(taskEntity);
        } catch (SQLException e) {
            log.error("Cannot update task get exception {}", e.getMessage());
            throw e;
        }
        return taskId;
    }

    @Override
    public int deleteTask(int taskId) throws SQLException {
        try {
            taskId = taskDao.deleteTask(taskId);
        } catch (SQLException e) {
            log.error("Cannot delete task get exception {}", e.getMessage());
            throw e;
        }
        return taskId;
    }

    private void setProjectDto(List<TaskDto> taskDtoList) {
        taskDtoList.forEach(o1-> {
            try {
                o1.setProject(projectService.getProjectById(o1.getProjectId()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    private Task fromDto(TaskDto taskDto) {
        Task taskEntity = Task.builder()
                .id(taskDto.getId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .deadline(taskDto.getDeadline())
                .status(taskDto.getStatus())
                .projectId(taskDto.getProjectId())
                .build();
        return taskEntity;
    }

    private TaskDto fromEntity(Task taskEntity) {
        TaskDto taskDto = TaskDto.builder()
                .id(taskEntity.getId())
                .title(taskEntity.getTitle())
                .description(taskEntity.getDescription())
                .deadline(taskEntity.getDeadline())
                .status(taskEntity.getStatus())
                .projectId(taskEntity.getProjectId())
                .build();
        return taskDto;
    }
}
