package com.aston.dao.implementation;

import com.aston.dao.api.UserTaskDaoApi;
import com.aston.entities.Task;
import com.aston.entities.User;
import com.aston.entities.UserTask;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

@Slf4j
public class UserTaskDaoImplementation implements UserTaskDaoApi {

    private final SessionFactory sessionFactory;

    public UserTaskDaoImplementation(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Long createUserTask(UserTask userTask) {
        Long userId = 0L;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(userTask);
                session.flush();
                userId = userTask.getUser().getId();
                transaction.commit();
                log.info("Save new user task with id {} in {}", userTask.getUser().getId(), new Date());
            }catch (Exception ex) {
                transaction.rollback();
                log.error("Error while saving project: " + ex.getMessage(), ex);
                ex.printStackTrace();
            }
        }
        return userId;
    }

    @Override
    public List<UserTask> getAllUsersTaskByUser(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<UserTask> criteria = builder.createQuery(UserTask.class);
            Root<UserTask> root = criteria.from(UserTask.class);
            criteria.select(root);
            Join<UserTask, User> userJoin = root.join("user", JoinType.INNER);
            Predicate userPredicate = builder.equal(userJoin.get("id"), userId);
            criteria.where(userPredicate);
            Query<UserTask> query = session.createQuery(criteria);
            List<UserTask> userTasks = query.getResultList();

            log.info("Get user tasks by user id {} in {}", userId, new Date());
            return userTasks;
        } catch (Exception e) {
            log.error("Error getting user tasks by user id", e);
            throw e;
        }
    }

    @Override
    public UserTask getUserTaskByUserAndTask(User user, Task task) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<UserTask> criteria = builder.createQuery(UserTask.class);
            Root<UserTask> root = criteria.from(UserTask.class);

            Predicate userCondition = builder.equal(root.get("user"), user);
            Predicate taskCondition = builder.equal(root.get("task"), task);

            criteria.where(userCondition, taskCondition);

            Query<UserTask> query = session.createQuery(criteria);
            List<UserTask> userTasks = query.getResultList();

            return userTasks.isEmpty() ? null : userTasks.get(0);
        }
    }
    @Override
    public Long deleteUserTask(UserTask userTaskDelete) {
        long rowsDeleted = 0;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                UserTask userTask = session.get(UserTask.class, userTaskDelete.getId());
                if (userTask != null) {
                    session.remove(userTask);
                    transaction.commit();
                    rowsDeleted = 1;
                }
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                log.error("Error deleting user task", e);
            }
        }

        log.info("Delete user task with id {} in {}", userTaskDelete.getId(), new Date());
        return rowsDeleted;
    }

}
