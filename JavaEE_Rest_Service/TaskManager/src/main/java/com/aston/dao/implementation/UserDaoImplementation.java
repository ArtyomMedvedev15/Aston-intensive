package com.aston.dao.implementation;

import com.aston.dao.api.TransactionManager;
import com.aston.dao.api.UserDaoApi;
import com.aston.entities.User;
import com.aston.util.TransactionManagerException;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UserDaoImplementation implements UserDaoApi {
    private static final String INSERT_USER_QUERY = "INSERT INTO users(username,email)VALUES(?,?)";
    private static final String SELECT_USER_BY_ID_QUERY = "SELECT * FROM users WHERE id=?";
    private static final String SELECT_USER_BY_USERNAME_QUERY= "SELECT * FROM users WHERE username=?";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET email=?, username=? WHERE id=?";
    private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id=?";

    private final TransactionManager transactionManager;

    public UserDaoImplementation(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }


    @Override
    public int createUser(User user) throws SQLException {
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(INSERT_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getEmail());
            pst.executeUpdate();
            transactionManager.commitSession();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                int id = rs.getInt(1);
                return id;
            }
        } catch (SQLException ex) {
            transactionManager.rollbackSession();
            log.error(ex.getMessage(), ex);
            throw ex;
        }finally {
            transactionManager.close();
        }
    }
    @Override
    public int updateUser(User user) throws SQLException {
        int rowsUpdated = 0;
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(UPDATE_USER_QUERY)) {
            pst.setString(2, user.getUsername());
            pst.setString(1, user.getEmail());
            pst.setLong(3, user.getId());
            rowsUpdated = pst.executeUpdate();
            transactionManager.commitSession();
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            transactionManager.rollbackSession();
            throw ex;
        }finally {
            transactionManager.close();
        }
        return rowsUpdated;
    }

    @Override
    public int deleteUser(int userId) throws SQLException {
        int updated_rows;
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(DELETE_USER_QUERY)) {
            pst.setLong(1, userId);
            updated_rows = pst.executeUpdate();
            transactionManager.commitSession();
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            transactionManager.rollbackSession();
            throw ex;
        }finally {
            transactionManager.close();
        }
        return updated_rows;
    }
    @Override
    public User getUserById(int userId) throws SQLException {
        User dbUser = null;
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SELECT_USER_BY_ID_QUERY)) {
            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    dbUser = parseUserFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            transactionManager.rollbackSession();
            throw ex;
        }
        return dbUser;
    }

    @Override
    public User getUserByUsername(String username) throws SQLException {
        User dbUser = null;
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SELECT_USER_BY_USERNAME_QUERY)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    dbUser = parseUserFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            transactionManager.rollbackSession();
            throw ex;
        }
        return dbUser;
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        List<User> usersList = new ArrayList<>();
        transactionManager.beginSession();
        try (Connection connection = transactionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SELECT_ALL_USERS_QUERY)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    usersList.add(parseUserFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            transactionManager.rollbackSession();
            throw ex;
        }
        return usersList;
    }



    private User parseUserFromResultSet(ResultSet rs) throws SQLException {
        User userMapper = User.builder().build();
        userMapper.setId((long) Integer.parseInt(rs.getString("id")));
        userMapper.setUsername(rs.getString("username"));
        userMapper.setEmail(rs.getString("email"));
        return userMapper;
    }
}
