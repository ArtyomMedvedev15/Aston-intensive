package com.aston.dao.implementation;

import com.aston.dao.api.ActivityDaoApi;
import com.aston.entities.Activity;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.List;

@Slf4j
public class ActivityDaoImplementation implements ActivityDaoApi {

    private final SessionFactory sessionFactory;

    public ActivityDaoImplementation(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Activity> getAllActivity() {
        try (Session session = sessionFactory.openSession()) {
            TypedQuery<Activity> query = session.createQuery("SELECT a FROM Activity a", Activity.class);
            List<Activity> activities = query.getResultList();
            log.info("Get all activities in {}",new Date());
            return activities;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            ex.printStackTrace();
            throw ex;
        }
    }
}
