package com.aston.dao.implementation;

import com.aston.dao.api.MeetingDaoApi;
import com.aston.entities.Meeting;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Date;

@Slf4j
public class MeetingDaoImplemntation implements MeetingDaoApi {

    private final SessionFactory sessionFactory;

    public MeetingDaoImplemntation(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Long createMeeting(Meeting meeting) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(meeting);
                session.flush();
                transaction.commit();
                log.info("Save new meeting with id {} in {}", meeting.getId(), new Date());
                return meeting.getId();
            } catch (Exception ex) {
                transaction.rollback();
                log.error("Error while saving meeting: " + ex.getMessage(), ex);
                ex.printStackTrace();
                return (long) -1;
            }
        }    }

    @Override
    public Long deleteMeeting(Meeting meeting) {
        long meetingId = 0;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.remove(meeting);
                transaction.commit();
                meetingId = meeting.getId();
                log.info("Delete meeting with id {} in {}",meetingId,new Date());
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
                log.error("Error deleting meeting", e);
            }
        }
        return meetingId;
    }
}
