package com.aston.dao.api;

import com.aston.entities.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDaoApi {
    int createUser(User user) throws  SQLException;
    User getUserById(int userId) throws  SQLException;
    User getUserByUsername(String username) throws  SQLException;
    List<User> getAllUsers() throws  SQLException;
    int updateUser(User user) throws  SQLException;
    int deleteUser(int userId) throws  SQLException;
}
