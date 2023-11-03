package com.aston.dao.implementation;

import com.aston.dao.api.UserDaoApi;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.entities.User;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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
     private final SessionFactory sessionFactory;

    public UserDaoImplementation(ConnectionManager connectionManager, SessionFactory sessionFactory) {
        this.connectionManager = connectionManager;
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Long createUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(user);
                session.flush();
                transaction.commit();
                log.info("Save new user with id {} in {}", user.getId(), new Date());
                return user.getId();
            } catch (Exception ex) {
                transaction.rollback();
                log.error("Error while saving user: " + ex.getMessage(), ex);
                ex.printStackTrace();
                return (long) -1;
            }
        }
    }
    @Override
    public int updateUser(User user) throws SQLException {

        int rowsUpdated = 0;
        try ( Connection connection = connectionManager.getConnection();
                PreparedStatement pst = connection.prepareStatement(UPDATE_USER_QUERY)) {
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

        int updated_rows;

        try (Connection connection = connectionManager.getConnection();
                PreparedStatement pst = connection.prepareStatement(DELETE_USER_QUERY)) {
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

        User dbUser = null;
        try (Connection connection = connectionManager.getConnection();
                PreparedStatement pst = connection.prepareStatement(SELECT_USER_BY_ID_QUERY)) {
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

        User dbUser = null;
        try (Connection connection = connectionManager.getConnection();
                PreparedStatement pst = connection.prepareStatement(SELECT_USER_BY_USERNAME_QUERY)) {
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
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root);

            TypedQuery<User> typedQuery = session.createQuery(query);
            List<User> usersList = typedQuery.getResultList();

            log.info("Get all user list in {}", new Date());
            return usersList;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            ex.printStackTrace();
            throw ex;
        }
    }



    private User parseUserFromResultSet(ResultSet rs) throws SQLException {
        User userMapper = new User();
        userMapper.setId((long) Integer.parseInt(rs.getString("id")));
        userMapper.setUsername(rs.getString("username"));
        userMapper.setEmail(rs.getString("email"));
        return userMapper;
    }
}
