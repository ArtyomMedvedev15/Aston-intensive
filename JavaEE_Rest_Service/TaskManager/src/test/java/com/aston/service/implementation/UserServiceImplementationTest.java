package com.aston.service.implementation;


import com.aston.dao.implementation.UserDaoImplementation;
import com.aston.util.TransactionException;
import com.aston.util.UserInvalidParameterException;
import com.aston.util.UserNotFoundException;
import com.aston.util.dto.UserDto;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class UserServiceImplementationTest {
    private static UserServiceImplementation userServiceImplementation;
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
        userServiceImplementation = new UserServiceImplementation(userDaoImplementation,sessionFactory);
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
    public void CreateUserTest_WithValidUser_ReturnTrue() throws UserInvalidParameterException {
        UserDto userSave = UserDto.builder()
                .email("testsaveuser@mail.cas")
                .username("usertest")
                .build();
        Long userSaveResult = userServiceImplementation.createUser(userSave);
        Assert.assertTrue(userSaveResult>0);
    }

    @Test
    public void CreateUserTest_WithUsernameLess5_ReturnTrue(){
        UserDto userSave = UserDto.builder()
                .email("testsaveuser@mail.cas")
                .username("user")
                .build();

        UserInvalidParameterException userInvalidParameterException = assertThrows(
                UserInvalidParameterException.class,
                () -> userServiceImplementation.createUser(userSave));

        Assert.assertEquals("Username or email invalid, try yet", userInvalidParameterException.getMessage());
    }

    @Test
    public void CreateUserTest_WithUsernameMoreThan256_ReturnTrue(){
        UserDto userSave = UserDto.builder()
                .email("testsaveuser@mail.cas")
                .username("useruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserus" +
                        "useruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserus" +
                        "useruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserus")
                .build();

        UserInvalidParameterException userInvalidParameterException = assertThrows(
                UserInvalidParameterException.class,
                () -> userServiceImplementation.createUser(userSave));

        Assert.assertEquals("Username or email invalid, try yet", userInvalidParameterException.getMessage());
    }

    @Test
    public void CreateUserTest_WithEmailLess8_ReturnTrue(){
        UserDto userSave = UserDto.builder()
                .email("emai")
                .username("username")
                .build();

        UserInvalidParameterException userInvalidParameterException = assertThrows(
                UserInvalidParameterException.class,
                () -> userServiceImplementation.createUser(userSave));

        Assert.assertEquals("Username or email invalid, try yet", userInvalidParameterException.getMessage());
    }

    @Test
    public void CreateUserTest_WithEmailMoreThan256_ReturnTrue(){
        UserDto userSave = UserDto.builder()
                .email("\"useruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserus\" +\n" +
                        "                        \"useruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserus\" +\n" +
                        "                        \"useruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserus")
                .username("username")
                .build();

        UserInvalidParameterException userInvalidParameterException = assertThrows(
                UserInvalidParameterException.class,
                () -> userServiceImplementation.createUser(userSave));

        Assert.assertEquals("Username or email invalid, try yet", userInvalidParameterException.getMessage());
    }

    @Test
    public void UpdateUserTest_WithValid_ReturnTrue() throws UserInvalidParameterException, SQLException, UserNotFoundException {
        UserDto userSave = UserDto.builder()
                .email("testsaveuser@mail.cas")
                .username("usertest")
                .build();
        Long userSaveResult = userServiceImplementation.createUser(userSave);

        UserDto userUpdate = UserDto.builder()
                .id((long) userSaveResult)
                .email("testsaveuser@mail.cas")
                .username("usertest")
                .build();
        Long userUpdateResult = userServiceImplementation.updateUser(userUpdate);
        Assert.assertTrue(userUpdateResult>0);
    }

    @Test
    public void UpdateUserTest_WithUsernameLess5_ReturnTrue(){
        UserDto userUpdate = UserDto.builder()
                .id(777L)
                .email("testsaveuser@mail.cas")
                .username("user")
                .build();

        UserInvalidParameterException userInvalidParameterException = assertThrows(
                UserInvalidParameterException.class,
                () -> userServiceImplementation.updateUser(userUpdate));

        Assert.assertEquals("Username or email invalid, try yet", userInvalidParameterException.getMessage());
    }

    @Test
    public void UpdateUserTest_WithUsernameMoreThan256_ReturnTrue(){
        UserDto userUpdate = UserDto.builder()
                .id(777L)
                .email("testsaveuser@mail.cas")
                .username("useruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserus" +
                        "useruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserus" +
                        "useruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserus")
                .build();

        UserInvalidParameterException userInvalidParameterException = assertThrows(
                UserInvalidParameterException.class,
                () -> userServiceImplementation.updateUser(userUpdate));

        Assert.assertEquals("Username or email invalid, try yet", userInvalidParameterException.getMessage());
    }

    @Test
    public void UpdateUserTest_WithEmailLess8_ReturnTrue(){
        UserDto userUpdate = UserDto.builder()
                .id(777L)
                .email("emai")
                .username("username")
                .build();
        UserInvalidParameterException userInvalidParameterException = assertThrows(
                UserInvalidParameterException.class,
                () -> userServiceImplementation.updateUser(userUpdate));
        Assert.assertEquals("Username or email invalid, try yet", userInvalidParameterException.getMessage());
    }

    @Test
    public void UpdateUserTest_WithEmailMoreThan256_ReturnTrue(){
        UserDto userUpdate = UserDto.builder()
                .id(777L)
                .email("\"useruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserus\" +\n" +
                        "                        \"useruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserus\" +\n" +
                        "                        \"useruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserususeruserus")
                .username("username")
                .build();

        UserInvalidParameterException userInvalidParameterException = assertThrows(
                UserInvalidParameterException.class,
                () -> userServiceImplementation.updateUser(userUpdate));

        Assert.assertEquals("Username or email invalid, try yet", userInvalidParameterException.getMessage());
    }

    @Test
    public void DeleteUserTest_WithExistsUser_ReturnTrue() throws UserNotFoundException, SQLException, UserInvalidParameterException {
        UserDto userSave = UserDto.builder()
                .email("testsaveuser@mail.cas")
                .username("usertest")
                .build();
        Long userSaveResult = userServiceImplementation.createUser(userSave);

        Long userDeleteResult = userServiceImplementation.deleteUser(userSaveResult);
        assertTrue(userDeleteResult>0);
    }

    @Test
    public void DeleteUserTest_WithNonExistsUser_ReturnTrue(){
        UserNotFoundException userNotFoundException = assertThrows(
               UserNotFoundException.class,
                () -> userServiceImplementation.deleteUser(929292L));

        Assert.assertEquals("User with id - 929292 not found", userNotFoundException.getMessage());
    }

    @Test
    public void GetUserByIdTest_WithId778_ReturnTrue() throws UserNotFoundException, UserInvalidParameterException {
        UserDto userSave = UserDto.builder()
                .email("testsaveuser@mail.cas")
                .username("usertest")
                .build();

        Long userSaveResult = userServiceImplementation.createUser(userSave);

        UserDto userById = userServiceImplementation.getUserById(userSaveResult);
        Assert.assertEquals("usertest", userById.getUsername());
    }

    @Test
    public void GetUserByIdTest_WithNonExistsUser_ReturnTrue(){
        UserNotFoundException userNotFoundException = assertThrows(
                UserNotFoundException.class,
                () -> userServiceImplementation.getUserById(929292L));

        Assert.assertEquals("User with id - 929292 not found", userNotFoundException.getMessage());
    }

    @Test
    public void GetUserByUsernameTest_WithUserNameTestUsr_ReturnTrue() throws UserNotFoundException, SQLException, UserInvalidParameterException {
        UserDto userSave = UserDto.builder()
                .email("testsaveuser@mail.cas")
                .username("usertestfind")
                .build();

        userServiceImplementation.createUser(userSave);

        UserDto userByUsername = userServiceImplementation.getUserByUsername("usertestfind");
        Assert.assertEquals("usertestfind", userByUsername.getUsername());
    }

    @Test
    public void GetUserByUsernameTest_WithNonExistsUser_ReturnTrue(){
        UserNotFoundException userNotFoundException = assertThrows(
                UserNotFoundException.class,
                () -> userServiceImplementation.getUserByUsername("Nonexists"));

        Assert.assertEquals("User with username Nonexists not found", userNotFoundException.getMessage());
    }

    @Test
    public void GetAllUserTest_ReturnTrue() throws TransactionException, UserInvalidParameterException {
        UserDto userSave = UserDto.builder()
                .email("testsaveuser@mail.cas")
                .username("usertest")
                .build();
        userServiceImplementation.createUser(userSave);
        List<UserDto> allUsers = userServiceImplementation.getAllUsers();
        Assert.assertTrue(allUsers.size()>0);
    }
}