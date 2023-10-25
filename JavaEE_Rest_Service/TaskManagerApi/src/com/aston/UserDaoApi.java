package com.aston;

import com.aston.entities.User;

import java.util.List;

public interface UserDaoApi {
    void createUser(User user);
    User getUserById(int userId);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(int userId);
}
