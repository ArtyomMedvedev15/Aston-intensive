package com.aston.dao.implementation;


import com.aston.dao.api.ConnectionPool;
import com.aston.dao.api.TransactionManager;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.dao.datasource.ConnectionPoolImpl;
import com.aston.dao.datasource.TransactionManagerImpl;
import com.aston.entities.User;
import com.aston.util.ConnectionPoolException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class UserDaoImplementationTest {
    private static UserDaoImplementation userDaoImplementation;
    private static ConnectionPool connectionPool;
    @BeforeClass
    public static void init() throws ConnectionPoolException {
        connectionPool = ConnectionPoolImpl.getInstance();
        connectionPool.init("database");
        TransactionManager transactionManager = new TransactionManagerImpl(connectionPool);
        ConnectionManager connectionManager = new ConnectionManager(transactionManager);
        userDaoImplementation = new UserDaoImplementation(connectionManager);
    }

    @AfterClass
    public static void destroy() throws ConnectionPoolException {
        connectionPool.destroy();
    }

    @Test
    public void CreateNewUserTest_ReturnTrue() throws SQLException {
        User userSave = User.builder()
                .email("testsaveuser@mail.cas")
                .username("usertest")
                .build();

        int userSaveResult = userDaoImplementation.createUser(userSave);

        Assert.assertTrue(userSaveResult>0);

        userDaoImplementation.deleteUser(userSaveResult);
    }

    @Test
    public void UpdateUserTest_ReturnTrue() throws SQLException {
        User userUpdate = User.builder()
                .id(778L)
                .username("Updated")
                .email("Updated")
                .build();
        int userUpdateResult = userDaoImplementation.updateUser(userUpdate);
        Assert.assertTrue(userUpdateResult>0);
    }

    @Test
    public void DeleteUserTest_ReturnTrue() throws SQLException {
        User userSave = User.builder()
                .email("testsaveuser@mail.cas")
                .username("usertest")
                .build();

        int userForDelete = userDaoImplementation.createUser(userSave);
        int userDeleteResult = userDaoImplementation.deleteUser(userForDelete);

        Assert.assertTrue(userDeleteResult>0);
    }
    @Test
     public void GetUserByIDTest_WithId778_ReturnTrue() throws SQLException {
        User userById = userDaoImplementation.getUserById(777);
        Assert.assertEquals("TestUsr", userById.getUsername());
    }

    @Test
    public void GetUserByUsernameTest_ReturnTrue() throws SQLException {
        User userByUsername = userDaoImplementation.getUserByUsername("TestUsr");
        Assert.assertEquals("TestUsr", userByUsername.getUsername());

    }
    @Test
    public void GetAllUsersTest_ReturnTrue() throws SQLException {
        List<User> allUsersTest = userDaoImplementation.getAllUsers();
        Assert.assertTrue(allUsersTest.size()>0);
    }
}