package com.aston.dao.implementation;

import com.aston.entities.Project;
import com.aston.entities.Task;
import com.aston.entities.User;
import com.aston.entities.UserTask;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.*;

import java.sql.Date;
import java.util.List;


public class UserTaskDaoImplementationTest{
    private static UserTaskDaoImplementation userTaskDaoImplementation;
    private static TaskDaoImplementation taskDaoImplementation;
    private static ProjectDaoImplementation projectDaoImplementation;

    private static SessionFactory sessionFactory;
    private static UserDaoImplementation userDaoImplementation;

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
        userTaskDaoImplementation = new UserTaskDaoImplementation(sessionFactory);
        userDaoImplementation = new UserDaoImplementation(sessionFactory);
        taskDaoImplementation = new TaskDaoImplementation(sessionFactory);
        projectDaoImplementation = new ProjectDaoImplementation(sessionFactory);

    }

    @After
    public void cleanup() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("DELETE FROM UserTask").executeUpdate();
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
    public void CreateUserTaskTest_ReturnTrue() {
        User userSave = new User();
        userSave.setEmail("testsaveuser@mail.cas");
        userSave.setUsername("usertest");

        userDaoImplementation.createUser(userSave);

        Project projectSave = new Project();
        projectSave.setName("TestProject");
        projectSave.setDescription("TestProject");


        projectDaoImplementation.createProject(projectSave);

        Task taskSave = new Task();
        taskSave.setTitle("Test");
        taskSave.setDescription("Test");
        taskSave.setDeadline(new Date(new java.util.Date().getTime()));
        taskSave.setStatus("Open");
        taskSave.setProject(projectSave);

        taskDaoImplementation.createTask(taskSave);

        UserTask userTaskSave = new UserTask();
        userTaskSave.setUser(userSave);
        userTaskSave.setTask(taskSave);

        Long userTaskSaveResult = userTaskDaoImplementation.createUserTask(userTaskSave);

        Assert.assertTrue(userTaskSaveResult>0);

    }

    @Test
    public void GetAllUsersTaskByUserTest_ReturnTrue() {
        User userSave = new User();
        userSave.setEmail("testsaveuser@mail.cas");
        userSave.setUsername("usertest");

        Long userId = userDaoImplementation.createUser(userSave);

        Project projectSave = new Project();
        projectSave.setName("TestProject");
        projectSave.setDescription("TestProject");


        projectDaoImplementation.createProject(projectSave);

        Task taskSave = new Task();
        taskSave.setTitle("Test");
        taskSave.setDescription("Test");
        taskSave.setDeadline(new Date(new java.util.Date().getTime()));
        taskSave.setStatus("Open");
        taskSave.setProject(projectSave);

        taskDaoImplementation.createTask(taskSave);

        UserTask userTaskSave = new UserTask();
        userTaskSave.setUser(userSave);
        userTaskSave.setTask(taskSave);

        userTaskDaoImplementation.createUserTask(userTaskSave);

        List<UserTask> userTaskByUserID = userTaskDaoImplementation.getAllUsersTaskByUser(userId);

        Assert.assertTrue(userTaskByUserID.size()>0);


    }

    @Test
    public void DeleteUserTaskTest_ReturnTrue() {
        User userSave = new User();
        userSave.setEmail("testsaveuser@mail.cas");
        userSave.setUsername("usertest");

        Long userId = userDaoImplementation.createUser(userSave);

        Project projectSave = new Project();
        projectSave.setName("TestProject");
        projectSave.setDescription("TestProject");


        projectDaoImplementation.createProject(projectSave);

        Task taskSave = new Task();
        taskSave.setTitle("Test");
        taskSave.setDescription("Test");
        taskSave.setDeadline(new Date(new java.util.Date().getTime()));
        taskSave.setStatus("Open");
        taskSave.setProject(projectSave);

        taskDaoImplementation.createTask(taskSave);

        UserTask userTaskSave = new UserTask();
        userTaskSave.setUser(userSave);
        userTaskSave.setTask(taskSave);

        userTaskDaoImplementation.createUserTask(userTaskSave);
        userTaskDaoImplementation.deleteUserTask(userTaskSave);
        List<UserTask> userTaskList = userTaskDaoImplementation.getAllUsersTaskByUser(userId);

        Assert.assertEquals(0, userTaskList.size());


    }

    @Test
    public void testGetUserTaskByUserAndTask() {
        User userSave = new User();
        userSave.setEmail("testsaveuser@mail.cas");
        userSave.setUsername("usertest");

        userDaoImplementation.createUser(userSave);

        Project projectSave = new Project();
        projectSave.setName("TestProject");
        projectSave.setDescription("TestProject");


        projectDaoImplementation.createProject(projectSave);

        Task taskSave = new Task();
        taskSave.setTitle("Test");
        taskSave.setDescription("Test");
        taskSave.setDeadline(new Date(new java.util.Date().getTime()));
        taskSave.setStatus("Open");
        taskSave.setProject(projectSave);

        taskDaoImplementation.createTask(taskSave);

        UserTask userTaskSave = new UserTask();
        userTaskSave.setUser(userSave);
        userTaskSave.setTask(taskSave);

        userTaskDaoImplementation.createUserTask(userTaskSave);

        Assert.assertNotNull(userTaskDaoImplementation.getUserTaskByUserAndTask(userSave,taskSave));
    }
}