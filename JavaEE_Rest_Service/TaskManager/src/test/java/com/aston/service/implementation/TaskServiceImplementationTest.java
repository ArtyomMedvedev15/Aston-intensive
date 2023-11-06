package com.aston.service.implementation;


import com.aston.dao.api.*;
import com.aston.dao.implementation.ProjectDaoImplementation;
import com.aston.dao.implementation.TaskDaoImplementation;
import com.aston.service.api.ProjectServiceApi;
import com.aston.util.*;
import com.aston.util.dto.ProjectDto;
import com.aston.util.dto.TaskDto;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertThrows;

public class TaskServiceImplementationTest {

    private static TaskServiceImplementation taskServiceImplementation;
    private static ProjectServiceApi projectServiceImplementation;
    private static SessionFactory sessionFactory;
    private static TaskDaoApi taskDaoApi;
    private static ProjectDaoApi projectDaoApi;
    @BeforeClass
    public static void init(){
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
        taskDaoApi = new TaskDaoImplementation(sessionFactory);
        projectDaoApi = new ProjectDaoImplementation(sessionFactory);
        projectServiceImplementation = new ProjectServiceImplementation(projectDaoApi,sessionFactory);
        taskServiceImplementation = new TaskServiceImplementation(taskDaoApi,projectServiceImplementation,sessionFactory, projectDaoApi);

    }

    @After
    public void cleanup() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("DELETE FROM Task").executeUpdate();
        session.createQuery("DELETE FROM Project ").executeUpdate();
        tx.commit();
        session.close();
    }

    @AfterClass
    public static void closeSession() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }


    @Test
    public void CreateTaskTest_WithValidTask_ReturnTrue() throws SQLException, ProjectNotFoundException, ProjectInvalidParameterException, TaskInvalidParameterException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveId = projectServiceImplementation.createProject(projectDtoSave);

        ProjectDto projectId = projectServiceImplementation.getProjectById(projectSaveId);

        TaskDto taskSave = TaskDto.builder()
                .title("TestTest")
                .description("TestTestTest")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open1")
                .projectId(projectSaveId)
                .project(projectId)
                .build();

        Long taskSaveResult = taskServiceImplementation.createTask(taskSave);

        Assert.assertTrue(taskSaveResult>0);
    }

    @Test
    public void CreateTaskTest_WithTitleLess5_ThrowException() throws ProjectInvalidParameterException, SQLException, ProjectNotFoundException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveId = projectServiceImplementation.createProject(projectDtoSave);

        ProjectDto projectId = projectServiceImplementation.getProjectById(projectSaveId);

        TaskDto taskSave = TaskDto.builder()
                .title("Test")
                .description("Test")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(projectSaveId)
                .project(projectId)
                .build();

        TaskInvalidParameterException taskInvalidParameterException = assertThrows(
                TaskInvalidParameterException.class,
                () -> taskServiceImplementation.createTask(taskSave));

        Assert.assertEquals("Task parameter is invalid, try yet", taskInvalidParameterException.getMessage());

    }

    @Test
    public void CreateTaskTest_WithTitleMore256_ThrowException() throws ProjectInvalidParameterException, SQLException, ProjectNotFoundException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveId = projectServiceImplementation.createProject(projectDtoSave);

        ProjectDto projectId = projectServiceImplementation.getProjectById(projectSaveId);

        TaskDto taskSave = TaskDto.builder()
                .title("TesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTe\" +\n" +
                        "                        \"sttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttest\" +\n" +
                        "                        \"teTesttestteTesttestteTesttestteTesttestteTestt\" +\n" +
                        "                        \"estteTesttestteTesttestteTesttestteTesttestteTesttestteTest\" +\n" +
                        "                        \"testteTesttestteTesttestteTesttestteTesttestte")
                .description("Testtesttest")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(projectSaveId)
                .project(projectId)
                .build();

        TaskInvalidParameterException taskInvalidParameterException = assertThrows(
                TaskInvalidParameterException.class,
                () -> taskServiceImplementation.createTask(taskSave));

        Assert.assertEquals("Task parameter is invalid, try yet", taskInvalidParameterException.getMessage());
    }

    @Test
    public void CreateTaskTest_WithDescriptionLess10_ThrowException() throws ProjectInvalidParameterException, SQLException, ProjectNotFoundException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveId = projectServiceImplementation.createProject(projectDtoSave);

        ProjectDto projectId = projectServiceImplementation.getProjectById(projectSaveId);

        TaskDto taskSave = TaskDto.builder()
                .title("Test1")
                .description("Test")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(projectSaveId)
                .project(projectId)
                .build();

        TaskInvalidParameterException taskInvalidParameterException = assertThrows(
                TaskInvalidParameterException.class,
                () -> taskServiceImplementation.createTask(taskSave));

        Assert.assertEquals("Task parameter is invalid, try yet", taskInvalidParameterException.getMessage());
    }

    @Test
    public void CreateTaskTest_WithDescriptionMore512_ThrowException() throws ProjectInvalidParameterException, SQLException, ProjectNotFoundException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveId = projectServiceImplementation.createProject(projectDtoSave);

        ProjectDto projectId = projectServiceImplementation.getProjectById(projectSaveId);

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
                .projectId(projectSaveId)
                .project(projectId)
                .build();

        TaskInvalidParameterException taskInvalidParameterException = assertThrows(
                TaskInvalidParameterException.class,
                () -> taskServiceImplementation.createTask(taskSave));

        Assert.assertEquals("Task parameter is invalid, try yet", taskInvalidParameterException.getMessage());
    }

    @Test
    public void GetTaskByIdTest_WithExistsTask_ReturnTrue() throws SQLException, ProjectInvalidParameterException, ProjectNotFoundException, TaskInvalidParameterException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveId = projectServiceImplementation.createProject(projectDtoSave);

        ProjectDto projectId = projectServiceImplementation.getProjectById(projectSaveId);

        TaskDto taskSave = TaskDto.builder()
                .title("TestTest")
                .description("TestTestTest")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(projectSaveId)
                .project(projectId)
                .build();

        Long taskId = taskServiceImplementation.createTask(taskSave);

        TaskDto taskById = taskServiceImplementation.getTaskById(taskId);

        Assert.assertEquals("TestTest", taskById.getTitle());
    }

    @Test
    public void GetTaskByIdTest_WithNonExistsTask_ReturnTrue()  {
        TransactionException taskNotFoundException = assertThrows(
                TransactionException.class,
                () -> taskServiceImplementation.getTaskById(989898L));

        Assert.assertEquals("Error with with database with message Task with id 989898 was not found", taskNotFoundException.getMessage());
    }

    @Test
    public void GetAllTasksTest_ReturnTrue() throws SQLException, ProjectInvalidParameterException, ProjectNotFoundException, TaskInvalidParameterException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveId = projectServiceImplementation.createProject(projectDtoSave);

        ProjectDto projectId = projectServiceImplementation.getProjectById(projectSaveId);

        TaskDto taskSave = TaskDto.builder()
                .title("TestTest")
                .description("TestTestTest")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(projectSaveId)
                .project(projectId)
                .build();

        taskServiceImplementation.createTask(taskSave);

        List<TaskDto> allTasks = taskServiceImplementation.getAllTasks();

        Assert.assertTrue(allTasks.size()>0);
    }

    @Test
    public void UpdateTaskTest_WithValidTask_ReturnTrue() throws SQLException, ProjectInvalidParameterException, ProjectNotFoundException, TaskInvalidParameterException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveId = projectServiceImplementation.createProject(projectDtoSave);

        ProjectDto projectId = projectServiceImplementation.getProjectById(projectSaveId);

        TaskDto taskSave = TaskDto.builder()
                .title("TestTest")
                .description("TestTestTest")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(projectSaveId)
                .project(projectId)
                .build();
        Long taskId = taskServiceImplementation.createTask(taskSave);

        TaskDto taskUpdate = TaskDto.builder()
                .id(taskId)
                .title("Update")
                .description("UpdateUpdate")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(projectSaveId)
                .project(projectId)
                .build();

        Long taskUpdateResult = taskServiceImplementation.updateTask(taskUpdate);

        Assert.assertTrue(taskUpdateResult>0);
     }

    @Test
    public void UpdateTaskTest_WithTitleLessThan5_ReturnTrue() throws ProjectInvalidParameterException, SQLException, ProjectNotFoundException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveId = projectServiceImplementation.createProject(projectDtoSave);

        ProjectDto projectId = projectServiceImplementation.getProjectById(projectSaveId);

        TaskDto taskSave = TaskDto.builder()
                .title("Test")
                .description("Test")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(projectSaveId)
                .project(projectId)
                .build();

        TaskInvalidParameterException taskInvalidParameterException = assertThrows(
                TaskInvalidParameterException.class,
                () -> taskServiceImplementation.updateTask(taskSave));

        Assert.assertEquals("Task parameter is invalid, try yet", taskInvalidParameterException.getMessage());

    }

    @Test
    public void UpdateTaskTest_WithTitleMoreThan256_ReturnTrue() throws ProjectInvalidParameterException, SQLException, ProjectNotFoundException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveId = projectServiceImplementation.createProject(projectDtoSave);

        ProjectDto projectId = projectServiceImplementation.getProjectById(projectSaveId);

        TaskDto taskSave = TaskDto.builder()
                .title("TesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTe\" +\n" +
                        "                        \"sttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttestteTesttest\" +\n" +
                        "                        \"teTesttestteTesttestteTesttestteTesttestteTestt\" +\n" +
                        "                        \"estteTesttestteTesttestteTesttestteTesttestteTesttestteTest\" +\n" +
                        "                        \"testteTesttestteTesttestteTesttestteTesttestte")
                .description("Testtesttest")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(projectSaveId)
                .project(projectId)
                .build();

        TaskInvalidParameterException taskInvalidParameterException = assertThrows(
                TaskInvalidParameterException.class,
                () -> taskServiceImplementation.updateTask(taskSave));

        Assert.assertEquals("Task parameter is invalid, try yet", taskInvalidParameterException.getMessage());
    }

    @Test
    public void UpdateTaskTest_WithDescriptionLess10_ThrowException() throws ProjectInvalidParameterException, SQLException, ProjectNotFoundException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveId = projectServiceImplementation.createProject(projectDtoSave);

        ProjectDto projectId = projectServiceImplementation.getProjectById(projectSaveId);

        TaskDto taskSave = TaskDto.builder()
                .title("Test1")
                .description("Test")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(projectSaveId)
                .project(projectId)
                .build();

        TaskInvalidParameterException taskInvalidParameterException = assertThrows(
                TaskInvalidParameterException.class,
                () -> taskServiceImplementation.updateTask(taskSave));

        Assert.assertEquals("Task parameter is invalid, try yet", taskInvalidParameterException.getMessage());
    }

    @Test
    public void UpdateTaskTest_WithDescriptionMore512_ThrowException() throws ProjectInvalidParameterException, SQLException, ProjectNotFoundException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveId = projectServiceImplementation.createProject(projectDtoSave);

        ProjectDto projectId = projectServiceImplementation.getProjectById(projectSaveId);

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
                .projectId(projectSaveId)
                .project(projectId)
                .build();

        TaskInvalidParameterException taskInvalidParameterException = assertThrows(
                TaskInvalidParameterException.class,
                () -> taskServiceImplementation.updateTask(taskSave));

        Assert.assertEquals("Task parameter is invalid, try yet", taskInvalidParameterException.getMessage());
    }


    @Test
    public void DeleteTaskTest_WithExistsTask_ReturnTrue() throws SQLException, ProjectInvalidParameterException, ProjectNotFoundException, TaskInvalidParameterException, TaskNotFoundException {
        ProjectDto projectDtoSave = ProjectDto.builder()
                .name("TestProject")
                .description("TestProject")
                .build();

        Long projectSaveId = projectServiceImplementation.createProject(projectDtoSave);

        ProjectDto projectId = projectServiceImplementation.getProjectById(projectSaveId);

        TaskDto taskSave = TaskDto.builder()
                .title("TestTest")
                .description("TestTestTest")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(projectSaveId)
                .project(projectId)
                .build();

        Long taskId = taskServiceImplementation.createTask(taskSave);


        Long taskDeleteResult = taskServiceImplementation.deleteTask(taskId);
        Assert.assertTrue(taskDeleteResult>0);

     }

    @Test
    public void DeleteTaskTest_WithNonExistsTask_ReturnTrue(){
        TaskNotFoundException taskNotFoundException = assertThrows(
                TaskNotFoundException.class,
                () -> taskServiceImplementation.deleteTask(989898L));

        Assert.assertEquals("Task with id 989898 was not found", taskNotFoundException.getMessage());
    }
}