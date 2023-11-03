package com.aston.dao.implementation;


import com.aston.entities.User;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.*;

import java.sql.SQLException;
import java.util.List;

public class UserDaoImplementationTest {
    private static UserDaoImplementation userDaoImplementation;
    private static SessionFactory sessionFactory;
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
        userDaoImplementation = new UserDaoImplementation(sessionFactory);
     }

    @After
    public void cleanup() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("DELETE FROM User").executeUpdate();
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
    public void CreateNewUserTest_ReturnTrue() throws SQLException {
        User userSave = new User();
                userSave.setEmail("testsaveuser@mail.cas");
                userSave.setUsername("usertest");

        Long userSaveResult = userDaoImplementation.createUser(userSave);

        Assert.assertTrue(userSaveResult>0);

        userDaoImplementation.deleteUser(userSaveResult);
    }

    @Test
    public void UpdateUserTest_ReturnTrue() throws SQLException {
        User userSave = new User();
        userSave.setEmail("testsaveuser@mail.cas");
        userSave.setUsername("usertest");

        Long userSaveResult = userDaoImplementation.createUser(userSave);

        User userUpdate = new User();
        userUpdate.setId(userSaveResult);
        userUpdate.setEmail("Updated@mail.cas");
        userUpdate.setUsername("Updated");
        Long userUpdateResult = userDaoImplementation.updateUser(userUpdate);
        Assert.assertTrue(userUpdateResult>0);
        userDaoImplementation.deleteUser( userSaveResult);

    }

    @Test
    public void DeleteUserTest_ReturnTrue(){
        User userSave = new User();
        userSave.setEmail("testsaveuser@mail.cas");
        userSave.setUsername("usertest");

        Long userForDelete = userDaoImplementation.createUser(userSave);
        Long userDeleteResult = userDaoImplementation.deleteUser(userForDelete);

        Assert.assertTrue(userDeleteResult>0);
    }
    @Test
     public void GetUserByIDTest_WithId778_ReturnTrue() throws SQLException {
        User userSave = new User();
        userSave.setEmail("testsaveuser@mail.cas");
        userSave.setUsername("usertest");

        Long userSaveResult = userDaoImplementation.createUser(userSave);

        User userById = userDaoImplementation.getUserById(userSaveResult);
        Assert.assertEquals("usertest", userById.getUsername());
        userDaoImplementation.deleteUser(userSaveResult);

    }

    @Test
    public void GetUserByUsernameTest_ReturnTrue() {
        User userSave = new User();
        userSave.setEmail("testsaveuser@mail.cas");
        userSave.setUsername("usertest");
        Long userSaveResult = userDaoImplementation.createUser(userSave);

        User userByUsername = userDaoImplementation.getUserByUsername("usertest");
        Assert.assertEquals("usertest", userByUsername.getUsername());
        userDaoImplementation.deleteUser(userSaveResult);

    }
    @Test
    public void GetAllUsersTest_ReturnTrue(){
        User userSave = new User();
        userSave.setEmail("testsaveuser@mail.cas");
        userSave.setUsername("usertest");
        userDaoImplementation.createUser(userSave);
        List<User> allUsersTest = userDaoImplementation.getAllUsers();
        Assert.assertTrue(allUsersTest.size()>0);
    }
}