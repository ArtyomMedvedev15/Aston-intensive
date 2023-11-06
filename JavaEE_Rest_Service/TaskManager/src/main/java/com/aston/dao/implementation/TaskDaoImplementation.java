package com.aston.dao.implementation;

import com.aston.dao.api.TaskDaoApi;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.entities.Project;
import com.aston.entities.Task;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.*;
import java.util.*;
import java.util.Date;

@Slf4j
public class TaskDaoImplementation implements TaskDaoApi {
     private final SessionFactory sessionFactory;

    public TaskDaoImplementation( SessionFactory sessionFactory) {
         this.sessionFactory = sessionFactory;
    }


    @Override
    public Long createTask(Task task){
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(task);
                session.flush();
                transaction.commit();
                log.info("Save new task with id {} in {}", task.getId(), new Date());
                return task.getId();
            } catch (Exception ex) {
                transaction.rollback();
                log.error("Error while saving task: " + ex.getMessage(), ex);
                ex.printStackTrace();
                return (long) -1;
            }
        }
    }

    @Override
    public Task getTaskById(Long taskId){
        Task task = null;
        try (Session session = sessionFactory.openSession()) {
            task = session.get(Task.class, taskId);
            log.info("Get task with id {} in {}",taskId,new Date());
        } catch (Exception e) {
            log.error("Error getting task by ID", e);
        }
        log.info("Get task by id {} in {}", taskId, new Date());
        return task;
    }

    @Override
    public List<Task> getAllTasks(){
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Task> query = criteriaBuilder.createQuery(Task.class);
            Root<Task> root = query.from(Task.class);
            query.select(root);

            TypedQuery<Task> typedQuery = session.createQuery(query);
            List<Task> taskList = typedQuery.getResultList();

            log.info("Get all task list in {}", new Date());
            return taskList;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            ex.printStackTrace();
            throw ex;
        }
    }


    @Override
    public Long updateTask(Task task){
        long rowsUpdated = 0;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.update(task);
                transaction.commit();
                rowsUpdated = 1;
                log.info("Update task with id {} in {}",task.getId(),new Date());
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
                log.error("Error updating task", e);
            }
        }
        log.info("Update task with id {} in {}", task.getId(), new Date());
        return rowsUpdated;
    }

    @Override
    public Long deleteTask(Long taskId){
        long rowsDeleted = 0;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Task taskDelete = session.get(Task.class, taskId);
                if (taskDelete != null) {
                    session.remove(taskDelete);
                    transaction.commit();
                    rowsDeleted = 1;
                    log.info("Delete task with id {} in {}",taskDelete.getId(),new Date());
                }
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
                log.error("Error deleting user", e);
            }
        }
        return rowsDeleted;
    }

}
