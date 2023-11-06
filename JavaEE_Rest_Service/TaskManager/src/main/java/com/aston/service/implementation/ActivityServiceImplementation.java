package com.aston.service.implementation;

import com.aston.dao.api.ActivityDaoApi;
import com.aston.entities.Activity;
import com.aston.service.api.ActivityServiceApi;
import com.aston.util.TransactionException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;

@Slf4j
public class ActivityServiceImplementation implements ActivityServiceApi {

    private final SessionFactory sessionFactory;
    private final ActivityDaoApi activityDaoApi;

    public ActivityServiceImplementation(SessionFactory sessionFactory, ActivityDaoApi activityDaoApi) {
        this.sessionFactory = sessionFactory;
        this.activityDaoApi = activityDaoApi;
    }

    @Override
    public List<Activity> getAllActivity() {
        List<Activity> activities;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                activities = activityDaoApi.getAllActivity();
                transaction.commit();
                log.info("Get all activities in {}",new Date());
            }catch (Exception exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            }
        }
        return activities;
    }
}
