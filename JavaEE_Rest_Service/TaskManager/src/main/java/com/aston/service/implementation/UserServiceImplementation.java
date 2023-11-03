package com.aston.service.implementation;

import com.aston.dao.api.UserDaoApi;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.entities.User;
import com.aston.service.api.UserServiceApi;
import com.aston.util.TransactionException;
import com.aston.util.UserInvalidParameterException;
import com.aston.util.UserNotFoundException;
import com.aston.util.dto.UserDto;
import com.aston.util.dto.util.UserDtoUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.aston.util.dto.util.UserDtoUtil.fromEntity;

@Slf4j
public class UserServiceImplementation implements UserServiceApi {

    private final UserDaoApi userDaoApi;
    private final SessionFactory sessionFactory;

    public UserServiceImplementation(UserDaoApi userDaoApi, SessionFactory sessionFactory1) {
        this.sessionFactory = sessionFactory1;
        this.userDaoApi = userDaoApi;
    }

    @Override
    public Long createUser(UserDto userSaveDto) throws UserInvalidParameterException {
        User userEntity;
        userEntity = ValidateUserDto(userSaveDto);
        long userId = 0;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                userId = userDaoApi.createUser(userEntity);
                transaction.commit();
                log.info("Save new user with id {} in {}", userId, new Date());
            } catch (Exception exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            }
        }
        return userId;
    }

    private User ValidateUserDto(UserDto userSaveDto) throws UserInvalidParameterException {
        User userEntity;
        if ((userSaveDto.getUsername().length() > 5 && userSaveDto.getUsername().length() < 256)
                && userSaveDto.getEmail().length() > 8 && userSaveDto.getEmail().length() < 256) {
            userEntity = UserDtoUtil.fromDto(userSaveDto);
        } else {
            throw new UserInvalidParameterException("Username or email invalid, try yet");
        }
        return userEntity;
    }

    @Override
    public Long updateUser(UserDto userUpdate) throws UserInvalidParameterException {
        User userEntity;
        userEntity = ValidateUserDto(userUpdate);
        long userId = 0;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                userId = userDaoApi.updateUser(userEntity);
                transaction.commit();
                log.info("Update user with id {} in {}", userId, new Date());
            } catch (Exception exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            }
        }

        return userId;
    }

    @Override
    public Long deleteUser(Long userId) throws UserNotFoundException {
        User userEntity = userDaoApi.getUserById(userId);
        if (userEntity != null) {
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                try {
                    userId = userDaoApi.deleteUser(userId);
                    transaction.commit();
                    log.info("Delete user with id {} in {}", userId, new Date());
                } catch (Exception exception) {
                    transaction.rollback();
                    log.error("Cannot commit transaction, error with db");
                    throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
                }
            }
        } else {
            log.error("Cannot delete user with id {} throw exception in {}", userId, new Date());
            throw new UserNotFoundException(String.format("User with id - %s not found", userId));
        }
        return userId;
    }

    @Override
    public UserDto getUserById(Long userId) throws UserNotFoundException {
        UserDto userDto;
        User userEntity = userDaoApi.getUserById(userId);
        if (userEntity != null) {
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                try {
                    userDto = fromEntity(userEntity);
                    transaction.commit();
                    log.info("Get user with id {} in {}", userId, new Date());
                } catch (Exception exception) {
                    transaction.rollback();
                    log.error("Cannot commit transaction, error with db");
                    throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
                }
            }
        } else {
            log.error("Cannot get user with id {} throw exception in {}", userId, new Date());
            throw new UserNotFoundException(String.format("User with id - %s not found", userId));
        }
        return userDto;
    }

    @Override
    public UserDto getUserByUsername(String username) throws UserNotFoundException {
        UserDto userDto;
        User userByUsername = userDaoApi.getUserByUsername(username);
        if (userByUsername != null) {
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                try {
                    userDto = fromEntity(userByUsername);
                    transaction.commit();
                    log.info("Get user with username {} in {}", username, new Date());
                } catch (Exception exception) {
                    transaction.rollback();
                    log.error("Cannot commit transaction, error with db");
                    throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
                }
            }
        } else {
            log.error("Cannot get user with username {} throw exception in {}", username, new Date());
            throw new UserNotFoundException(String.format("User with username %s not found", username));
        }
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> userDtoList;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                userDtoList = userDaoApi.getAllUsers().stream().map(UserDtoUtil::fromEntity)
                        .collect(Collectors.toList());
                transaction.commit();
                log.info("Get user list in {}", new Date());
            } catch (Exception exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            }
            return userDtoList;
        }

    }
}
