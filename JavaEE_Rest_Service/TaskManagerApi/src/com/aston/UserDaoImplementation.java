package com.aston;

import com.aston.entities.User;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class UserDaoImplementation implements UserDaoApi{
    private static final String INSERT_USER_QUERY = "INSERT INTO users(username,email)VALUES(?,?)";
    private static final String SELECT_USER_BY_ID_QUERY = "SELECT * FROM users WHERE id=?";
    private static final String SELECT_USER_BY_USERNAME_QUERY= "SELECT * FROM users WHERE username=?";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET email=?, username=? WHERE id=?";
    private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id=?";



    @Override
    public void createUser(User user) {

    }

    @Override
    public User getUserById(int userId) {
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deleteUser(int userId) {

    }
}
