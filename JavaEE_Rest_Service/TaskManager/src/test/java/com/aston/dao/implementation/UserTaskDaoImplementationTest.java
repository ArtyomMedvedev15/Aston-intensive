package com.aston.dao.implementation;


import com.aston.dao.api.*;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.dao.datasource.ConnectionPoolImpl;
import com.aston.dao.datasource.TransactionManagerImpl;
import com.aston.entities.Project;
import com.aston.entities.Task;
import com.aston.entities.User;
import com.aston.entities.UserTask;
import com.aston.util.ConnectionPoolException;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class UserTaskDaoImplementationTest {
    private static UserTaskDaoImplementation userTaskDaoImplementation;
    private static ConnectionPool connectionPool;
    private static UserDaoApi userDaoApi;
    private static TaskDaoApi taskDaoApi;
    private static ProjectDaoApi projectDaoApi;
    private static SessionFactory sessionFactory;
    @BeforeClass
    public static void init() throws ConnectionPoolException {
        connectionPool = ConnectionPoolImpl.getInstance();
        connectionPool.init("database");
        TransactionManager transactionManager = new TransactionManagerImpl(connectionPool);
        ConnectionManager connectionManager = new ConnectionManager(transactionManager);
        taskDaoApi = new TaskDaoImplementation(connectionManager);
        userDaoApi = new UserDaoImplementation(connectionManager, sessionFactory);
        projectDaoApi = new ProjectDaoImplementation(connectionManager);
        userTaskDaoImplementation = new UserTaskDaoImplementation(connectionManager);
    }

    @AfterClass
    public static void destroy() throws ConnectionPoolException {
        connectionPool.destroy();
    }
    @Test
    public void CreateUserTaskTest_ReturnTrue() throws SQLException {
        Project projectSave = new Project();
        projectSave.setName("TestProject");
        projectSave.setDescription("TestProject");

        int projectId = projectDaoApi.createProject(projectSave);

        Task taskSave = new Task();
        taskSave.setTitle("Test");
        taskSave.setDescription("Test");
        taskSave.setDeadline(new Date(new java.util.Date().getTime()));
        taskSave.setStatus("Open");


        int taskId = taskDaoApi.createTask(taskSave);

        User userSave = new User();
        userSave.setEmail("testsaveuser@mail.cas");
        userSave.setUsername("usertest");


        Long userId = userDaoApi.createUser(userSave);

        UserTask userTaskSave = UserTask.builder()
                .userId(Math.toIntExact(userId))
                .taskId(taskId)
                .build();

        int userTaskSaveResult = userTaskDaoImplementation.createUserTask(userTaskSave);

        Assert.assertTrue(userTaskSaveResult>0);

        taskDaoApi.deleteTask(taskId);
        projectDaoApi.deleteProject(projectId);
        userDaoApi.deleteUser(Math.toIntExact(userId));
        userTaskDaoImplementation.deleteUserTask(userTaskSaveResult);

    }

    @Test
    public void GetAllUserTaskByUserTest_WithUserId777_ReturnTrue() throws SQLException {
        Project projectSave = new Project();
        projectSave.setName("TestProject");
        projectSave.setDescription("TestProject");

        int projectId = projectDaoApi.createProject(projectSave);

        Task taskSave = new Task();
        taskSave.setTitle("Test");
        taskSave.setDescription("Test");
        taskSave.setDeadline(new Date(new java.util.Date().getTime()));
        taskSave.setStatus("Open");


        int taskId = taskDaoApi.createTask(taskSave);

        User userSave = new User();
        userSave.setEmail("testsaveuser@mail.cas");
        userSave.setUsername("usertest");

        int userId = Math.toIntExact(userDaoApi.createUser(userSave));

        UserTask userTaskSave = UserTask.builder()
                .userId(userId)
                .taskId(taskId)
                .build();

        int userTaskSaveResult = userTaskDaoImplementation.createUserTask(userTaskSave);

        List<UserTask> allUserTaskByUser = userTaskDaoImplementation.getAllUserTaskByUser(userId);
        Assert.assertTrue(allUserTaskByUser.size()>0);

        taskDaoApi.deleteTask(taskId);
        projectDaoApi.deleteProject(projectId);
        userDaoApi.deleteUser(userId);
        userTaskDaoImplementation.deleteUserTask(userTaskSaveResult);
    }

    @Test
    public void GetAllUsersTaskTest_ReturnTrue() throws SQLException {
        Project projectSave = new Project();
        projectSave.setName("TestProject");
        projectSave.setDescription("TestProject");

        int projectId = projectDaoApi.createProject(projectSave);

        Task taskSave = new Task();
        taskSave.setTitle("Test");
        taskSave.setDescription("Test");
        taskSave.setDeadline(new Date(new java.util.Date().getTime()));
        taskSave.setStatus("Open");

        int taskId = taskDaoApi.createTask(taskSave);

        User userSave = new User();
        userSave.setEmail("testsaveuser@mail.cas");
        userSave.setUsername("usertest");

        int userId = Math.toIntExact(userDaoApi.createUser(userSave));

        UserTask userTaskSave = UserTask.builder()
                .userId(userId)
                .taskId(taskId)
                .build();

        int userTaskSaveResult = userTaskDaoImplementation.createUserTask(userTaskSave);

        List<UserTask> allUsersTask = userTaskDaoImplementation.getAllUsersTask();
        Assert.assertTrue(allUsersTask.size()>0);

        taskDaoApi.deleteTask(taskId);
        projectDaoApi.deleteProject(projectId);
        userDaoApi.deleteUser(userId);
        userTaskDaoImplementation.deleteUserTask(userTaskSaveResult);
    }

    @Test
    public void DeleteUserTaskTest_ReturnTrue() throws SQLException {
        Project projectSave = new Project();
        projectSave.setName("TestProject");
        projectSave.setDescription("TestProject");

        int projectId = projectDaoApi.createProject(projectSave);

        Task taskSave = new Task();
        taskSave.setTitle("Test");
        taskSave.setDescription("Test");
        taskSave.setDeadline(new Date(new java.util.Date().getTime()));
        taskSave.setStatus("Open");


        int taskId = taskDaoApi.createTask(taskSave);

        User userSave = new User();
        userSave.setEmail("testsaveuser@mail.cas");
        userSave.setUsername("usertest");

        int userId = Math.toIntExact(userDaoApi.createUser(userSave));

        UserTask userTaskSave = UserTask.builder()
                .userId(userId)
                .taskId(taskId)
                .build();

        int userTaskSaveResult = userTaskDaoImplementation.createUserTask(userTaskSave);
        int deleteUserTaskResult = userTaskDaoImplementation.deleteUserTask(userTaskSaveResult);

        Assert.assertTrue(deleteUserTaskResult>0);

        taskDaoApi.deleteTask(taskId);
        projectDaoApi.deleteProject(projectId);
        userDaoApi.deleteUser(userId);
    }
}