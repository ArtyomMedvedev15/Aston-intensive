package com.aston.service.implementation;

import com.aston.dao.api.TransactionManager;
import com.aston.dao.api.UserDaoApi;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.entities.User;
import com.aston.service.api.UserServiceApi;
import com.aston.util.TransactionException;
import com.aston.util.dto.UserDto;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class UserServiceImplementation implements UserServiceApi {

    private final UserDaoApi userDaoApi;
    private TransactionManager transactionManager;
    private final ConnectionManager connectionManager;
    public UserServiceImplementation(UserDaoApi userDaoApi,ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.userDaoApi = userDaoApi;
        this.transactionManager = connectionManager.getTransactionManager();
    }

    @Override
    public int createUser(UserDto userSaveDto) throws SQLException {
        User userEntity = fromDto(userSaveDto);
        int userId = 0;
        try {
            transactionManager.beginTransaction();
             userId = userDaoApi.createUser(userEntity);
             transactionManager.commitTransaction();
         } catch (SQLException e) {
            transactionManager.rollbackTransaction();
            log.error("Cannot save user get exception {}", e.getMessage());
            throw e;
         }
        return userId;
    }
    @Override
    public int updateUser(UserDto userUpdate) throws SQLException {
        User userEntity = fromDto(userUpdate);
        int userId = 0;
        try {
            transactionManager.beginTransaction();
            userId = userDaoApi.updateUser(userEntity);
            transactionManager.commitTransaction();
        } catch (SQLException e) {
            transactionManager.rollbackTransaction();
            log.error("Cannot update user get exception {}", e.getMessage());
            throw e;
        }
        return userId;
    }

    @Override
    public int deleteUser(int userId) throws SQLException {
        try {
            transactionManager.beginTransaction();
            userId = userDaoApi.deleteUser(userId);
            transactionManager.commitTransaction();
        } catch (SQLException e) {
            transactionManager.rollbackTransaction();
            log.error("Cannot delete user get exception {}", e.getMessage());
            throw e;
        }
        return userId;
    }
    @Override
    public UserDto getUserById(int userId) throws SQLException {
        UserDto userDto;
        try {
            transactionManager.beginTransaction();
            userDto = fromEntity(userDaoApi.getUserById(userId));
            transactionManager.commitTransaction();
        } catch (SQLException e) {
            transactionManager.rollbackTransaction();
            log.error("Cannot get user by id with exception {}",e.getMessage());
           throw e;
        }
        return userDto;
    }

    @Override
    public UserDto getUserByUsername(String username) throws SQLException {
        UserDto userDto;
        try {
            transactionManager.beginTransaction();
            userDto = fromEntity(userDaoApi.getUserByUsername(username));
            transactionManager.commitTransaction();
        } catch (SQLException e) {
            transactionManager.rollbackTransaction();
            log.error("Cannot get user by username with exception {}",e.getMessage());
            throw e;
        }
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers() throws TransactionException {
        List<UserDto> userDtoList = new ArrayList<>();
         try {
             transactionManager.beginTransaction();
             userDtoList = userDaoApi.getAllUsers().stream().map(this::fromEntity)
                     .collect(Collectors.toList());
             transactionManager.commitTransaction();
        } catch (SQLException e) {
             transactionManager.rollbackTransaction();
            log.error("Cannot get all user with exception {}",e.getMessage());
            e.printStackTrace();
         }
        return userDtoList;
    }



    private User fromDto(UserDto dto) {
        User userEntity = User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .build();
        return userEntity;
    }

    private UserDto fromEntity(User userEntity) {
        UserDto userDto = UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .build();
        return userDto;
    }
}
