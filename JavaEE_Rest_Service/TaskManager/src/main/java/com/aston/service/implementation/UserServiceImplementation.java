package com.aston.service.implementation;

import com.aston.dao.api.TransactionManager;
import com.aston.dao.api.UserDaoApi;
import com.aston.entities.User;
import com.aston.service.api.UserServiceApi;
import com.aston.util.dto.UserDto;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class UserServiceImplementation implements UserServiceApi {

    private final UserDaoApi userDaoApi;
    private final TransactionManager transactionManager;

    public UserServiceImplementation(UserDaoApi userDaoApi, TransactionManager transactionManager) {
        this.userDaoApi = userDaoApi;
        this.transactionManager = transactionManager;
    }

    @Override
    public int createUser(UserDto userSaveDto) throws SQLException {
        User userEntity = fromDto(userSaveDto);
        int userId = 0;
        try {
             userId = userDaoApi.createUser(userEntity);
         } catch (SQLException e) {
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
            userId = userDaoApi.updateUser(userEntity);
        } catch (SQLException e) {
            log.error("Cannot update user get exception {}", e.getMessage());
            throw e;
        }
        return userId;
    }

    @Override
    public int deleteUser(int userId) throws SQLException {
        try {
            userId = userDaoApi.deleteUser(userId);
        } catch (SQLException e) {
            log.error("Cannot delete user get exception {}", e.getMessage());
            throw e;
        }
        return userId;
    }
    @Override
    public UserDto getUserById(int userId) throws SQLException {
        UserDto userDto;
        try {
            userDto = fromEntity(userDaoApi.getUserById(userId));
        } catch (SQLException e) {
            log.error("Cannot get user by id with exception {}",e.getMessage());
           throw e;
        }
        return userDto;
    }

    @Override
    public UserDto getUserByUsername(String username) throws SQLException {
        UserDto userDto;
        try {
            userDto = fromEntity(userDaoApi.getUserByUsername(username));
        } catch (SQLException e) {
            log.error("Cannot get user by username with exception {}",e.getMessage());
            throw e;
        }
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers(){
        List<UserDto> userDtoList = new ArrayList<>();
         try {
             userDtoList = userDaoApi.getAllUsers().stream().map(this::fromEntity)
                     .collect(Collectors.toList());
        } catch (SQLException e) {
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
