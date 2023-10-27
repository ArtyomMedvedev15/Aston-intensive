package com.aston.dao.implementation;

import com.aston.dao.api.UserDaoApi;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.entities.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class UserDaoImplementation implements UserDaoApi {
    private static final String INSERT_USER_QUERY = "INSERT INTO taskmaneger.users(username,email)VALUES(?,?)";
    private static final String SELECT_USER_BY_ID_QUERY = "SELECT * FROM taskmaneger.users WHERE id=?";
    private static final String SELECT_USER_BY_USERNAME_QUERY= "SELECT * FROM taskmaneger.users WHERE username=?";
    private static final String UPDATE_USER_QUERY = "UPDATE taskmaneger.users SET email=?, username=? WHERE id=?";
    private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM taskmaneger.users";
    private static final String DELETE_USER_QUERY = "DELETE FROM taskmaneger.users WHERE id=?";

     private final ConnectionManager connectionManager;

    public UserDaoImplementation(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }


    @Override
    public int createUser(User user) throws SQLException {
        Connection connection = connectionManager.getConnection();
        try (PreparedStatement pst = connection.prepareStatement(INSERT_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getEmail());
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                int id = rs.getInt(1);
                log.info("Save new user with id {} in {}",id,new Date());
                return id;
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }
    @Override
    public int updateUser(User user) throws SQLException {
        Connection connection = connectionManager.getConnection();
        int rowsUpdated = 0;
        try (PreparedStatement pst = connection.prepareStatement(UPDATE_USER_QUERY)) {
            pst.setString(2, user.getUsername());
            pst.setString(1, user.getEmail());
            pst.setLong(3, user.getId());
            rowsUpdated = pst.executeUpdate();

        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
        log.info("Update user with id {} in {}",rowsUpdated,new Date());
        return rowsUpdated;
    }

    @Override
    public int deleteUser(int userId) throws SQLException {
        Connection connection = connectionManager.getConnection();
        int updated_rows;

        try (PreparedStatement pst = connection.prepareStatement(DELETE_USER_QUERY)) {
            pst.setLong(1, userId);
            updated_rows = pst.executeUpdate();
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
        log.info("Delete user with id {} in {}",userId,new Date());
        return updated_rows;
    }
    @Override
    public User getUserById(int userId) throws SQLException {
        Connection connection = connectionManager.getConnection();
        User dbUser = null;
        try (PreparedStatement pst = connection.prepareStatement(SELECT_USER_BY_ID_QUERY)) {
            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    dbUser = parseUserFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
        log.info("Get user by id with {} in {}",userId,new Date());
        return dbUser;
    }

    @Override
    public User getUserByUsername(String username) throws SQLException {
        Connection connection = connectionManager.getConnection();
        User dbUser = null;
        try (PreparedStatement pst = connection.prepareStatement(SELECT_USER_BY_USERNAME_QUERY)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    dbUser = parseUserFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
        log.info("Get user by username with {} in {}",username,new Date());
        return dbUser;
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        Connection connection = connectionManager.getConnection();
        List<User> usersList = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(SELECT_ALL_USERS_QUERY)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    usersList.add(parseUserFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
        log.info("Get all user list in {}", new Date());
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
