package com.aston.service.implementation;

import com.aston.dao.api.TaskDaoApi;
import com.aston.dao.api.UserDaoApi;
import com.aston.dao.implementation.UserTaskDaoImplementation;
import com.aston.entities.Task;
import com.aston.entities.User;
import com.aston.entities.UserTask;
import com.aston.service.api.UserTaskServiceApi;
import com.aston.util.TaskNotFoundException;
import com.aston.util.TransactionException;
import com.aston.util.UserNotFoundException;
import com.aston.util.UserTaskAlreadyExistsException;
import com.aston.util.dto.TaskDto;
import com.aston.util.dto.UserTaskDto;
import com.aston.util.dto.UserTaskSaveDto;
import com.aston.util.dto.util.TaskDtoUtil;
import com.aston.util.dto.util.UserDtoUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class UserTaskServiceImplementation implements UserTaskServiceApi {

    private final SessionFactory sessionFactory;
    private final TaskDaoApi taskDaoApi;
    private final UserDaoApi userDaoApi;
    private final UserTaskDaoImplementation userTaskDaoImplementation;
    public UserTaskServiceImplementation(UserDaoApi userDaoApi, SessionFactory sessionFactory, TaskDaoApi taskDaoApi, UserTaskDaoImplementation userTaskDaoImplementation) {
        this.sessionFactory = sessionFactory;
        this.taskDaoApi = taskDaoApi;
        this.userDaoApi = userDaoApi;
        this.userTaskDaoImplementation = userTaskDaoImplementation;
    }


    @Override
    public int createUserTask(UserTaskSaveDto userTaskDtoSave) throws UserNotFoundException, TaskNotFoundException, UserTaskAlreadyExistsException {
        int userTaskId = 0;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {

                User userById = userDaoApi.getUserById(userTaskDtoSave.getUserId());
                Task taskById = taskDaoApi.getTaskById(userTaskDtoSave.getTaskId());
                if (userById == null) {
                    log.info("User with id {} not found in {}", userTaskDtoSave.getUserId(), new Date());
                    throw new UserNotFoundException(String.format("User with id %s was not found", userTaskDtoSave.getUserId()));
                }
                if (taskById == null) {
                    log.info("Task with id {} not found in {}", userTaskDtoSave.getTaskId(), new Date());
                    throw new TaskNotFoundException(String.format("Task with id %s was not found", userTaskDtoSave.getTaskId()));
                }
                if (userTaskDaoImplementation.getUserTaskByUserAndTask(userById,taskById)!=null) {
                    log.info("Task was already added to user with {} in {}", userTaskDtoSave.getUserId(), new Date());
                    throw new UserTaskAlreadyExistsException(String.format("Task with id %s already add to user with id %s",
                            userTaskDtoSave.getTaskId(), userTaskDtoSave.getUserId()));
                }
                UserTask userTask = new UserTask();
                userTask.setUser(userById);
                userTask.setTask(taskById);
                userTaskDaoImplementation.createUserTask(userTask);
                transaction.commit();
            } catch (HibernateException | SQLException exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            } catch (UserNotFoundException e) {
                throw new UserNotFoundException(e.getMessage());
            } catch (TaskNotFoundException e) {
                throw new TaskNotFoundException(e.getMessage());
            }
            return userTaskId;
        }
    }

    @Override
    public UserTaskDto getAllUserTaskByUser(Long userid) throws UserNotFoundException {
        UserTaskDto userTaskFullDto;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                User userById = userDaoApi.getUserById(userid);
                if (userById == null) {
                    log.info("User with id {} not found in {}", userid, new Date());
                    throw new UserNotFoundException(String.format("User with id %s was not found", userid));
                }
                Set<TaskDto>userTask = userTaskDaoImplementation.getAllUsersTaskByUser(userid).stream()
                        .map(o1-> TaskDtoUtil.fromEntity(o1.getTask()))
                        .collect(Collectors.toSet());
                userTaskFullDto = UserTaskDto.builder()
                        .user(UserDtoUtil.fromEntity(userById))
                        .tasks(userTask)
                        .build();
                transaction.commit();
                log.info("Get all task by user with id {} in {}",userid,new Date());
            }catch (HibernateException exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            }
        }
        return userTaskFullDto;
    }
    @Override
    public Long deleteUserTask(Long userId,Long taskDeleteId) throws SQLException, UserNotFoundException, TaskNotFoundException {
         try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                User userById = userDaoApi.getUserById(userId);
                Task taskById = taskDaoApi.getTaskById(taskDeleteId);
                if (userById == null) {
                    log.info("User with id {} not found in {}", userId, new Date());
                    throw new UserNotFoundException(String.format("User with id %s was not found", userId));
                }
                if (taskById == null) {
                    log.info("Task with id {} not found in {}", taskDeleteId, new Date());
                    throw new TaskNotFoundException(String.format("Task with id %s was not found", taskDeleteId));
                }
                UserTask userTaskDelete = new UserTask();
                userTaskDelete.setUser(userById);
                userTaskDelete.setTask(taskById);
                Long userTaskDeleteId = userTaskDaoImplementation.deleteUserTask(userTaskDelete);
                return userTaskDeleteId;
            }catch (HibernateException exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            }
        }
    }


}
