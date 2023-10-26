package com.aston.service.api;

import com.aston.entities.User;
import com.aston.util.dto.UserDto;

import java.sql.SQLException;
import java.util.List;

public interface UserServiceApi {
    int createUser(UserDto userSave) throws SQLException;
    UserDto getUserById(int userId) throws  SQLException;
    UserDto getUserByUsername(String username) throws  SQLException;
    List<UserDto> getAllUsers() throws  SQLException;
    int updateUser(UserDto userUpdate) throws  SQLException;
    int deleteUser(int userId) throws  SQLException;
}
