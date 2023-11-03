package com.aston.dao.implementation;

import com.aston.dao.api.ProjectDaoApi;
import com.aston.dao.api.TransactionManager;
import com.aston.dao.datasource.ConnectionManager;
import com.aston.entities.Project;
import com.aston.entities.User;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
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
public class ProjectDaoImplementation implements ProjectDaoApi {
    private final SessionFactory sessionFactory;
    public ProjectDaoImplementation(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Long createProject(Project project){
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(project);
                session.flush();
                transaction.commit();
                log.info("Save new user with id {} in {}", project.getId(), new Date());
                return project.getId();
            } catch (Exception ex) {
                transaction.rollback();
                log.error("Error while saving user: " + ex.getMessage(), ex);
                ex.printStackTrace();
                return (long) -1;
            }
        }
    }
    @Override
    public Long updateProject(Project project){
        long rowsUpdated = 0;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.merge(project);
                transaction.commit();
                rowsUpdated = 1;
                log.info("Update project with id {} in {}",project.getId(),new Date());
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
                log.error("Error updating project", e);
            }
        }
        log.info("Update project with id {} in {}", project.getId(), new Date());
        return rowsUpdated;
    }

    @Override
    public Long deleteProject(Long projectId){
        long rowsDeleted = 0;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Project projectDelete = session.get(Project.class, projectId);
                if (projectDelete != null) {
                    session.remove(projectDelete);
                    transaction.commit();
                    rowsDeleted = 1;
                    log.info("Delete project with id {} in {}",projectDelete.getId(),new Date());
                }
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
                log.error("Error deleting user", e);
            }
        }
        return rowsDeleted;
    }

    @Override
    public Project getProjectById(Long projectId){
            Project project = null;
            try (Session session = sessionFactory.openSession()) {
                project = session.get(Project.class, projectId);
                log.info("Get project with id {} in {}",projectId,new Date());
            } catch (Exception e) {
                log.error("Error getting user by ID", e);
            }
            log.info("Get project by id {} in {}", project, new Date());
            return project;
    }

    @Override
    public  List<Project> getProjectByName(String name){
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Project> criteria = builder.createQuery(Project.class);
            Root<Project> root = criteria.from(Project.class);

            Predicate condition = builder.like(root.get("name"), "%" + name + "%");
            criteria.where(condition);

            Query<Project> query = session.createQuery(criteria);
            log.info("Get projects with name containing {} in {}", name, new Date());
            return query.getResultList();
        } catch (Exception e) {
            log.error("Error getting projects by name", e);
            throw e;
        }
    }

    @Override
    public List<Project> getAllProject(){
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Project> query = criteriaBuilder.createQuery(Project.class);
            Root<Project> root = query.from(Project.class);
            query.select(root);

            TypedQuery<Project> typedQuery = session.createQuery(query);
            List<Project> projectList = typedQuery.getResultList();

            log.info("Get all project list in {}", new Date());
            return projectList;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            ex.printStackTrace();
            throw ex;
        }
    }
}
