package com.aston.service.implementation;

import com.aston.dao.api.TransactionManager;
import com.aston.dao.api.UserDaoApi;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.entities.User;
import com.aston.service.api.UserServiceApi;
import com.aston.util.UserInvalidParameterException;
import com.aston.util.UserNotFoundException;
import com.aston.util.dto.UserDto;
import com.aston.util.dto.util.UserDtoUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.aston.util.dto.util.UserDtoUtil.fromEntity;

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
    public Long createUser(UserDto userSaveDto) throws UserInvalidParameterException {
        User userEntity;
        userEntity = ValidateUserDto(userSaveDto);
        long userId = userDaoApi.createUser(userEntity);
        log.info("Save new user with id {} in {}",userId,new Date());
        return userId;
    }

    private User ValidateUserDto(UserDto userSaveDto) throws UserInvalidParameterException {
        User userEntity;
        if((userSaveDto.getUsername().length()>5 && userSaveDto.getUsername().length()<256)
                && userSaveDto.getEmail().length()>8 && userSaveDto.getEmail().length()<256){
            userEntity = UserDtoUtil.fromDto(userSaveDto);
        }else{
            throw new UserInvalidParameterException("Username or email invalid, try yet");
        }
        return userEntity;
    }

    @Override
    public Long updateUser(UserDto userUpdate) throws UserInvalidParameterException {
        User userEntity;
        userEntity = ValidateUserDto(userUpdate);
        long userId = userDaoApi.updateUser(userEntity);
        log.info("Update user with id {} in {}",userId,new Date());
        return userId;
    }

    @Override
    public Long deleteUser(Long userId) throws UserNotFoundException {
        User userEntity = userDaoApi.getUserById(userId);
        if(userEntity!=null){
            userId = userDaoApi.deleteUser(userId);
            log.info("Delete user with id {} in {}",userId,new Date());
        }else{
            log.error("Cannot delete user with id {} throw exception in {}",userId,new Date());
            throw new UserNotFoundException(String.format("User with id - %s not found",userId));
        }
        return userId;
    }
    @Override
    public UserDto getUserById(Long userId) throws UserNotFoundException {
        UserDto userDto;
        User userEntity = userDaoApi.getUserById(userId);
        if(userEntity!=null){
            log.info("Get user with id {} in {}",userId,new Date());
            userDto = fromEntity(userEntity);
        }else{
            log.error("Cannot get user with id {} throw exception in {}",userId,new Date());
            throw new UserNotFoundException(String.format("User with id - %s not found",userId));
        }
        return userDto;
    }

    @Override
    public UserDto getUserByUsername(String username) throws SQLException, UserNotFoundException {
        UserDto userDto;
        User userByUsername = userDaoApi.getUserByUsername(username);
        if(userByUsername!=null) {
            userDto = fromEntity(userByUsername);
            log.info("Get user with username {} in {}",username,new Date());
        }else{
            log.error("Cannot get user with username {} throw exception in {}",username,new Date());
            throw new UserNotFoundException(String.format("User with username %s not found",username));
        }
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers(){
        List<UserDto> userDtoList;
        userDtoList = userDaoApi.getAllUsers().stream().map(UserDtoUtil::fromEntity)
                .collect(Collectors.toList());
        log.info("Get user list in {}",new Date());
        return userDtoList;
    }




}
