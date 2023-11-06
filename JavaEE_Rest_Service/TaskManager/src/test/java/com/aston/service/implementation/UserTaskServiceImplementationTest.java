package com.aston.service.implementation;


import com.aston.dao.api.ProjectDaoApi;
import com.aston.dao.api.TaskDaoApi;
import com.aston.dao.api.UserDaoApi;
import com.aston.dao.implementation.ProjectDaoImplementation;
import com.aston.dao.implementation.TaskDaoImplementation;
import com.aston.dao.implementation.UserDaoImplementation;
import com.aston.dao.implementation.UserTaskDaoImplementation;
import com.aston.util.*;
import com.aston.util.dto.*;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Date;
import java.sql.SQLException;

import static org.junit.Assert.assertThrows;

public class UserTaskServiceImplementationTest {
    private static UserTaskServiceImplementation userTaskServiceImplementation;

    private static SessionFactory sessionFactory;
    private static UserDaoApi userDaoApi;
    private static TaskDaoApi taskDaoApi;
    private static ProjectDaoApi projectDaoApi;
    private static ProjectServiceImplementation projectServiceImplementation;
    private static TaskServiceImplementation taskServiceImplementation;
    private static UserServiceImplementation userServiceImplementation;

    @BeforeClass
    public static void init() {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:postgresql://localhost:5432/taskmanagertest",
                        "postgres", "postgres")
                .schemas("taskmanager")
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
        Configuration configuration = new Configuration();
        configuration.configure("hibernate-test.cfg.xml");
        sessionFactory = configuration.buildSessionFactory();
        userDaoApi = new UserDaoImplementation(sessionFactory);
        taskDaoApi = new TaskDaoImplementation(sessionFactory);
        projectDaoApi = new ProjectDaoImplementation(sessionFactory);
        userTaskServiceImplementation = new UserTaskServiceImplementation(userDaoApi,sessionFactory,taskDaoApi, new UserTaskDaoImplementation(sessionFactory));
        projectServiceImplementation = new ProjectServiceImplementation(projectDaoApi,sessionFactory);
        taskServiceImplementation = new TaskServiceImplementation(taskDaoApi,projectServiceImplementation,sessionFactory, projectDaoApi);
        userServiceImplementation = new UserServiceImplementation(userDaoApi,sessionFactory);

    }

    @AfterClass
    public static void closeSession() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void CreateUserTaskTest_WithValidUserTask_ReturnTrue() throws SQLException, UserNotFoundException, TaskNotFoundException, UserTaskAlreadyExistsException, ProjectNotFoundException, TaskInvalidParameterException, UserInvalidParameterException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectId = projectServiceImplementation.createProject(projectDtoSave);

        TaskDto taskSave = TaskDto.builder()
                .title("TestTest")
                .description("TesTestTestt")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(projectId)
                .build();

        Long taskId = taskServiceImplementation.createTask(taskSave);

        UserDto userSave = UserDto.builder()
                .email("testsaveuser@mail.cas")
                .username("usertest")
                .build();

        Long userId = userServiceImplementation.createUser(userSave);

        UserTaskSaveDto userTaskSave = UserTaskSaveDto.builder()
                .userId(userId)
                .taskId(taskId)
                .build();

        userTaskServiceImplementation.createUserTask(userTaskSave);

        Assert.assertTrue(userTaskServiceImplementation.getAllUserTaskByUser(userId).getTasks().size()>0);

        taskServiceImplementation.deleteTask(taskId);
        projectServiceImplementation.deleteProject(projectId);
        userServiceImplementation.deleteUser(userId);
        userTaskServiceImplementation.deleteUserTask(userId,taskId);
    }


    @Test
    public void CreateUserTaskTest_WithExistsTaskUserTask_ReturnTrue() throws UserInvalidParameterException, UserNotFoundException, TaskInvalidParameterException, TaskNotFoundException, ProjectInvalidParameterException, ProjectNotFoundException, UserTaskAlreadyExistsException, SQLException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();
        Long projectId = projectServiceImplementation.createProject(projectDtoSave);
        TaskDto taskSave = TaskDto.builder()
                .title("TestTest")
                .description("TesTestTestt")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(projectId)
                .build();
        Long taskId = taskServiceImplementation.createTask(taskSave);
        UserDto userSave = UserDto.builder()
                .email("testsaveuser@mail.cas")
                .username("usertest")
                .build();
        Long userId = userServiceImplementation.createUser(userSave);
        UserTaskSaveDto userTaskSave = UserTaskSaveDto.builder()
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

        Long projectId = projectServiceImplementation.createProject(projectDtoSave);

        TaskDto taskSave = TaskDto.builder()
                .title("TestTest")
                .description("TestTestTest")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(projectId)
                .build();

        Long taskId = taskServiceImplementation.createTask(taskSave);

        UserDto userSave = UserDto.builder()
                .email("testsaveuser@mail.cas")
                .username("usertest")
                .build();

        Long userId = userServiceImplementation.createUser(userSave);

        UserTaskSaveDto userTaskSave = UserTaskSaveDto.builder()
                .userId(userId)
                .taskId(taskId)
                .build();

        userTaskServiceImplementation.createUserTask(userTaskSave);

        UserTaskDto allUserTaskByUserByUserId = userTaskServiceImplementation.getAllUserTaskByUser(userId);

        Assert.assertTrue(allUserTaskByUserByUserId.getTasks().size()>0);

        taskServiceImplementation.deleteTask(taskId);
        projectServiceImplementation.deleteProject(projectId);
        userServiceImplementation.deleteUser(userId);
        userTaskServiceImplementation.deleteUserTask(userId,taskId);
    }


    @Test
    public void DeleteUserTaskTest_WithExistsUserTask_ReturnTrue() throws SQLException, UserInvalidParameterException, UserNotFoundException, TaskInvalidParameterException, TaskNotFoundException, ProjectInvalidParameterException, ProjectNotFoundException, UserTaskAlreadyExistsException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectId = projectServiceImplementation.createProject(projectDtoSave);

        TaskDto taskSave = TaskDto.builder()
                .title("TestTest")
                .description("TestTestTest")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(projectId)
                .build();

        Long taskId = taskServiceImplementation.createTask(taskSave);

        UserDto userSave = UserDto.builder()
                .email("testsaveuser@mail.cas")
                .username("usertest")
                .build();

        Long userId = userServiceImplementation.createUser(userSave);

        UserTaskSaveDto userTaskSave = UserTaskSaveDto.builder()
                .userId(userId)
                .taskId(taskId)
                .build();

        userTaskServiceImplementation.createUserTask(userTaskSave);
        userTaskServiceImplementation.deleteUserTask(userId,taskId);

        Assert.assertTrue(userTaskServiceImplementation.getAllUserTaskByUser(userId).getTasks().size()>0);

        taskServiceImplementation.deleteTask(taskId);
        projectServiceImplementation.deleteProject(projectId);
        userServiceImplementation.deleteUser(userId);

    }
}