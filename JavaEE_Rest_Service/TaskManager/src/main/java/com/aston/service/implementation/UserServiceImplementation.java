package com.aston.service.implementation;

import com.aston.dao.api.TransactionManager;
import com.aston.dao.api.UserDaoApi;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.entities.User;
import com.aston.service.api.UserServiceApi;
import com.aston.util.UserInvalidParameterException;
import com.aston.util.UserNotFoundException;
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
    private ConnectionManager connectionManager;
    public UserServiceImplementation(UserDaoApi userDaoApi,ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.userDaoApi = userDaoApi;
        this.transactionManager = connectionManager.getTransactionManager();
    }

    @Override
    public int createUser(UserDto userSaveDto) throws SQLException, UserInvalidParameterException {
        User userEntity;
        userEntity = ValidateUserDto(userSaveDto);
        int userId = 0;
        try {
             userId = userDaoApi.createUser(userEntity);
         } catch (SQLException e) {
            log.error("Cannot save user get exception {}", e.getMessage());
            throw e;
         }
        return userId;
    }

    private User ValidateUserDto(UserDto userSaveDto) throws UserInvalidParameterException {
        User userEntity;
        if((userSaveDto.getUsername().length()>5 && userSaveDto.getUsername().length()<256)
                && userSaveDto.getEmail().length()>8 && userSaveDto.getEmail().length()<256){
            userEntity = fromDto(userSaveDto);
        }else{
            throw new UserInvalidParameterException("Username or email invalid, try yet");
        }
        return userEntity;
    }

    @Override
    public int updateUser(UserDto userUpdate) throws SQLException, UserInvalidParameterException {
        User userEntity;
        userEntity = ValidateUserDto(userUpdate);
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
    public int deleteUser(int userId) throws SQLException, UserNotFoundException {
        try {
            User userEntity = userDaoApi.getUserById(userId);
            if(userEntity!=null){
                userId = userDaoApi.deleteUser(userId);
            }else{
                throw new UserNotFoundException(String.format("User with id - %s not found",userId));
            }
        } catch (SQLException e) {
            log.error("Cannot delete user get exception {}", e.getMessage());
            throw e;
        }
        return userId;
    }
    @Override
    public UserDto getUserById(int userId) throws SQLException, UserNotFoundException {
        UserDto userDto;
        try {
            User userEntity = userDaoApi.getUserById(userId);
            if(userEntity!=null){
                userDto = fromEntity(userEntity);
            }else{
                throw new UserNotFoundException(String.format("User with id - %s not found",userId));
            }
        } catch (SQLException e) {
            log.error("Cannot get user by id with exception {}",e.getMessage());
           throw e;
        }
        return userDto;
    }

    @Override
    public UserDto getUserByUsername(String username) throws SQLException, UserNotFoundException {
        UserDto userDto;
        try {
            User userByUsername = userDaoApi.getUserByUsername(username);
            if(userByUsername!=null) {
                userDto = fromEntity(userByUsername);
            }else{
                throw new UserNotFoundException(String.format("User with username %s not found",username));
            }
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
