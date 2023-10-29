package com.aston.service.implementation;


import com.aston.dao.api.*;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.dao.datasource.ConnectionPoolImpl;
import com.aston.dao.datasource.TransactionManagerImpl;
import com.aston.dao.implementation.ProjectDaoImplementation;
import com.aston.dao.implementation.TaskDaoImplementation;
import com.aston.dao.implementation.UserDaoImplementation;
import com.aston.dao.implementation.UserTaskDaoImplementation;
import com.aston.util.*;
import com.aston.util.dto.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertThrows;

public class UserTaskServiceImplementationTest {
    private static UserTaskServiceImplementation userTaskServiceImplementation;
    private static UserServiceImplementation userServiceImplementation;
    private static ProjectServiceImplementation projectServiceImplementation;

    private static TaskServiceImplementation taskServiceImplementation;
    private static ConnectionPool connectionPool;
    @BeforeClass
    public static void init() throws ConnectionPoolException {
        connectionPool = ConnectionPoolImpl.getInstance();
        connectionPool.init("database");
        TransactionManager transactionManager = new TransactionManagerImpl(connectionPool);
        ConnectionManager connectionManager = new ConnectionManager(transactionManager);
        UserDaoApi userDaoApi = new UserDaoImplementation(connectionManager);
        ProjectDaoApi projectDaoApi = new ProjectDaoImplementation(connectionManager);
        TaskDaoApi taskDaoApi = new TaskDaoImplementation(connectionManager);
        UserTaskDaoApi userTaskDaoApi = new UserTaskDaoImplementation(connectionManager);
        taskServiceImplementation = new TaskServiceImplementation(taskDaoApi,
                new ProjectServiceImplementation(projectDaoApi,
                        connectionManager,taskDaoApi,userTaskDaoApi),connectionManager);
        userServiceImplementation = new UserServiceImplementation(userDaoApi,connectionManager);
        userTaskServiceImplementation = new UserTaskServiceImplementation(userTaskDaoApi,userServiceImplementation,
                taskServiceImplementation,connectionManager);
        projectServiceImplementation = new ProjectServiceImplementation(projectDaoApi,connectionManager,taskDaoApi,userTaskDaoApi);

    }

    @AfterClass
    public static void destroy() throws ConnectionPoolException {
        connectionPool.destroy();
    }

    @Test
    public void CreateUserTaskTest_WithValidUserTask_ReturnTrue() throws SQLException, UserInvalidParameterException, UserNotFoundException, TaskInvalidParameterException, TaskNotFoundException, ProjectInvalidParameterException, ProjectNotFoundException, UserTaskAlreadyExistsException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        int projectId = projectServiceImplementation.createProject(projectDtoSave);

        TaskDto taskSave = TaskDto.builder()
                .title("TestTest")
                .description("TesTestTestt")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(projectId)
                .build();

        int taskId = taskServiceImplementation.createTask(taskSave);

        UserDto userSave = UserDto.builder()
                .email("testsaveuser@mail.cas")
                .username("usertest")
                .build();

        int userId = userServiceImplementation.createUser(userSave);

        UserTaskDto userTaskSave = UserTaskDto.builder()
                .userId(userId)
                .taskId(taskId)
                .build();

        int userTaskSaveResult = userTaskServiceImplementation.createUserTask(userTaskSave);

        Assert.assertTrue(userTaskSaveResult>0);

        taskServiceImplementation.deleteTask(taskId);
        projectServiceImplementation.deleteProject(projectId);
        userServiceImplementation.deleteUser(userId);
        userTaskServiceImplementation.deleteUserTask(userTaskSaveResult);
    }


    @Test
    public void CreateUserTaskTest_WithExistsTaskUserTask_ReturnTrue() throws SQLException, UserInvalidParameterException, UserNotFoundException, TaskInvalidParameterException, TaskNotFoundException, ProjectInvalidParameterException, ProjectNotFoundException, UserTaskAlreadyExistsException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();
        int projectId = projectServiceImplementation.createProject(projectDtoSave);
        TaskDto taskSave = TaskDto.builder()
                .title("TestTest")
                .description("TesTestTestt")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(projectId)
                .build();
        int taskId = taskServiceImplementation.createTask(taskSave);
        UserDto userSave = UserDto.builder()
                .email("testsaveuser@mail.cas")
                .username("usertest")
                .build();
        int userId = userServiceImplementation.createUser(userSave);
        UserTaskDto userTaskSave = UserTaskDto.builder()
                .userId(userId)
                .taskId(taskId)
                .build();
        userTaskServiceImplementation.createUserTask(userTaskSave);
        UserTaskAlreadyExistsException userTaskAlreadyExistsException = assertThrows(
                UserTaskAlreadyExistsException.class,
                () -> userTaskServiceImplementation.createUserTask(userTaskSave));

        Assert.assertEquals(String.format("Task with id %s already add to user with id %s",taskId,userId),userTaskAlreadyExistsException.getMessage());

        taskServiceImplementation.deleteTask(taskId);
        projectServiceImplementation.deleteProject(projectId);
        userServiceImplementation.deleteUser(userId);
    }


    @Test
    public void GetAllUserTaskByUserTest_WithExistsUser_ReturnTrue() throws SQLException, UserInvalidParameterException, UserNotFoundException, TaskInvalidParameterException, TaskNotFoundException, ProjectInvalidParameterException, ProjectNotFoundException, UserTaskAlreadyExistsException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        int projectId = projectServiceImplementation.createProject(projectDtoSave);

        TaskDto taskSave = TaskDto.builder()
                .title("TestTest")
                .description("TestTestTest")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(projectId)
                .build();

        int taskId = taskServiceImplementation.createTask(taskSave);

        UserDto userSave = UserDto.builder()
                .email("testsaveuser@mail.cas")
                .username("usertest")
                .build();

        int userId = userServiceImplementation.createUser(userSave);

        UserTaskDto userTaskSave = UserTaskDto.builder()
                .userId(userId)
                .taskId(taskId)
                .build();

        int userTaskId = userTaskServiceImplementation.createUserTask(userTaskSave);

        List<UserTaskFullDto> allUserTaskByUserByUserId = userTaskServiceImplementation.getAllUserTaskByUser(userId);

        Assert.assertTrue(allUserTaskByUserByUserId.size()>0);

        taskServiceImplementation.deleteTask(taskId);
        projectServiceImplementation.deleteProject(projectId);
        userServiceImplementation.deleteUser(userId);
        userTaskServiceImplementation.deleteUserTask(userTaskId);
    }

    @Test
    public void GetAllUsersTaskTest_ReturnTrue() throws UserInvalidParameterException, SQLException, UserNotFoundException, TaskInvalidParameterException, TaskNotFoundException, ProjectInvalidParameterException, ProjectNotFoundException, UserTaskAlreadyExistsException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        int projectId = projectServiceImplementation.createProject(projectDtoSave);

        TaskDto taskSave = TaskDto.builder()
                .title("TestTest")
                .description("TestTestTest")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(projectId)
                .build();

        int taskId = taskServiceImplementation.createTask(taskSave);

        UserDto userSave = UserDto.builder()
                .email("testsaveuser@mail.cas")
                .username("usertest")
                .build();

        int userId = userServiceImplementation.createUser(userSave);

        UserTaskDto userTaskSave = UserTaskDto.builder()
                .userId(userId)
                .taskId(taskId)
                .build();

        int userTaskId = userTaskServiceImplementation.createUserTask(userTaskSave);

        List<UserTaskFullDto> allUserTaskByUserByUserId = userTaskServiceImplementation.getAllUsersTask();

        Assert.assertTrue(allUserTaskByUserByUserId.size()>0);

        taskServiceImplementation.deleteTask(taskId);
        projectServiceImplementation.deleteProject(projectId);
        userServiceImplementation.deleteUser(userId);
        userTaskServiceImplementation.deleteUserTask(userTaskId);
    }

    @Test
    public void DeleteUserTaskTest_WithExistsUserTask_ReturnTrue() throws SQLException, UserInvalidParameterException, UserNotFoundException, TaskInvalidParameterException, TaskNotFoundException, ProjectInvalidParameterException, ProjectNotFoundException, UserTaskAlreadyExistsException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        int projectId = projectServiceImplementation.createProject(projectDtoSave);

        TaskDto taskSave = TaskDto.builder()
                .title("TestTest")
                .description("TestTestTest")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(projectId)
                .build();

        int taskId = taskServiceImplementation.createTask(taskSave);

        UserDto userSave = UserDto.builder()
                .email("testsaveuser@mail.cas")
                .username("usertest")
                .build();

        int userId = userServiceImplementation.createUser(userSave);

        UserTaskDto userTaskSave = UserTaskDto.builder()
                .userId(userId)
                .taskId(taskId)
                .build();

        int userTaskId = userTaskServiceImplementation.createUserTask(userTaskSave);
        int userDeleteResult = userTaskServiceImplementation.deleteUserTask(userTaskId);

        Assert.assertTrue(userDeleteResult>0);

        taskServiceImplementation.deleteTask(taskId);
        projectServiceImplementation.deleteProject(projectId);
        userServiceImplementation.deleteUser(userId);

    }
}