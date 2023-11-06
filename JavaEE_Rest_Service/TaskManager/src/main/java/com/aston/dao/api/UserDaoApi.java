package com.aston.dao.api;

import com.aston.entities.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDaoApi {
    Long createUser(User user);
    User getUserById(Long userId);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    Long updateUser(User user);
    Long deleteUser(Long userId);
}
