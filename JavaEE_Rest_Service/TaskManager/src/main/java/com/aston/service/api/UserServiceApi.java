package com.aston.service.api;

import com.aston.entities.User;
import com.aston.util.UserInvalidParameter;
import com.aston.util.UserNotFoundException;
import com.aston.util.dto.UserDto;

import java.sql.SQLException;
import java.util.List;

public interface UserServiceApi {
    int createUser(UserDto userSave) throws SQLException, UserInvalidParameter;
    UserDto getUserById(int userId) throws SQLException, UserNotFoundException;
    UserDto getUserByUsername(String username) throws SQLException, UserNotFoundException;
    List<UserDto> getAllUsers() throws  SQLException;
    int updateUser(UserDto userUpdate) throws SQLException, UserInvalidParameter;
    int deleteUser(int userId) throws SQLException, UserNotFoundException;
}
