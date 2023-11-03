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
import org.hibernate.query.Query;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class UserDaoImplementation implements UserDaoApi {
    private final SessionFactory sessionFactory;

    public UserDaoImplementation(SessionFactory sessionFactory) {
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
    public Long updateUser(User user){
        long rowsUpdated = 0;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.merge(user);
                transaction.commit();
                rowsUpdated = 1;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                log.error("Error updating user", e);
            }
        }

        log.info("Update user with id {} in {}", user.getId(), new Date());
        return rowsUpdated;
    }

    @Override
    public Long deleteUser(Long userId){
        long rowsDeleted = 0;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                User user = session.get(User.class, userId);
                if (user != null) {
                    session.remove(user);
                    transaction.commit();
                    rowsDeleted = 1;
                }
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                log.error("Error deleting user", e);
            }
        }

        log.info("Delete user with id {} in {}", userId, new Date());
        return rowsDeleted;
    }
    @Override
    public User getUserById(Long userId){
        User user = null;
        try (Session session = sessionFactory.openSession()) {
            user = session.get(User.class, userId);
        } catch (Exception e) {
            log.error("Error getting user by ID", e);
        }
        log.info("Get user by id {} in {}", userId, new Date());
        return user;
    }

    @Override
    public User getUserByUsername(String username){
        User user = null;
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);

            query.select(root).where(builder.equal(root.get("username"), username));
            Query<User> q = session.createQuery(query);

            List<User> userList = q.getResultList();
            if (!userList.isEmpty()) {
                user = userList.get(0);
            }
        } catch (Exception e) {
            log.error("Error getting user by username", e);
        }
        log.info("Get user by username {} in {}", username, new Date());
        return user;
    }

    @Override
    public List<User> getAllUsers(){
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
}
