package com.aston.dao.implementation;


import com.aston.entities.Project;
import com.aston.entities.Task;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.*;

import java.sql.Date;
import java.util.List;


public class TaskDaoImplementationTest {
    private static TaskDaoImplementation taskDaoImplementation;
    private static SessionFactory sessionFactory;
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
        taskDaoImplementation = new TaskDaoImplementation(sessionFactory);
     }

    @After
    public void cleanup() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("DELETE FROM Task ").executeUpdate();
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
    public void CreateTaskTest_ReturnTrue(){
        Project project = new Project();
        project.setId(778L);

        Task taskSave = new Task();
        taskSave.setTitle("Test");
        taskSave.setDescription("Test");
        taskSave.setDeadline(new Date(new java.util.Date().getTime()));
        taskSave.setStatus("Open");
        taskSave.setProject(project);

        Long taskSaveResult = taskDaoImplementation.createTask(taskSave);

        Assert.assertTrue(taskSaveResult>0);

    }

    @Test
    public void GetTaskByIdTest_WithId779_ReturnTrue(){
        Project project = new Project();
        project.setId(778L);

        Task taskSave = new Task();
        taskSave.setTitle("Test");
        taskSave.setDescription("Test");
        taskSave.setDeadline(new Date(new java.util.Date().getTime()));
        taskSave.setStatus("Open");
        taskSave.setProject(project);

        Long taskSaveResult = taskDaoImplementation.createTask(taskSave);

        Task taskById = taskDaoImplementation.getTaskById(taskSaveResult);
        Assert.assertEquals("Test", taskById.getTitle());
    }

    @Test
    public void GetAllTasksTest_ReturnTrue(){
        Project project = new Project();
        project.setId(778L);

        Task taskSave = new Task();
        taskSave.setTitle("Test");
        taskSave.setDescription("Test");
        taskSave.setDeadline(new Date(new java.util.Date().getTime()));
        taskSave.setStatus("Open");
        taskSave.setProject(project);

        taskDaoImplementation.createTask(taskSave);
        List<Task> allTasks = taskDaoImplementation.getAllTasks();
        Assert.assertTrue(allTasks.size()>0);
    }

    @Test
    public void UpdateTaskTest_WithId778_ReturnTrue(){
        Project project = new Project();
        project.setId(777L);

        Task taskUpdate = new Task();

        taskUpdate.setId(778L);
        taskUpdate.setTitle("Update");
        taskUpdate.setDescription("Test");
        taskUpdate.setDeadline(new Date(new java.util.Date().getTime()));
        taskUpdate.setStatus("Open");
        taskUpdate.setProject(project);

        Long taskUpdateResult = taskDaoImplementation.updateTask(taskUpdate);
        Assert.assertTrue(taskUpdateResult>0);
    }

    @Test
    public void DeleteTaskTest_ReturnTrue(){
        Project project = new Project();
        project.setId(777L);

        Task taskSave = new Task();
        taskSave.setTitle("Test");
        taskSave.setDescription("Test");
        taskSave.setDeadline(new Date(new java.util.Date().getTime()));
        taskSave.setStatus("Open");
        taskSave.setProject(project);

        Long taskDeleteId = taskDaoImplementation.createTask(taskSave);
        Long deleteTaskResult = taskDaoImplementation.deleteTask(taskDeleteId);

        Assert.assertTrue(deleteTaskResult>0);
    }
}