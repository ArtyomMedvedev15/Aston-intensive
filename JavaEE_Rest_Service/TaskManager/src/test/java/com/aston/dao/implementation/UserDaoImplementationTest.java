package com.aston.dao.implementation;


import com.aston.dao.api.ConnectionPool;
import com.aston.dao.api.TransactionManager;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.dao.datasource.ConnectionPoolImpl;
import com.aston.dao.datasource.TransactionManagerImpl;
import com.aston.entities.User;
import com.aston.util.ConnectionPoolException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class UserDaoImplementationTest {
    private static UserDaoImplementation userDaoImplementation;
    private static ConnectionPool connectionPool;
    private static SessionFactory sessionFactory;
    @BeforeClass
    public static void init() throws ConnectionPoolException {
        connectionPool = ConnectionPoolImpl.getInstance();
        connectionPool.init("database");
        TransactionManager transactionManager = new TransactionManagerImpl(connectionPool);
        ConnectionManager connectionManager = new ConnectionManager(transactionManager);
        userDaoImplementation = new UserDaoImplementation(connectionManager, sessionFactory);
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    @AfterClass
    public static void destroy() throws ConnectionPoolException {
        connectionPool.destroy();
    }

    @Test
    public void CreateNewUserTest_ReturnTrue() throws SQLException {
        User userSave = new User();
                userSave.setEmail("testsaveuser@mail.cas");
                userSave.setUsername("usertest");

        Long userSaveResult = userDaoImplementation.createUser(userSave);

        Assert.assertTrue(userSaveResult>0);

        userDaoImplementation.deleteUser(Math.toIntExact(userSaveResult));
    }

    @Test
    public void UpdateUserTest_ReturnTrue() throws SQLException {
        User userSave = new User();
        userSave.setEmail("testsaveuser@mail.cas");
        userSave.setUsername("usertest");

        Long userSaveResult = userDaoImplementation.createUser(userSave);

        User userUpdate = new User();
        userUpdate.setId((long) userSaveResult);
        userUpdate.setEmail("Updated@mail.cas");
        userUpdate.setUsername("Updated");
        int userUpdateResult = userDaoImplementation.updateUser(userUpdate);
        Assert.assertTrue(userUpdateResult>0);
        userDaoImplementation.deleteUser(Math.toIntExact(userSaveResult));

    }

    @Test
    public void DeleteUserTest_ReturnTrue() throws SQLException {
        User userSave = new User();
        userSave.setEmail("testsaveuser@mail.cas");
        userSave.setUsername("usertest");

        Long userForDelete = userDaoImplementation.createUser(userSave);
        int userDeleteResult = userDaoImplementation.deleteUser(Math.toIntExact(userForDelete));

        Assert.assertTrue(userDeleteResult>0);
    }
    @Test
     public void GetUserByIDTest_WithId778_ReturnTrue() throws SQLException {
        User userSave = new User();
        userSave.setEmail("testsaveuser@mail.cas");
        userSave.setUsername("usertest");

        Long userSaveResult = userDaoImplementation.createUser(userSave);

        User userById = userDaoImplementation.getUserById(Math.toIntExact(userSaveResult));
        Assert.assertEquals("usertest", userById.getUsername());
        userDaoImplementation.deleteUser(Math.toIntExact(userSaveResult));

    }

    @Test
    public void GetUserByUsernameTest_ReturnTrue() throws SQLException {
        User userSave = new User();
        userSave.setEmail("testsaveuser@mail.cas");
        userSave.setUsername("usertest");
        Long userSaveResult = userDaoImplementation.createUser(userSave);

        User userByUsername = userDaoImplementation.getUserByUsername("usertest");
        Assert.assertEquals("usertest", userByUsername.getUsername());
        userDaoImplementation.deleteUser(Math.toIntExact(userSaveResult));

    }
    @Test
    public void GetAllUsersTest_ReturnTrue() throws SQLException {
        List<User> allUsersTest = userDaoImplementation.getAllUsers();
        Assert.assertTrue(allUsersTest.size()>0);
    }
}