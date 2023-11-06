package com.aston.service.implementation;

import com.aston.dao.api.BugDaoApi;
import com.aston.entities.Bug;
import com.aston.service.api.BugServiceApi;
import com.aston.util.TransactionException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Date;

@Slf4j
public class BugServiceImplementation implements BugServiceApi {

    private final BugDaoApi bugDaoApi;
    private final SessionFactory sessionFactory;

    public BugServiceImplementation(BugDaoApi bugDaoApi, SessionFactory sessionFactory) {
        this.bugDaoApi = bugDaoApi;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Long createBug(Bug bug) {
         Long bugId;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                bugId = bugDaoApi.createBug(bug);
                transaction.commit();
                log.info("Save new bug with id {} in {}", bugId, new Date());
            }catch (Exception exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            }
        }
        return bugId;
    }

    @Override
    public Long deleteBug(Bug bug) {
        Long bugId;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                bugId = bugDaoApi.deleteBug(bug);
                transaction.commit();
                log.info("Delete bug with id {} in {}", bugId, new Date());
            }catch (Exception exception) {
                transaction.rollback();
                log.error("Cannot commit transaction, error with db");
                throw new TransactionException(String.format("Error with with database with message %s", exception.getMessage()));
            }
        }
        return bugId;
    }
}
