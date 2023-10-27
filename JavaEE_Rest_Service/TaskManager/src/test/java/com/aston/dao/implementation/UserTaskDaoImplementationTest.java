package com.aston.dao.implementation;


import com.aston.dao.api.ConnectionPool;
import com.aston.dao.api.TransactionManager;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.dao.datasource.ConnectionPoolImpl;
import com.aston.dao.datasource.TransactionManagerImpl;
import com.aston.entities.UserTask;
import com.aston.util.ConnectionPoolException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class UserTaskDaoImplementationTest {
    private static UserTaskDaoImplementation userTaskDaoImplementation;
    private static ConnectionPool connectionPool;
    @BeforeClass
    public static void init() throws ConnectionPoolException {
        connectionPool = ConnectionPoolImpl.getInstance();
        connectionPool.init("database");
        TransactionManager transactionManager = new TransactionManagerImpl(connectionPool);
        ConnectionManager connectionManager = new ConnectionManager(transactionManager);
        userTaskDaoImplementation = new UserTaskDaoImplementation(connectionManager);
    }

    @AfterClass
    public static void destroy() throws ConnectionPoolException {
        connectionPool.destroy();
    }
    @Test
    public void CreateUserTaskTest_ReturnTrue() throws SQLException {
        UserTask userTaskSave = UserTask.builder()
                .userId(778)
                .taskId(778)
                .build();

        int userTaskSaveResult = userTaskDaoImplementation.createUserTask(userTaskSave);

        Assert.assertTrue(userTaskSaveResult>0);

        userTaskDaoImplementation.deleteUserTask(userTaskSaveResult);
    }

    @Test
    public void GetAllUserTaskByUserTest_WithUserId777_ReturnTrue() throws SQLException {
        List<UserTask> allUserTaskByUser = userTaskDaoImplementation.getAllUserTaskByUser(777);
        Assert.assertTrue(allUserTaskByUser.size()>0);
    }

    @Test
    public void GetAllUsersTaskTest_ReturnTrue() throws SQLException {
        List<UserTask> allUsersTask = userTaskDaoImplementation.getAllUsersTask();
        Assert.assertTrue(allUsersTask.size()>0);
    }

    @Test
    public void DeleteUserTaskTest_ReturnTrue() throws SQLException {
        UserTask userTaskSave = UserTask.builder()
                .userId(778)
                .taskId(778)
                .build();

        int userTaskSaveResult = userTaskDaoImplementation.createUserTask(userTaskSave);

        int deleteUserTaskResult = userTaskDaoImplementation.deleteUserTask(userTaskSaveResult);
        Assert.assertTrue(deleteUserTaskResult>0);
    }
}