package com.aston.dao.implementation;

import com.aston.dao.api.BugDaoApi;
import com.aston.entities.Bug;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Date;

@Slf4j
public class BugDaoImplementation implements BugDaoApi {

    private final SessionFactory sessionFactory;

    public BugDaoImplementation(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Long createBug(Bug bug) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(bug);
                session.flush();
                transaction.commit();
                log.info("Save new bug with id {} in {}", bug.getId(), new Date());
                return bug.getId();
            } catch (Exception ex) {
                transaction.rollback();
                log.error("Error while saving bug: " + ex.getMessage(), ex);
                ex.printStackTrace();
                return (long) -1;
            }
        }
    }

    @Override
    public Long deleteBug(Bug bug) {
        long bugId = 0;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                    session.remove(bug);
                    transaction.commit();
                    bugId = bug.getId();
                    log.info("Delete bug with id {} in {}",bugId,new Date());
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
                log.error("Error deleting bug", e);
            }
        }
        return bugId;
    }
}
