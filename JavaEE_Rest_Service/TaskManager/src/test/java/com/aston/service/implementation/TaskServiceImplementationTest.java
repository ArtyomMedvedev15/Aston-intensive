package com.aston.service.implementation;


import com.aston.dao.api.*;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.dao.datasource.ConnectionPoolImpl;
import com.aston.dao.datasource.TransactionManagerImpl;
import com.aston.dao.implementation.ProjectDaoImplementation;
import com.aston.dao.implementation.TaskDaoImplementation;
import com.aston.dao.implementation.UserTaskDaoImplementation;
import com.aston.service.api.ProjectServiceApi;
import com.aston.util.*;
import com.aston.util.dto.ProjectDto;
import com.aston.util.dto.TaskDto;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertThrows;

public class TaskServiceImplementationTest {

    private static TaskServiceImplementation taskServiceImplementation;
    private static ConnectionPool connectionPool;
    private static ProjectServiceApi projectServiceImplementation;
    private static SessionFactory sessionFactory;
    @BeforeClass
    public static void init() throws ConnectionPoolException {
        connectionPool = ConnectionPoolImpl.getInstance();
        connectionPool.init("database");
        TransactionManager transactionManager = new TransactionManagerImpl(connectionPool);
        ConnectionManager connectionManager = new ConnectionManager(transactionManager);
        ProjectDaoApi projectDaoApi = new ProjectDaoImplementation(sessionFactory);
        TaskDaoApi taskDaoApi = new TaskDaoImplementation(sessionFactory);
        UserTaskDaoApi userTaskDaoApi = new UserTaskDaoImplementation(connectionManager);
        taskServiceImplementation = new TaskServiceImplementation(taskDaoApi,
                new ProjectServiceImplementation(projectDaoApi,
                        connectionManager,taskDaoApi,userTaskDaoApi),connectionManager);
        projectServiceImplementation = new ProjectServiceImplementation(projectDaoApi,connectionManager,taskDaoApi,userTaskDaoApi);

    }

    @AfterClass
    public static void destroy() throws ConnectionPoolException {
        connectionPool.destroy();
    }


    @Test
    public void CreateTaskTest_WithValidTask_ReturnTrue() throws SQLException, TaskInvalidParameterException, TaskNotFoundException, ProjectInvalidParameterException, ProjectNotFoundException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectId = projectServiceImplementation.createProject(projectDtoSave);

        TaskDto taskSave = TaskDto.builder()
                .title("TestTest")
                .description("TestTestTest")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open1")
                .projectId(Math.toIntExact(projectId))
                .build();

        Long taskSaveResult = taskServiceImplementation.createTask(taskSave);

        Assert.assertTrue(taskSaveResult>0);

        taskServiceImplementation.deleteTask(taskSaveResult);
        projectServiceImplementation.deleteProject(projectId);
    }

    @Test
    public void CreateTaskTest_WithTitleLess5_ThrowException(){
        TaskDto taskSave = TaskDto.builder()
                .title("Test")
                .description("Test")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(777)
                .build();

        TaskInvalidParameterException taskInvalidParameterException = assertThrows(
                TaskInvalidParameterException.class,
                () -> taskServiceImplementation.createTask(taskSave));

        Assert.assertEquals("Task parameter is invalid, try yet", taskInvalidParameterException.getMessage());

    }

    @Test
    public void CreateTaskTest_WithTitleMore256_ThrowException(){
        TaskDto taskSave = TaskDto.builder()
                .title("TesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTe\" +\n" +
                        "                        \"sttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttest\" +\n" +
                        "                        \"teTesttestteTesttestteTesttestteTesttestteTestt\" +\n" +
                        "                        \"estteTesttestteTesttestteTesttestteTesttestteTesttestteTest\" +\n" +
                        "                        \"testteTesttestteTesttestteTesttestteTesttestte")
                .description("Testtesttest")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(777)
                .build();

        TaskInvalidParameterException taskInvalidParameterException = assertThrows(
                TaskInvalidParameterException.class,
                () -> taskServiceImplementation.createTask(taskSave));

        Assert.assertEquals("Task parameter is invalid, try yet", taskInvalidParameterException.getMessage());
    }

    @Test
    public void CreateTaskTest_WithDescriptionLess10_ThrowException(){
        TaskDto taskSave = TaskDto.builder()
                .title("Test1")
                .description("Test")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(777)
                .build();

        TaskInvalidParameterException taskInvalidParameterException = assertThrows(
                TaskInvalidParameterException.class,
                () -> taskServiceImplementation.createTask(taskSave));

        Assert.assertEquals("Task parameter is invalid, try yet", taskInvalidParameterException.getMessage());
    }

    @Test
    public void CreateTaskTest_WithDescriptionMore512_ThrowException(){
        TaskDto taskSave = TaskDto.builder()
                .title("Test1")
                .description("TesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTe\" +\n" +
                        "                        \"sttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttest\" +\n" +
                        "                        \"teTesttestteTesttestteTesttestteTesttestteTestt\" +\n" +
                        "                        \"estteTesttestteTesttestteTesttestteTesttestteTesttestteTest\" +\n" +
                        "                        \"testteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTe\" +\n" +
                        "                        \"sttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttest\" +\n" +
                        "                        \"teTesttestteTesttestteTesttestteTesttestteTestt\" +\n" +
                        "                        \"estteTesttestteTesttestteTesttestteTesttestteTesttestteTest\" +\n" +
                        "                        \"testteTesttestteTesttestteTesttestteTesttestte")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(777)
                .build();

        TaskInvalidParameterException taskInvalidParameterException = assertThrows(
                TaskInvalidParameterException.class,
                () -> taskServiceImplementation.createTask(taskSave));

        Assert.assertEquals("Task parameter is invalid, try yet", taskInvalidParameterException.getMessage());
    }

    @Test
    public void GetTaskByIdTest_WithExistsTask_ReturnTrue() throws SQLException, TaskNotFoundException, TaskInvalidParameterException, ProjectInvalidParameterException, ProjectNotFoundException {
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
                .projectId(Math.toIntExact(projectId))
                .build();

        Long taskId = taskServiceImplementation.createTask(taskSave);

        TaskDto taskById = taskServiceImplementation.getTaskById(taskId);

        Assert.assertEquals("TestTest", taskById.getTitle());

        taskServiceImplementation.deleteTask(taskId);
        projectServiceImplementation.deleteProject(projectId);

    }

    @Test
    public void GetTaskByIdTest_WithNonExistsTask_ReturnTrue()  {
        TaskNotFoundException taskNotFoundException = assertThrows(
                TaskNotFoundException.class,
                () -> taskServiceImplementation.getTaskById(989898L));

        Assert.assertEquals("Task with id 989898 was not found", taskNotFoundException.getMessage());
    }

    @Test
    public void GetAllTasksTest_ReturnTrue() throws SQLException, TaskInvalidParameterException, TaskNotFoundException, ProjectInvalidParameterException, ProjectNotFoundException {
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
                .projectId(Math.toIntExact(projectId))
                .build();

        Long taskId = taskServiceImplementation.createTask(taskSave);

        List<TaskDto> allTasks = taskServiceImplementation.getAllTasks();

        Assert.assertTrue(allTasks.size()>0);

        taskServiceImplementation.deleteTask(taskId);
        projectServiceImplementation.deleteProject(projectId);
    }

    @Test
    public void GetAllTasksByProjectTest_WithExistsProject_ReturnTrue() throws SQLException, TaskInvalidParameterException, TaskNotFoundException, ProjectInvalidParameterException, ProjectNotFoundException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectId = projectServiceImplementation.createProject(projectDtoSave);

        TaskDto taskSave = TaskDto.builder()
                .title("TestTest")
                .description("TestTestTest")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open1")
                .projectId(Math.toIntExact(projectId))
                .build();

        Long taskId = taskServiceImplementation.createTask(taskSave);

        Set<TaskDto> allTasks = taskServiceImplementation.getAllTasksByProject(projectId);

        Assert.assertTrue(allTasks.size()>0);

        taskServiceImplementation.deleteTask(taskId);
        projectServiceImplementation.deleteProject(projectId);
    }

    @Test
    public void UpdateTaskTest_WithValidTask_ReturnTrue() throws SQLException, TaskInvalidParameterException, TaskNotFoundException, ProjectInvalidParameterException, ProjectNotFoundException {
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
                .projectId(Math.toIntExact(projectId))
                .build();
        Long taskId = taskServiceImplementation.createTask(taskSave);

        TaskDto taskUpdate = TaskDto.builder()
                .id((long) taskId)
                .title("Update")
                .description("UpdateUpdate")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(Math.toIntExact(projectId))
                .build();

        Long taskUpdateResult = taskServiceImplementation.updateTask(taskUpdate);

        Assert.assertTrue(taskUpdateResult>0);

        taskServiceImplementation.deleteTask(taskId);
    }

    @Test
    public void UpdateTaskTest_WithTitleLessThan5_ReturnTrue(){
        TaskDto taskSave = TaskDto.builder()
                .title("Test")
                .description("Test")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(777)
                .build();

        TaskInvalidParameterException taskInvalidParameterException = assertThrows(
                TaskInvalidParameterException.class,
                () -> taskServiceImplementation.updateTask(taskSave));

        Assert.assertEquals("Task parameter is invalid, try yet", taskInvalidParameterException.getMessage());

    }

    @Test
    public void UpdateTaskTest_WithTitleMoreThan256_ReturnTrue(){
        TaskDto taskSave = TaskDto.builder()
                .title("TesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTe\" +\n" +
                        "                        \"sttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttest\" +\n" +
                        "                        \"teTesttestteTesttestteTesttestteTesttestteTestt\" +\n" +
                        "                        \"estteTesttestteTesttestteTesttestteTesttestteTesttestteTest\" +\n" +
                        "                        \"testteTesttestteTesttestteTesttestteTesttestte")
                .description("Testtesttest")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(777)
                .build();

        TaskInvalidParameterException taskInvalidParameterException = assertThrows(
                TaskInvalidParameterException.class,
                () -> taskServiceImplementation.updateTask(taskSave));

        Assert.assertEquals("Task parameter is invalid, try yet", taskInvalidParameterException.getMessage());
    }

    @Test
    public void UpdateTaskTest_WithDescriptionLess10_ThrowException(){
        TaskDto taskSave = TaskDto.builder()
                .title("Test1")
                .description("Test")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(777)
                .build();

        TaskInvalidParameterException taskInvalidParameterException = assertThrows(
                TaskInvalidParameterException.class,
                () -> taskServiceImplementation.updateTask(taskSave));

        Assert.assertEquals("Task parameter is invalid, try yet", taskInvalidParameterException.getMessage());
    }

    @Test
    public void UpdateTaskTest_WithDescriptionMore512_ThrowException(){
        TaskDto taskSave = TaskDto.builder()
                .title("Test1")
                .description("TesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTe\" +\n" +
                        "                        \"sttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttest\" +\n" +
                        "                        \"teTesttestteTesttestteTesttestteTesttestteTestt\" +\n" +
                        "                        \"estteTesttestteTesttestteTesttestteTesttestteTesttestteTest\" +\n" +
                        "                        \"testteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTe\" +\n" +
                        "                        \"sttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttest\" +\n" +
                        "                        \"teTesttestteTesttestteTesttestteTesttestteTestt\" +\n" +
                        "                        \"estteTesttestteTesttestteTesttestteTesttestteTesttestteTest\" +\n" +
                        "                        \"testteTesttestteTesttestteTesttestteTesttestte")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(777)
                .build();

        TaskInvalidParameterException taskInvalidParameterException = assertThrows(
                TaskInvalidParameterException.class,
                () -> taskServiceImplementation.updateTask(taskSave));

        Assert.assertEquals("Task parameter is invalid, try yet", taskInvalidParameterException.getMessage());
    }


    @Test
    public void DeleteTaskTest_WithExistsTask_ReturnTrue() throws SQLException, TaskInvalidParameterException, TaskNotFoundException, ProjectInvalidParameterException, ProjectNotFoundException {
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
                .projectId(Math.toIntExact(projectId))
                .build();

        Long taskId = taskServiceImplementation.createTask(taskSave);


        Long taskDeleteResult = taskServiceImplementation.deleteTask(taskId);
        Assert.assertTrue(taskDeleteResult>0);

        projectServiceImplementation.deleteProject(projectId);
    }

    @Test
    public void DeleteTaskTest_WithNonExistsTask_ReturnTrue(){
        TaskNotFoundException taskNotFoundException = assertThrows(
                TaskNotFoundException.class,
                () -> taskServiceImplementation.deleteTask(989898L));

        Assert.assertEquals("Task with id 989898 was not found", taskNotFoundException.getMessage());
    }
}