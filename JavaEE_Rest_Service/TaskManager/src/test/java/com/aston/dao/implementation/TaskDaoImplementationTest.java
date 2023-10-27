package com.aston.dao.implementation;


import com.aston.dao.api.ConnectionPool;
import com.aston.dao.api.TransactionManager;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.dao.datasource.ConnectionPoolImpl;
import com.aston.dao.datasource.TransactionManagerImpl;
import com.aston.entities.Task;
import com.aston.util.ConnectionPoolException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class TaskDaoImplementationTest {
    private static TaskDaoImplementation taskDaoImplementation;
    private static ConnectionPool connectionPool;
    @BeforeClass
    public static void init() throws ConnectionPoolException {
        connectionPool = ConnectionPoolImpl.getInstance();
        connectionPool.init("database");
        TransactionManager transactionManager = new TransactionManagerImpl(connectionPool);
        ConnectionManager connectionManager = new ConnectionManager(transactionManager);
        taskDaoImplementation = new TaskDaoImplementation(connectionManager);
    }

    @AfterClass
    public static void destroy() throws ConnectionPoolException {
        connectionPool.destroy();
    }


    @Test
    public void CreateTaskTest_ReturnTrue() throws SQLException {
        Task taskSave = Task.builder()
                .title("Test")
                .description("Test")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(777)
                .build();

        int taskSaveResult = taskDaoImplementation.createTask(taskSave);

        Assert.assertTrue(taskSaveResult>0);

        taskDaoImplementation.deleteTask(taskSaveResult);
    }

    @Test
    public void GetTaskByIdTest_WithId779_ReturnTrue() throws SQLException {
        Task taskById = taskDaoImplementation.getTaskById(779);
        Assert.assertEquals("TestTask1", taskById.getTitle());
    }

    @Test
    public void GetAllTasksTest_ReturnTrue() throws SQLException {
        List<Task> allTasks = taskDaoImplementation.getAllTasks();
        Assert.assertTrue(allTasks.size()>0);
    }

    @Test
    public void GetAllTasksByProjectTest_WithProjectId777_ReturnTrue() throws SQLException {
        List<Task> allTasksByProject = taskDaoImplementation.getAllTasksByProject(777);
        Assert.assertTrue(allTasksByProject.size()>0);
    }

    @Test
    public void UpdateTaskTest_WithId778_ReturnTrue() throws SQLException {
        Task taskUpdate = Task.builder()
                .id(778L)
                .title("Update")
                .description("Test")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(778)
                .build();

        int taskUpdateResult = taskDaoImplementation.updateTask(taskUpdate);
        Assert.assertTrue(taskUpdateResult>0);
    }

    @Test
    public void DeleteTaskTest_ReturnTrue() throws SQLException {
        Task taskSave = Task.builder()
                .title("Test")
                .description("Test")
                .deadline(new Date(new java.util.Date().getTime()))
                .status("Open")
                .projectId(777)
                .build();

        int taskDeleteId = taskDaoImplementation.createTask(taskSave);
        int deleteTaskResult = taskDaoImplementation.deleteTask(taskDeleteId);

        Assert.assertTrue(deleteTaskResult>0);
    }
}