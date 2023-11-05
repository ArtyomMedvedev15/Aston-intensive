package com.aston.service.implementation;

import com.aston.dao.api.ProjectDaoApi;
import com.aston.dao.api.TaskDaoApi;
import com.aston.entities.Project;
import com.aston.entities.Task;
import com.aston.service.api.ProjectServiceApi;
import com.aston.service.api.TaskServiceApi;
import com.aston.util.ProjectNotFoundException;
import com.aston.util.TaskInvalidParameterException;
import com.aston.util.TaskNotFoundException;
import com.aston.util.TransactionException;
import com.aston.util.dto.ProjectDto;
import com.aston.util.dto.util.ProjectDtoUtil;
import com.aston.util.dto.TaskDto;
import com.aston.util.dto.util.TaskDtoUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static com.aston.util.dto.util.TaskDtoUtil.fromDto;
import static com.aston.util.dto.util.TaskDtoUtil.fromEntity;

@Slf4j
public class TaskServiceImplementation implements TaskServiceApi {

    private final TaskDaoApi taskDao;
    private final ProjectServiceApi projectService;
    private final SessionFactory sessionFactory;
    private final ProjectDaoApi projectDaoApi;
    public TaskServiceImplementation(TaskDaoApi taskDao, ProjectServiceApi projectService, SessionFactory sessionFactory, ProjectDaoApi projectDaoApi) {
        this.taskDao = taskDao;
        this.projectService = projectService;
        this.sessionFactory = sessionFactory;

        this.projectDaoApi = projectDaoApi;
    }

    @Override
    public Long createTask(TaskDto taskDtoSave) throws TaskInvalidParameterException, ProjectNotFoundException {
        Task taskEntity;
        Long taskId;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                ProjectDto projectById = projectService.getProjectById(taskDtoSave.getProjectId());
                taskEntity = fromDto(taskDtoSave,projectById);
                taskEntity.setProject(ProjectDtoUtil.fromDto(projectById));
                taskId = ValidationDto(taskEntity);
                transaction.commit();
                log.info("Save task with id {} in {}",taskId,new Date());
            }catch (HibernateException | SQLException exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            } catch (TaskInvalidParameterException e) {
                throw new TaskInvalidParameterException(e.getMessage());
            }
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

    private Long ValidationUpdateDto(Task taskEntity) throws SQLException, TaskInvalidParameterException {
        Long taskId;
        if((taskEntity.getTitle().length()>5 && taskEntity.getTitle().length()<256) &&
                (taskEntity.getDescription().length()>10 && taskEntity.getDescription().length()<512)&&
                (taskEntity.getStatus().length()>3 && taskEntity.getStatus().length()<50)){
            taskId = taskDao.updateTask(taskEntity);
        }else{
            throw new TaskInvalidParameterException("Task parameter is invalid, try yet");
        }
        return taskId;
    }

    @Override
    public TaskDto getTaskById(Long taskId){
        TaskDto taskDto;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Task taskById = taskDao.getTaskById(taskId);
                if (taskById != null) {
                    taskDto = fromEntity(taskById);
                    taskDto.setProject(ProjectDtoUtil.fromEntity(taskById.getProject()));
                    transaction.commit();
                    log.info("Get task with id {} in {}",taskById.getId(),new Date());
                } else {
                    throw new TaskNotFoundException(String.format("Task with id %s was not found", taskId));
                }
            } catch (Exception exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            }
        }
        return taskDto;
    }

    @Override
    public List<TaskDto> getAllTasks(){
        List<TaskDto> taskDtoList;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                taskDtoList = taskDao.getAllTasks().stream().map(TaskDtoUtil::fromEntity)
                        .collect(Collectors.toList());
                log.info("Get all task in {}",new Date());
            } catch (Exception exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            }
        }
        return taskDtoList;
    }

    @Override
    public Long updateTask(TaskDto taskDtoUpdate) throws TaskInvalidParameterException {
        Task taskEntity = fromDto(taskDtoUpdate);
        Long taskId;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                taskId = ValidationUpdateDto(taskEntity);
                transaction.commit();
                log.info("Update task with id {} in {}",taskId,new Date());
            } catch (HibernateException | SQLException exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            }
        }
        return taskId;
    }

    @Override
    public Long deleteTask(Long taskId) throws TaskNotFoundException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Task taskById = taskDao.getTaskById(taskId);
                if (taskById != null) {
                    taskId = taskDao.deleteTask(taskId);
                } else {
                    throw new TaskNotFoundException(String.format("Task with id %s was not found", taskId));
                }
                transaction.commit();
                log.info("Delete task with id {} in {}",taskId,new Date());
            } catch (HibernateException | SQLException exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            } catch (TaskNotFoundException e) {
                throw new TaskNotFoundException(String.format("Task with id %s was not found", taskId));
            }
        }
        return taskId;
    }

    @Override
    public Set<TaskDto> getAllTasksByProject(Long projectId) {
        List<TaskDto> taskDtoList;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                ProjectDto projectById = projectService.getProjectById(projectId);
                taskDtoList = taskDao.getAllTasks().stream().filter(o1->o1.getProject()
                                .equals(ProjectDtoUtil.fromDto(projectById)))
                        .map(TaskDtoUtil::fromEntity)
                        .collect(Collectors.toList());
                log.info("Get all task in {}",new Date());
            } catch (Exception exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            }
        }
        return new HashSet<>(taskDtoList);
    }


}
