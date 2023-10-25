package com.aston.dao.api;

import com.aston.entities.User;
import com.aston.util.TransactionManagerException;

import java.sql.SQLException;
import java.util.List;

public interface UserDaoApi {
    int createUser(User user) throws TransactionManagerException, SQLException;
    User getUserById(int userId) throws TransactionManagerException, SQLException;
    User getUserByUsername(String username) throws TransactionManagerException, SQLException;
    List<User> getAllUsers() throws TransactionManagerException, SQLException;
    int updateUser(User user) throws TransactionManagerException, SQLException;
    int deleteUser(int userId) throws TransactionManagerException, SQLException;
}
